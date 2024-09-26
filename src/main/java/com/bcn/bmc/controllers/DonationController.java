package com.bcn.bmc.controllers;

import com.bcn.bmc.helper.TokenData;
import com.bcn.bmc.models.*;
import com.bcn.bmc.services.DonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


    @GetMapping("/")
    public List<DonationDetails> getAllDonations(@RequestHeader("Authorization") String tokenHeader) {
        UserAuthorize userAuthorize = tokenHelper.parseToken(tokenHeader);
        System.out.println("getAllDonations callled");
        return donationService.getAllDonations(userAuthorize);
    }


    @GetMapping("/donor/{donor}")
    public List<DonationDetails> getAllDonationsByDonorId(@RequestHeader("Authorization") String tokenHeader, @PathVariable Long donor) {
        UserAuthorize userAuthorize = tokenHelper.parseToken(tokenHeader);
        return donationService.getAllDonationsByDonorId(userAuthorize, donor);
    }

    @PutMapping("/update")
    public ResponseEntity<DonationResponse> updateDonation(@RequestHeader("Authorization") String tokenHeader, @RequestBody Donation donation) {
        UserAuthorize userAuthorize = tokenHelper.parseToken(tokenHeader);
        try {
            DonationResponse donationResponse = donationService.updateDonation(userAuthorize, donation);
            return new ResponseEntity<>(donationResponse, HttpStatus.OK);
        } catch (Exception e) {
            DonationResponse response = new DonationResponse("FAILURE", "Failed to update donation: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{donationId}")
    public ResponseEntity<DonationResponse> deleteDonation(@PathVariable Long donationId) {
        try {
            DonationResponse donationResponse = donationService.deleteDonation(donationId);
            return new ResponseEntity<>(donationResponse, HttpStatus.OK);
        } catch (Exception e) {
            DonationResponse response = new DonationResponse("FAILURE", "Failed to delete donation: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

}
