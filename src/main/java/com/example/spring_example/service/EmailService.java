package com.example.spring_example.service;

import com.example.spring_example.config.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void sendVerificationEmail(String to, String token) {
        String subject = "Verify your email";
        String verificationUrl = AppConfig.getVerificationUrl() + token;
        String message = "Click the link below to verify your email:\n" + verificationUrl;
        SimpleMailMessage verificationMail = new SimpleMailMessage();
        verificationMail.setFrom(AppConfig.getCompanyEmail());
        verificationMail.setTo(to);
        verificationMail.setSubject(subject);
        verificationMail.setText(message);

        mailSender.send(verificationMail);
    }

    @Async
    public void sendOTPMail(String to,String otp) {
        String subject = "Your OTP for login to turbulencehub";
        String message = otp + " is your otp for login to turbulencehub. It is only valid for 10 minutes. Please do not share it with anyone.";
        SimpleMailMessage otpMail = new SimpleMailMessage();
        otpMail.setFrom(AppConfig.getCompanyEmail());
        otpMail.setTo(to);
        otpMail.setSubject(subject);
        otpMail.setText(message);
        mailSender.send(otpMail);
    }

    @Async
    public void sendPasswordResetEmail(String to, String token) {
        String subject = "Password reset for turbulencehub";
        String passwordResetUrl = AppConfig.getPasswordResetUrl() + token;
        String message = "Click on the below link to reset your password:\n" + passwordResetUrl;
        SimpleMailMessage passwordRestMail = new SimpleMailMessage();
        passwordRestMail.setFrom(AppConfig.getCompanyEmail());
        passwordRestMail.setTo(to);
        passwordRestMail.setSubject(subject);
        passwordRestMail.setText(message);
        mailSender.send(passwordRestMail);
    }


    @Async
    public void sendRunCompletedEmail(String username, String to, String kind, String dimension, String resolution,String timeOfRun) {
        String subject = "Your run has completed";
        String date = Arrays.stream(timeOfRun.split("_")).limit(3).collect(
                Collectors.collectingAndThen(Collectors.toList(),list -> {
                    Collections.reverse(list);
                    return String.join("-",list);
                }));
        String time = Arrays.stream(timeOfRun.split("_")).skip(3).collect(Collectors.joining(":"));
        String message = "Hi, " + username + ".\n"
                + "Your " + kind + " run has successfully finished. Below are run details for your reference.\n" +
                "Kind: " + kind + "\n" +
                "Dimension: " + dimension + "\n" +
                "Resolution: " + resolution + "\n" +
                "Time of run: " + time + "\n" +
                "Date of run: " + date + "\n";
        SimpleMailMessage runCompletedMail = new SimpleMailMessage();
        runCompletedMail.setFrom(AppConfig.getCompanyEmail());
        runCompletedMail.setTo(to);
        runCompletedMail.setSubject(subject);
        runCompletedMail.setText(message);
        mailSender.send(runCompletedMail);
    }
}
