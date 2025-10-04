package com.example.spring_example.controller;

import com.example.spring_example.config.SimulationConfig;
import com.example.spring_example.models.HydroPara;
import com.example.spring_example.models.MhdPara;
import com.example.spring_example.security.JwtUtil;
import com.example.spring_example.service.run.HydroRunService;
import com.example.spring_example.service.run.MhdRunService;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;


@Component
public class ProcessWebSocketHandler extends TextWebSocketHandler {

    private Process currentProcess;
    private volatile boolean running = false;
    private Map<String,Object> payloadObject;
    private WebSocketSession currentSession;
    private String kind;
    @Autowired
    HydroRunService hydroRunService;
    @Autowired
    MhdRunService mhdRunService;

    @Autowired
    JwtUtil jwtUtil;

    String jwtToken = "";
    private boolean sessionAuthenticated = false;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("WebSocket connection established");
        jwtToken = "";
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        currentSession = session;
        String payload = message.getPayload();
        System.out.println(payload);
        ObjectMapper mapper = new ObjectMapper();

        if("Token:".equals(payload.trim().substring(0,6))) {
            System.out.println("Token received.");
            jwtToken = payload.trim().substring(6);
            if(jwtToken.equals("null") || jwtToken.isEmpty()) {
                currentSession.sendMessage(new TextMessage("The token is invalid"));
                currentSession.close();
            }
            handleRunWithUser();
        }
        else if("stop".equals(payload.trim())){
            stopRun(currentSession);
        } else {
            if(!sessionAuthenticated) {
                currentSession.sendMessage(new TextMessage("You are not logged in. Please log in to continue."));
            }
            try {

                payloadObject = mapper.readValue(payload,new TypeReference<Map<String,Object>>() {});
                System.out.println("Payload object is: " + payloadObject.toString());
                kind = (String) payloadObject.get("kind");

                handleRun();

            } catch (Exception e) {
                session.sendMessage(new TextMessage("Invalid input format: " + e.getMessage()));
            }

        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("Websocket connection closed.");
        running = false;
        if(currentProcess != null && currentProcess.isAlive()) {
            currentProcess.destroy();
        }
    }

    public void stopRun(WebSocketSession session) throws InterruptedException, IOException {
        session.sendMessage(new TextMessage("Process has been stopped"));
        session.close();
        if(currentProcess != null && currentProcess.isAlive()) {
            currentProcess.destroy();
            if (!currentProcess.waitFor(1, java.util.concurrent.TimeUnit.SECONDS)) {
                currentProcess.destroyForcibly();
            }

            running = false;

            session.sendMessage(new TextMessage("The process has been killed."));
            System.out.print("Process stopped by the client");

        }
    }

    public void handleRun() throws IOException {
        System.out.println("Inside handle run");
        try {

            if(kind.equals("HYDRO")) {
                System.out.println("Creating hydro para file");
                createHydroParaFile();
            } else if(kind.equals("MHD")) {
                createMhdParaFile();
            }

            ProcessBuilder builder = new ProcessBuilder(
                    SimulationConfig.getPythonPath(),
                    SimulationConfig.getScriptPath()
            );
            builder.redirectErrorStream(true);
            currentProcess = builder.start();
            running = true;

            new Thread(() -> {
                try(BufferedReader reader = new BufferedReader(new InputStreamReader(currentProcess.getInputStream()))) {
                    String line;
                    while( running && (line = reader.readLine()) != null && currentSession.isOpen()) {
                        currentSession.sendMessage(new TextMessage(line));
                    }
                } catch (IOException e) {
                    try {
                        currentSession.sendMessage(new TextMessage("Error sending process output: " + e.getMessage()));
                    } catch (IOException ignored) {}
                }
            }).start();


        } catch (Exception e) {
            currentSession.sendMessage(new TextMessage("Invalid input format: " + e.getMessage()));
        }
    }

    public void createHydroParaFile() {


        System.out.println("Trying to create para file");
        try {
            HydroPara hydroPara = new HydroPara();
            hydroPara.setDevice( (String) payloadObject.get("device"));
            hydroPara.setDevice_rank(Integer.parseInt(payloadObject.get("device_rank").toString()));
            hydroPara.setDimension(Integer.parseInt(payloadObject.get("dimension").toString()));
            hydroPara.setNx(Integer.parseInt(payloadObject.get("Nx").toString()));
            hydroPara.setNy(Integer.parseInt(payloadObject.get("Ny").toString()));
            hydroPara.setNz(Integer.parseInt(payloadObject.get("Nz").toString()));
            hydroPara.setNu(Double.parseDouble(payloadObject.get("nu").toString()));
            hydroPara.setEta(Double.parseDouble(payloadObject.get("eta").toString()));
            hydroPara.setTime_scheme((String) payloadObject.get("time_scheme"));
            hydroPara.setT_initial(Double.parseDouble(payloadObject.get("t_initial").toString()));
            hydroPara.setT_final(Double.parseDouble(payloadObject.get("t_final").toString()));
            hydroPara.setDt(Double.parseDouble(payloadObject.get("dt").toString()));
            hydroRunService.createNewRun(hydroPara);
            hydroPara.createParaFile(currentSession.getId());
        } catch (Exception e) {
            System.out.print("Error creating para file: " + e.getMessage());
        }
    }


    public void createMhdParaFile() {
        MhdPara mhdPara = new MhdPara();
        mhdPara.setDevice( (String) payloadObject.get("device"));
        mhdPara.setDevice_rank(Integer.parseInt(payloadObject.get("device_rank").toString()));
        mhdPara.setDimension(Integer.parseInt(payloadObject.get("dimension").toString()));
        mhdPara.setNx(Integer.parseInt(payloadObject.get("Nx").toString()));
        mhdPara.setNy(Integer.parseInt(payloadObject.get("Ny").toString()));
        mhdPara.setNz(Integer.parseInt(payloadObject.get("Nz").toString()));
        mhdPara.setNu(Double.parseDouble(payloadObject.get("nu").toString()));
        mhdPara.setEta(Double.parseDouble(payloadObject.get("eta").toString()));
        mhdPara.setTime_scheme((String) payloadObject.get("time_scheme"));
        mhdPara.setT_initial(Double.parseDouble(payloadObject.get("t_initial").toString()));
        mhdPara.setT_final(Double.parseDouble(payloadObject.get("t_final").toString()));
        mhdPara.setDt(Double.parseDouble(payloadObject.get("dt").toString()));
        mhdRunService.createNewRun(mhdPara);

        System.out.println("Trying to create para file");
        try {
            mhdPara.createParaFile(currentSession.getId());
        } catch (Exception e) {
            System.out.print("Error creating para file: " + e.getMessage());
        }
    }

    public void handleRunWithUser() throws IOException {

        try {
            if(!jwtUtil.validateToken(jwtToken)) {
                currentSession.sendMessage(new TextMessage("You are not logged in. Please log in to continue."));
                currentSession.close();
            }

            sessionAuthenticated = true;

            String username = jwtUtil.extractUsername(jwtToken);
            System.out.println();
            currentSession.sendMessage( new TextMessage("Username is: " + username));

        } catch(Exception e) {
            currentSession.sendMessage(new TextMessage("You are not logged in. Please log in to continue."));
            currentSession.close();
        }

    }
}


