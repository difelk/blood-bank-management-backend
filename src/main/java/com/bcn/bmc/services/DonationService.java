package com.bcn.bmc.services;

import com.bcn.bmc.models.*;
import com.bcn.bmc.repositories.DonationRepositories;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class DonationService {

    private final  DonationRepositories donationRepositories;

    public DonationService(DonationRepositories donationRepositories){
        this.donationRepositories = donationRepositories;
    }

    public DonationResponse createDonation(UserAuthorize userAuthorize, Donation donation) {
        try {
            donation.setDonationDate(new Date());

            donation.setCreatedBy(userAuthorize.getUserId());
            Donation newDonation = donationRepositories.save(donation);

            if (newDonation.getId() > 0) {
                return new DonationResponse("Success", "Donor registered successfully.");
            } else {
                return new DonationResponse("Failure", "Donor registration failed.");
            }

        } catch (Exception e) {
            throw new RuntimeException("Error creating donor: " + e.getMessage(), e);
        }
    }

}
