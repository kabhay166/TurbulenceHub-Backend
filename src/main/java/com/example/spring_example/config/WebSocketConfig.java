package com.example.spring_example.config;

import com.example.spring_example.controller.ExistingRunWebSocketHandler;
import com.example.spring_example.controller.LaunchRunWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;


@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    final private ApplicationContext applicationContext;

    public WebSocketConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

        registry.addHandler(  applicationContext.getBean(LaunchRunWebSocketHandler.class), "/ws/tarang-demo")
                .setAllowedOrigins("*");
        registry.addHandler(applicationContext.getBean(ExistingRunWebSocketHandler.class),"/ws/get-run-output")
                .setAllowedOrigins("*");
    }
}