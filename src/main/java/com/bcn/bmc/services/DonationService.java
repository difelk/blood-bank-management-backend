package com.bcn.bmc.services;

import com.bcn.bmc.models.*;
import com.bcn.bmc.repositories.DonationRepositories;
import com.bcn.bmc.repositories.DonorRepository;
import com.bcn.bmc.repositories.OrganizationRepository;
import com.bcn.bmc.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DonationService {

    private final CommonService commonService;
    private final DonationRepositories donationRepository;
    private final StockService stockService;

    @Autowired
    public DonationService(CommonService commonService, DonationRepositories donationRepository, StockService stockService) {
        this.commonService = commonService;
        this.donationRepository = donationRepository;
        this.stockService = stockService;
    }

    @Transactional
    public DonationResponse createDonation(UserAuthorize userAuthorize, Donation donation) {
        try {

            if (donation.getDonationDate() == null) {
                donation.setDonationDate(new Date());
            }
            donation.setDonationDate(new Date());

            Date donationDate = new Date();

            Optional<Donation> lastDonation = donationRepository.findLastDonationByDonor(donation.getDonor());
            if (lastDonation.isPresent()) {
                Calendar threeMonthsAgo = Calendar.getInstance();
                threeMonthsAgo.add(Calendar.MONTH, -3);

                if (lastDonation.get().getDonationDate().after(threeMonthsAgo.getTime())) {
                    return new DonationResponse("Failure", "Donor cannot donate again within 3 months of the last donation.");
                }
            }

            donation.setCreatedBy(userAuthorize.getUserId());

            donation.setOrganizationId((long) userAuthorize.getOrganization());
            Donation newDonation = donationRepository.save(donation);

            if (newDonation.getId() > 0) {
                try {
                    Stock stock =  stockService.addStock(
                            donation.getOrganizationId(),
                            donation.getBloodType(),
                            donation.getQuantity()
                    );
                    return new DonationResponse("Success", "Donor registered successfully.");
                } catch (Exception stockUpdateException) {

                    return new DonationResponse("Partial Success", "Donation registered successfully, but failed to update stock. Error: " + stockUpdateException.getMessage());
                }
            } else {
                return new DonationResponse("Failure", "Donor registration failed.");
            }

        } catch (Exception e) {
            throw new RuntimeException("Error creating donation: " + e.getMessage(), e);
        }
    }

    private List<DonationDetails> mapDonationsToDetails(List<Donation> donations) {
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
    }

    public List<DonationDetails> getAllDonations(UserAuthorize userAuthorize) {
        try {
            List<Donation> donations = userAuthorize.getOrganization() == 1
                    ? donationRepository.findAllDonations()
                    : donationRepository.findAllDonationsByOrg(userAuthorize.getOrganization());

            return mapDonationsToDetails(donations);

        } catch (Exception e) {
            throw new RuntimeException("Error fetching donation details: " + e.getMessage(), e);
        }
    }

    public List<DonationDetails> getAllDonationsForStock(long id) {
        try {
            List<Donation> donations = donationRepository.findAllDonationsByOrg(id);

            return mapDonationsToDetails(donations);

        } catch (Exception e) {
            throw new RuntimeException("Error fetching donation details: " + e.getMessage(), e);
        }
    }

    public List<DonationDetails> getAllDonationsByDonorId(UserAuthorize userAuthorize, long donor) {
        try {
            List<Donation> donations = userAuthorize.getOrganization() == 1
                    ? donationRepository.findDonationByDonorId(donor)
                    : donationRepository.findDonationByDonorIdWithinOrg(donor, userAuthorize.getOrganization());

            return mapDonationsToDetails(donations);

        } catch (Exception e) {
            throw new RuntimeException("Error fetching donation details: " + e.getMessage(), e);
        }
    }



    @Transactional
    public DonationResponse updateDonation(UserAuthorize userAuthorize, Donation donation) {
        try {
            Optional<Donation> existingDonationOpt = donationRepository.findById(donation.getId());
            if (existingDonationOpt.isEmpty()) {

                return new DonationResponse("Failure", "Donation not found.");
            }

            Donation existingDonation = existingDonationOpt.get();

            List<Donation> donorDonations = donationRepository.findByDonor(existingDonation.getDonor());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(donation.getDonationDate());
            calendar.add(Calendar.MONTH, 3);
            Date futureDateLimit = calendar.getTime();

            for (Donation d : donorDonations) {
                if (!d.getId().equals(donation.getId())) {
                    if (d.getDonationDate().before(futureDateLimit)) {
                        return new DonationResponse("Failure", "Cannot update donation within 3 months of a previous donation.");
                    }
                }
            }

            boolean quantityChanged = !existingDonation.getQuantity().equals(donation.getQuantity());
            boolean bloodTypeChanged = !existingDonation.getBloodType().equals(donation.getBloodType());
            double oldQty = existingDonation.getQuantity();
            long organization =existingDonation.getOrganizationId();


                    System.out.println("is qty change ? " + quantityChanged);
            System.out.println("is blood type  change ? " + bloodTypeChanged);
            System.out.println("organization ? " + organization);

            existingDonation.setDonationDate(donation.getDonationDate());
            existingDonation.setQuantity(donation.getQuantity());
            existingDonation.setBloodType(donation.getBloodType());

            donationRepository.save(existingDonation);

            if (quantityChanged || bloodTypeChanged) {
                try {
                    double quantityDifference = donation.getQuantity() - oldQty;

                    System.out.println("oldQty: " + oldQty);
                    System.out.println("donation.getQuantity(): " + donation.getQuantity());
                    System.out.println("qty differences: " + quantityDifference);

                    if (quantityDifference != 0) {
                        if (bloodTypeChanged) {
                            stockService.updateStock(existingDonation.getOrganizationId(), existingDonation.getBloodType(), -oldQty);
                            stockService.updateStock(donation.getOrganizationId(), donation.getBloodType(), donation.getQuantity());
                        } else {
                            stockService.updateStock(organization, existingDonation.getBloodType(), quantityDifference);
                        }
                    }
                } catch (Exception stockUpdateException) {
                    return new DonationResponse("Partial Success", "Donation updated successfully, but failed to update stock. Error: " + stockUpdateException.getMessage());
                }
            }

            return new DonationResponse("Success", "Donation updated successfully.");

        } catch (Exception e) {
            throw new RuntimeException("Error updating donation: " + e.getMessage(), e);
        }
    }



    @Transactional
    public DonationResponse deleteDonation(long donationId) {
        try {
            Optional<Donation> donationOpt = donationRepository.findById(donationId);
            if (!donationOpt.isPresent()) {
                return new DonationResponse("Failure", "Donation not found.");
            }

            donationRepository.deleteById(donationId);

            return new DonationResponse("Success", "Donation deleted successfully.");

        } catch (Exception e) {
            throw new RuntimeException("Error deleting donation: " + e.getMessage(), e);
        }
    }




}
