package com.example.spring_example.config;

import com.example.spring_example.controller.ExistingRunWebSocketHandler;
import com.example.spring_example.controller.WebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;


@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final WebSocketHandler webSocketHandler;
    private final ExistingRunWebSocketHandler existingRunWebSocketHandler;

    public WebSocketConfig(WebSocketHandler webSocketHandler, ExistingRunWebSocketHandler existingRunWebSocketHandler) {
        this.webSocketHandler = webSocketHandler;
        this.existingRunWebSocketHandler = existingRunWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler, "/ws/tarang-demo")
                .setAllowedOrigins("*");
        registry.addHandler(existingRunWebSocketHandler,"/ws/get-run-output")
                .setAllowedOrigins("*");
    }
}