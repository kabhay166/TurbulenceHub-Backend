package com.example.spring_example.controller;

import com.example.spring_example.models.HydroPara;
import com.example.spring_example.models.MhdPara;
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

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("WebSocket connection established");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        currentSession = session;
        String payload = message.getPayload();
        System.out.println(payload);
        ObjectMapper mapper = new ObjectMapper();

        if("stop".equals(payload.trim())){
            stopRun(currentSession);
        } else {
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
        try {

            if(kind.equals("HYDRO")) {
                createHydroParaFile();
            } else if(kind.equals("MHD")) {
                createMhdParaFile();
            }

            ProcessBuilder builder = new ProcessBuilder("C:\\Users\\kabha\\OneDrive\\Desktop\\Programming\\Vayusoft_Labs\\Tarang CLI\\myvenv\\Scripts\\python.exe",
                    "C:\\Users\\kabha\\OneDrive\\Desktop\\Programming\\Vayusoft_Labs\\TurbulenceHUB\\backend\\Tarang\\tarang_cli.py");
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

        System.out.println("Trying to create para file");
        try {
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
}


