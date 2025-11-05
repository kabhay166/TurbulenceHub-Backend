package com.example.spring_example.controller;

import com.example.spring_example.config.AppConfig;
import com.example.spring_example.entity.AppUser;
import com.example.spring_example.models.BasicPara;
import com.example.spring_example.models.HydroPara;
import com.example.spring_example.models.MhdPara;
import com.example.spring_example.security.JwtUtil;
import com.example.spring_example.service.ProcessManager;
import com.example.spring_example.service.UserService;
import com.example.spring_example.service.run.HydroRunService;
import com.example.spring_example.service.run.MhdRunService;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.*;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;



@Component
@Scope("prototype")
public class LaunchRunWebSocketHandler extends TextWebSocketHandler {

    private volatile boolean running = false;
    private Map<String,Object> payloadObject;
    private WebSocketSession currentSession;
    private String kind;
    @Autowired
    HydroRunService hydroRunService;
    @Autowired
    MhdRunService mhdRunService;

    @Autowired
    ProcessManager processManager;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    UserService userService;

    String jwtToken = "";
    private boolean sessionAuthenticated = false;

    String currentUserName;

    Long currentRunId = -1L;

    String timeOfRunPath = "";

    ZonedDateTime timeOfRun;

    String dimension = "";
    String resolution = "";

    String processInfoId = "";


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("WebSocket connection established");
        jwtToken = "";
        currentSession = session;

    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        System.out.println(payload);
        ObjectMapper mapper = new ObjectMapper();

        if("stop".equals(payload.trim()))
        {
            stopRun(currentSession);
        }
        else if("Token:".equals(payload.trim().substring(0,6))) {
            System.out.println("Token received.");
            jwtToken = payload.trim().substring(6);
            if(jwtToken.equals("null") || jwtToken.isEmpty()) {
                currentSession.sendMessage(new TextMessage("The token is invalid"));
                currentSession.close();
            }
            validateUser();
        } else {
            if(!sessionAuthenticated) {
                currentSession.sendMessage(new TextMessage("You are not logged in. Please log in to continue."));
                currentSession.close();
            }
            try {

                payloadObject = mapper.readValue(payload,new TypeReference<Map<String,Object>>() {});
                System.out.println("Payload object is: " + payloadObject.toString());
                kind = (String) payloadObject.get("kind");
                timeOfRun = ZonedDateTime.now();
                System.out.println("Time of run creation is: "  + timeOfRun);
                timeOfRunPath =  BasicPara.getTimeStamp(timeOfRun);
                System.out.println("Time of run path is: " + timeOfRunPath);
                handleRun();

            } catch (Exception e) {
                session.sendMessage(new TextMessage("Invalid input format: " + e.getMessage()));
            }

        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        currentSession.close();
        running = false;
    }

    public void stopRun(WebSocketSession session) throws InterruptedException, IOException {
        if(processManager.checkProcessCompletion(processInfoId)) {
            session.sendMessage(new TextMessage("Run has already completed"));
            return;
        }

        running = false;
        processManager.stopProcess(processInfoId);
        session.sendMessage(new TextMessage("Run has been stopped"));
        session.close();

    }

    public void handleRun() throws IOException {
        try {

            if(kind.equals("HYDRO")) {
                createHydroParaFile();
            } else if(kind.equals("MHD")) {
                createMhdParaFile();
            }


            processInfoId = processManager.startProcess(currentUserName, String.valueOf(timeOfRun), currentSession.getId(), kind,dimension,resolution,currentRunId);
            if(processInfoId == null) {
                currentSession.sendMessage(new TextMessage("An Unknown Error occured."));
            }
            processManager.sendOutputToClient(processInfoId,currentSession);

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
            hydroPara.setOutput_dir(Paths.get(AppConfig.getBaseOutputPath(),currentUserName,"Hydro Runs",timeOfRunPath).toString().replace("\\","/"));
            currentRunId = hydroRunService.createNewRun(hydroPara,currentUserName,timeOfRun,timeOfRunPath);
            if(currentRunId == -1) {
                currentSession.sendMessage(new TextMessage("An error occurred."));
                currentSession.close();
            }

            dimension = String.valueOf(hydroPara.getDimension());
            resolution = hydroPara.getNx() + "x" + hydroPara.getNy() + "x" + hydroPara.getNz();

            hydroPara.createParaFile(currentSession.getId());
        } catch (Exception e) {
            System.out.print("Error creating para file: " + e.getMessage());
        }
    }


    public void createMhdParaFile() throws IOException {
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
        mhdPara.setOutput_dir(Paths.get(AppConfig.getBaseOutputPath(),currentUserName,"MHD Runs",timeOfRunPath).toString().replace("\\","/"));

        currentRunId =  mhdRunService.createNewRun(mhdPara,currentUserName,timeOfRun,timeOfRunPath);
        if(currentRunId == -1) {
            currentSession.sendMessage(new TextMessage("An error occurred."));
            currentSession.close();
        }

        dimension = String.valueOf(mhdPara.getDimension());
        resolution = mhdPara.getNx() + "x" + mhdPara.getNy() + "x" + mhdPara.getNz();
        System.out.println("Trying to create para file");
        try {
            mhdPara.createParaFile(currentSession.getId());
        } catch (Exception e) {
            System.out.print("Error creating para file: " + e.getMessage());
        }
    }

    public void validateUser() throws IOException {

        try {
            if(!jwtUtil.validateToken(jwtToken)) {
                currentSession.sendMessage(new TextMessage("You are not logged in. Please log in to continue."));
                currentSession.close();
            }

            sessionAuthenticated = true;

            currentUserName = jwtUtil.extractUsername(jwtToken);
            Optional<AppUser> user = userService.findByUsername(currentUserName);
            if(user.isEmpty()) {
                currentSession.sendMessage(new TextMessage("No user found with this username: " + currentUserName));
                currentSession.close();
            }

        } catch(Exception e) {
            currentSession.sendMessage(new TextMessage("You are not logged in. Please log in to continue."));
            currentSession.close();
        }

    }

}

