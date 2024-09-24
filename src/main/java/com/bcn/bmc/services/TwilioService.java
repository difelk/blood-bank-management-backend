package com.bcn.bmc.services;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TwilioService {

    @Value("${twilio.phone.number}")
    private String twilioNumber;

    /**
     * Sends an SMS message.
     *
     * @param to      Recipient's phone number in E.164 format (e.g., +1234567890)
     * @param message The message content
     */
    public void sendSMS(String to, String message) {
        Message.creator(
                new PhoneNumber(to),
                new PhoneNumber(twilioNumber),
                message
        ).create();
    }
}
