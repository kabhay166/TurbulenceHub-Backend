package com.example.spring_example.service;


import com.example.spring_example.config.SimulationConfig;
import com.example.spring_example.entity.RunProcessInfo;
import com.example.spring_example.events.RunCompletedEvent;
import com.example.spring_example.service.run.HydroRunService;
import com.example.spring_example.service.run.MhdRunService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class ProcessManager {

    @Autowired
    private HydroRunService hydroRunService;

    @Autowired
    private MhdRunService mhdRunService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    private  final Map<String, RunProcessInfo> processes = new HashMap<>();
    private  final Map<String,BlockingQueue<String>> processOutputQueue = new HashMap<>();




    public String startProcess(String username, String timeOfRun,String sessionId,String kind, String dimension,String resolution,Long runId) throws IOException {

        try {
            String runProcessId = createRunProcessId(username,timeOfRun,sessionId);
            ProcessBuilder builder = new ProcessBuilder(
                    SimulationConfig.getPythonPath(),
                    SimulationConfig.getScriptPath()
            );
            builder.redirectErrorStream(true);
            Process newProcess = builder.start();

            BlockingQueue<String> outputQueue = new LinkedBlockingQueue<>(1000);
            processOutputQueue.put(runProcessId,outputQueue);
            RunProcessInfo newRunProcessInfo = new RunProcessInfo(newProcess,username,kind,dimension,resolution,runId,timeOfRun,runProcessId);
            new Thread(() -> captureProcessOutput(newRunProcessInfo,outputQueue)).start();

            processes.put(runProcessId,newRunProcessInfo);
            return runProcessId;
        } catch (Exception e) {
            return null;
        }

    }

    public boolean checkProcessCompletion(String runProcessInfoId) {
        return processes.get(runProcessInfoId).isCompleted();
    }

    public void captureProcessOutput(RunProcessInfo runProcessInfo, BlockingQueue<String> outputQueue) {
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(runProcessInfo.getProcess().getInputStream()))) {
            String line;
            while( (line = reader.readLine()) != null ) {
                if(!outputQueue.offer(line)) {
                    outputQueue.poll();
                    outputQueue.offer(line);
                }
                if(line.trim().equalsIgnoreCase("Done")){
                    runProcessInfo.setCompleted(true);
                    RunCompletedEvent event = RunCompletedEvent.builder()
                            .username(runProcessInfo.getUsername())
                            .kind(runProcessInfo.getKind())
                            .dimension(runProcessInfo.getDimension())
                            .resolution(runProcessInfo.getResolution())
                            .timeOfRun(runProcessInfo.getTimeOfRun())
                            .build();
                    applicationEventPublisher.publishEvent(event);
                    markRunCompleted(runProcessInfo.getProcessInfoId());
                    processes.remove(runProcessInfo.getProcessInfoId());
                }
            }

            markRunCompleted(runProcessInfo.getProcessInfoId());
            processes.remove(runProcessInfo.getProcessInfoId());
        } catch (IOException e) {
            System.out.println("Error occured while capturing output: " + e.getMessage());
        }
    }

    public boolean stopProcess(String runProcessInfoId) {

        try {
            RunProcessInfo RunProcessInfo = processes.get(runProcessInfoId);

            if(RunProcessInfo != null) {
                markRunStopped(runProcessInfoId);
                RunProcessInfo.getProcess().destroyForcibly();
                processes.remove(runProcessInfoId);
            }
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public void sendOutputToClient(String runProcessInfoId,WebSocketSession session) {
        BlockingQueue<String> outputQueue = processOutputQueue.get(runProcessInfoId);

        if(outputQueue == null) {
            return;
        }

        sendExistingOutput(session,outputQueue);

        new Thread(() -> sendOutput(session,outputQueue)).start();
    }

    private void sendExistingOutput(WebSocketSession session, BlockingQueue<String> outputQueue) {
        for(String line : outputQueue) {
            try {
                session.sendMessage(new TextMessage(line));
            } catch (Exception e) {
                System.out.println("Error sending message: " + e.getMessage());
            }
        }
    }

    private void sendOutput(WebSocketSession session, BlockingQueue<String> outputQueue) {
        try {
            while (true) {
                String line = outputQueue.take();
                if(session.isOpen()) {
                    session.sendMessage(new TextMessage(line));
                } else {
                    System.out.println("Session is closed so stopping sending output");
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Error sending output: " + e.getMessage());
        }
    }

    private void markRunCompleted(String runProcessInfoId) throws IOException {
        RunProcessInfo runProcessInfo = this.processes.get(runProcessInfoId);
        if(runProcessInfo == null) {
            return;
        }
        if(runProcessInfo.getKind().equalsIgnoreCase("hydro")) {
            hydroRunService.markRunCompleted(runProcessInfo.getRunId());
        } else if(runProcessInfo.getKind().equalsIgnoreCase("mhd")) {
            mhdRunService.markRunCompleted(runProcessInfo.getRunId());
        }

    }


    private void markRunStopped(String runProcessInfoId) {
        RunProcessInfo runProcessInfo = this.processes.get(runProcessInfoId);
        if(runProcessInfo == null) {return;}
        if(runProcessInfo.getKind().equalsIgnoreCase("hydro")) {
            hydroRunService.markRunStopped(runProcessInfo.getRunId());
        } else if(runProcessInfo.getKind().equalsIgnoreCase("mhd")) {
            mhdRunService.markRunStopped(runProcessInfo.getRunId());
        }
    }

    public List<RunProcessInfo> getAllRunProcessInfoForUsername(String username) {
        return processes.values().stream().filter(runProcessInfo -> runProcessInfo.getUsername().equalsIgnoreCase(username)).toList();
    }

    private String createRunProcessId(String username, String timeOfRun, String sessionId) {
        return username + "_" + timeOfRun + "_" + sessionId;
    }

}
