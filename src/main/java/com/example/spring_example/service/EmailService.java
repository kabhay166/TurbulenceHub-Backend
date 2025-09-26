package com.example.spring_example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(String to, String token) {
        String subject = "Verify your email";
        String verificationUrl = "http://localhost:8080/user/signup/verify?token=" + token;
        String message = "Click the link below to verify your email:\n" + verificationUrl;
        SimpleMailMessage verificationMail = new SimpleMailMessage();
        verificationMail.setFrom("vayusoftlabs@gmail.com");
        verificationMail.setTo(to);
        verificationMail.setSubject(subject);
        verificationMail.setText(message);

        mailSender.send(verificationMail);
    }
}
