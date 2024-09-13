package com.bcn.bmc.services;

import com.bcn.bmc.models.*;
import com.bcn.bmc.repositories.DonationRepositories;
import com.bcn.bmc.repositories.DonorRepository;
import com.bcn.bmc.repositories.OrganizationRepository;
import com.bcn.bmc.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DonationService {

    private final CommonService commonService;
    private final DonationRepositories donationRepository;

    @Autowired
    public DonationService(CommonService commonService, DonationRepositories donationRepository) {
        this.commonService = commonService;
        this.donationRepository = donationRepository;
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

            Optional<Donation> lastDonation = donationRepository.findLastDonationByDonor(donation.getDonor());

            if (lastDonation.isPresent()) {
                Calendar threeMonthsAgo = Calendar.getInstance();
                threeMonthsAgo.add(Calendar.MONTH, -3);

                if (lastDonation.get().getDonationDate().after(threeMonthsAgo.getTime())) {
                    return new DonationResponse("Failure", "Donor cannot donate again within 3 months of the last donation.");
                }
            }

            donation.setCreatedBy(userAuthorize.getUserId());

            Donation newDonation = donationRepository.save(donation);


            if (newDonation.getId() > 0) {
                return new DonationResponse("Success", "Donor registered successfully.");
            } else {
                return new DonationResponse("Failure", "Donor registration failed.");
            }

        } catch (Exception e) {
            throw new RuntimeException("Error creating donation: " + e.getMessage(), e);
        }
    }



    public List<DonationDetails> getAllDonations(UserAuthorize userAuthorize) {
        try {
            List<Donation> donations = userAuthorize.getOrganization() == 1
                    ? donationRepository.findAllDonations()
                    : donationRepository.findAllDonationsByOrg(userAuthorize.getOrganization());


            List<DonationDetails> donationDetails = new ArrayList<>();

            for (Donation donation : donations) {
                KeyValue donatedDonor = commonService.fetchDonorKeyValue(donation.getDonor());

                KeyValue donatedOrganization = commonService.fetchOrganizationKeyValue(donation.getOrganizationId());

                KeyValue donatedHandledUser = commonService.fetchUserKeyValue(donation.getCreatedBy());

                donationDetails.add(new DonationDetails(
                        donation.getId(),
                        donatedDonor,
                        donatedOrganization,
                        donation.getQuantity(),
                        donatedHandledUser,
                        donation.getBloodType(),
                        donation.getDonationDate()
                ));
            }

            return donationDetails;

        } catch (Exception e) {
            throw new RuntimeException("Error fetching donation details: " + e.getMessage(), e);
        }
    }







}
