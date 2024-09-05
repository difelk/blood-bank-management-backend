package com.bcn.bmc.services;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @PostConstruct
    public void init() {
        if (emailSender == null) {
            System.out.println("emailSender is null");
        } else {
            System.out.println("emailSender is initialized");
        }
    }

    public void sendVerificationEmail(String to, String subject, String text) {
        if (emailSender == null) {
            System.out.println("emailSender is null when sending email");
            return;
        }
        System.out.println("sendVerificationEmail to " + to);
        System.out.println("sendVerificationEmail subject " + subject);
        System.out.println("sendVerificationEmail text " + text);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }
}