package com.bcn.bmc.controllers;

import com.bcn.bmc.helper.TokenData;
import com.bcn.bmc.models.Donation;
import com.bcn.bmc.models.DonationResponse;
import com.bcn.bmc.models.UserAuthorize;
import com.bcn.bmc.services.DonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/donations")
public class DonationController {

    @Autowired
    private DonationService donationService;

    @Autowired
    private TokenData tokenHelper;

    @PostMapping("/create")
    public ResponseEntity<DonationResponse> createDonation(@RequestHeader("Authorization") String tokenHeader, @RequestBody Donation donation) {
        UserAuthorize userAuthorize =  tokenHelper.parseToken(tokenHeader);
        try {
            DonationResponse donationResponse = donationService.createDonation(userAuthorize, donation);
            return new ResponseEntity<>(donationResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            DonationResponse response = new DonationResponse("FAILURE", "Failed to create donation: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
