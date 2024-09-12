package com.bcn.bmc.services;

import com.bcn.bmc.models.*;
import com.bcn.bmc.repositories.DonationRepositories;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
public class DonationService {

    private final  DonationRepositories donationRepositories;

    public DonationService(DonationRepositories donationRepositories){
        this.donationRepositories = donationRepositories;
    }

    public DonationResponse createDonation(UserAuthorize userAuthorize, Donation donation) {
        try {
            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);

            if (donation.getDonationDate() == null) {
                donation.setDonationDate(new Date());
            }

            Calendar donationDate = Calendar.getInstance();
            donationDate.setTime(donation.getDonationDate());

            if (donationDate.after(today)) {
                return new DonationResponse("Failure", "Donor cannot donate for a future date.");
            }

            Optional<Donation> lastDonation = donationRepositories.findLastDonationByDonor(donation.getDonor());

            if (lastDonation.isPresent()) {
                Calendar threeMonthsAgo = Calendar.getInstance();
                threeMonthsAgo.add(Calendar.MONTH, -3);

                if (lastDonation.get().getDonationDate().after(threeMonthsAgo.getTime())) {
                    return new DonationResponse("Failure", "Donor cannot donate again within 3 months of the last donation.");
                }
            }

            donation.setCreatedBy(userAuthorize.getUserId());

            Donation newDonation = donationRepositories.save(donation);


            if (newDonation.getId() > 0) {
                return new DonationResponse("Success", "Donor registered successfully.");
            } else {
                return new DonationResponse("Failure", "Donor registration failed.");
            }

        } catch (Exception e) {
            throw new RuntimeException("Error creating donation: " + e.getMessage(), e);
        }
    }



}
