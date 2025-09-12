package com.example.spring_example.controller;

import com.example.spring_example.models.HydroPara;
import com.example.spring_example.service.HydroRunService;
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

    @Autowired
    HydroRunService hydroRunService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("WebSocket connection established");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        String payload = message.getPayload();
        System.out.println(payload);
        ObjectMapper mapper = new ObjectMapper();

        if("stop".equals(payload.trim())){
            System.out.println("Stop message received.");
            stopRun(session);
        } else {
            try {

                Map<String,Object> payloadObject = mapper.readValue(payload,new TypeReference<Map<String,Object>>() {});
                System.out.println("Payload object is: " + payloadObject.toString());
                HydroPara hydroPara = new HydroPara();
                createParaFile(payloadObject,session,hydroPara);
                ProcessBuilder builder = new ProcessBuilder("C:\\Users\\kabha\\OneDrive\\Desktop\\Programming\\Vayusoft_Labs\\TurbulenceHUB\\backend\\Tarang\\tarang.exe");
                builder.redirectErrorStream(true);
                currentProcess = builder.start();
                running = true;
                hydroRunService.createNewRun(hydroPara);
                new Thread(() -> {
                    try(BufferedReader reader = new BufferedReader(new InputStreamReader(currentProcess.getInputStream()))) {
                        String line;
                        while( running && (line = reader.readLine()) != null && session.isOpen()) {
                            session.sendMessage(new TextMessage(line));
                        }
                    } catch (IOException e) {
                        try {
                            session.sendMessage(new TextMessage("Error sending process output: " + e.getMessage()));
                        } catch (IOException ignored) {}
                    }
                }).start();


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


    public void createParaFile(Map<String,Object> payloadObject,WebSocketSession session,HydroPara hydroPara) {

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


        System.out.println("Trying to create para file");
        try {
            hydroPara.createParaFile(session.getId());
        } catch (Exception e) {
            System.out.print("Error creating para file: " + e.getMessage());
        }
    }
}


