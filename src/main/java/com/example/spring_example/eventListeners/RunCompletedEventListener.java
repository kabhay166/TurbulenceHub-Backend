package com.example.spring_example.eventListeners;

import com.example.spring_example.entity.AppUser;
import com.example.spring_example.entity.RunProcessInfo;
import com.example.spring_example.events.RunCompletedEvent;
import com.example.spring_example.service.EmailService;
import com.example.spring_example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RunCompletedEventListener {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Async
    @EventListener
    public void handleRunCompleted(RunCompletedEvent event) {
        Optional<AppUser> user = userService.findByUsername(event.getUsername());
        if(user.isEmpty()) {
            return;
        }

        String userEmail = user.get().getEmail();
        emailService.sendRunCompletedEmail(event.getUsername(), userEmail,event.getKind(),
                event.getDimension(), event.getResolution(), event.getTimeOfRun());

    }
}
