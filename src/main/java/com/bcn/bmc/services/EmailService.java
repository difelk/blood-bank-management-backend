package com.bcn.bmc.services;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import jakarta.mail.MessagingException;

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
        System.out.println("sendVerificationEmail code " + code);
        System.out.println("sendVerificationEmail psw " + psw);

        String mes = "<html><body>"
                + "<p>Hi " + firstName + " " + lastName + "!</p>"
                + "<p>Your account for the BloodCenter Network has been created successfully.</p>"
                + "<p>Your username is: <strong>" + userName + "</strong></p>"
                + "<p>Your one-time password is: <strong>" + psw + "</strong></p>"
                + "<p>Your verification code is: <strong>" + code + "</strong>. The verification code will expire within 24 hours. In case it expires, you can request a new code through the login form.</p>"
                + "<p>Please log in using these credentials. You will be prompted to enter the verification code.</p>"
                + "<p>Once you enter the code, you will be able to change your password.</p>"
                + "<p>Thank you!</p>"
                + "<p>Best regards,<br/>BloodCenter Network</p>"
                + "</body></html>";

        try {
            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(mes, true);
            emailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}