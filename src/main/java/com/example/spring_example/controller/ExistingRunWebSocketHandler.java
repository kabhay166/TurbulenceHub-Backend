package com.example.spring_example.controller;

import com.example.spring_example.entity.AppUser;
import com.example.spring_example.security.JwtUtil;
import com.example.spring_example.service.ProcessManager;
import com.example.spring_example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;


@Component
public class ExistingRunWebSocketHandler extends TextWebSocketHandler {

    private volatile boolean running = false;
    private Map<String,Object> payloadObject;
    private WebSocketSession currentSession;


    @Autowired
    ProcessManager processManager;

    @Autowired
    UserService userService;

    @Autowired
    JwtUtil jwtUtil;

    String jwtToken = "";
    private boolean sessionAuthenticated = false;

    String processInfoId = "";

    String currentUserName = "";


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("WebSocket connection established for sending existing output");
        jwtToken = "";
        currentSession = session;

    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        System.out.println(payload);

        if("stop".equals(payload.trim()))
        {
            stopRun(currentSession);
        }
        else if("RunId:".equals(payload.trim().substring(0,6))) {
            System.out.println("Run Id received.");
            processInfoId = payload.trim().substring(6);
            if(processInfoId.equals("null") || processInfoId.isEmpty()) {
                currentSession.sendMessage(new TextMessage("The run id is invalid"));
                currentSession.close();
            }

            processManager.sendOutputToClient(processInfoId,currentSession);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        currentSession.close();
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

}


