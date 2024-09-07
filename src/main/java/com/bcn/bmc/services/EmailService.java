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



    public void sendVerificationEmailToNewlyCreatedUser(String to, String firstName, String lastName, String subject, String code, String userName, String psw) {
        if (emailSender == null) {
            System.out.println("emailSender is null when sending email");
            return;
        }
        System.out.println("sendVerificationEmail to " + to);
        System.out.println("sendVerificationEmail subject " + subject);
        System.out.println("sendVerificationEmail text " + code);
        System.out.println("sendVerificationEmail psw " + psw);

        String mes = "<html><body>"
                + "<p>HI " + firstName + ' ' + lastName + "!,</p>"
                + "<p>Your account for the BloodCenter Network has been created successfully.</p>"
                + "<p>Your username is: <strong>" + userName + "</strong></p>"
                + "<p>Your one-time password is: <strong>" + psw + "</strong></p>"
                + "<p>Your verification code is: <strong>" + code + ". verification code will expired within 24hrs.in case it was expired you can still request a verification code through login form</strong></p>"
                + "<p>Please log in using these credentials. You will be prompted to enter the verification code.</p>"
                + "<p>Once you enter the code, you will be able to change your password.</p>"
                + "<p>Thank you!</p>"
                + "<p>Best regards,<br/>BloodCenter Network</p>"
                + "</body></html>";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(mes);
        emailSender.send(message);
    }
}