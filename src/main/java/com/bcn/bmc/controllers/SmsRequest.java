package com.bcn.bmc.controllers;


import com.bcn.bmc.services.TwilioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/sms")
public class SmsRequest {
    @Autowired
    private TwilioService twilioService;

    @PostMapping("/send")
    public ResponseEntity<String> sendSms(@RequestBody com.bcn.bmc.models.SmsRequest smsRequest) {
        try {
            twilioService.sendSMS(smsRequest.getTo(), smsRequest.getMessage());
            return ResponseEntity.ok("SMS sent successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to send SMS: " + e.getMessage());
        }
    }
}
