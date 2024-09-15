package com.bcn.bmc.services;

import com.bcn.bmc.models.*;
import com.bcn.bmc.repositories.DonationRepositories;
import com.bcn.bmc.repositories.DonorRepository;
import com.bcn.bmc.repositories.OrganizationRepository;
import com.bcn.bmc.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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

//            Date newDonationDate = donation.getDonationDate();

            LocalDate newDonationDate = donation.getDonationDate().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate();

            List<Donation> donorDonations = donationRepository.findByDonor(donation.getDonor());
            if (!donorDonations.isEmpty()) {
                // Check for donations within 3 months of the new donation date (past and future)
                LocalDate threeMonthsAgo = newDonationDate.minus(3, ChronoUnit.MONTHS);
                LocalDate threeMonthsAfter = newDonationDate.plus(3, ChronoUnit.MONTHS);

                boolean hasInvalidDonation = donorDonations.stream()
                        .anyMatch(d -> {
                            LocalDate donationDate = d.getDonationDate().toInstant()
                                    .atZone(ZoneId.systemDefault()).toLocalDate();
                            return !donationDate.isBefore(threeMonthsAgo) && !donationDate.isAfter(threeMonthsAfter);
                        });

                if (hasInvalidDonation) {
                    return new DonationResponse("Failure",
                            "Donor is not eligible for a new donation. There is another donation within 3 months of the specified date.");
                }
            }

            // Check if the new donation date is in the future
            if (newDonationDate.isAfter(LocalDate.now())) {
                return new DonationResponse("Failure", "Donation date cannot be in the future.");
            }

            donation.setCreatedBy(userAuthorize.getUserId());
            donation.setOrganizationId((long) userAuthorize.getOrganization());

            Donation newDonation = donationRepository.save(donation);

            if (newDonation.getId() > 0) {
                try {

                    Stock stock = stockService.addStock(
                            donation.getOrganizationId(),
                            donation.getBloodType(),
                            donation.getQuantity()
                    );
                    return new DonationResponse("Success", "Donation added successfully.");
                } catch (Exception stockUpdateException) {
                    return new DonationResponse("Partial Success", "Donation added successfully, but failed to update stock. Error: " + stockUpdateException.getMessage());
                }
            } else {
                return new DonationResponse("Failure", "Donation added failed.");
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
            if (!existingDonationOpt.isPresent()) {
                return new DonationResponse("Failure", "Donation not found.");
            }

            Donation existingDonation = existingDonationOpt.get();


            if (!existingDonation.getDonationDate().equals(donation.getDonationDate())) {

                List<Donation> donorDonations = donationRepository.findByDonor(existingDonation.getDonor());

                for (Donation d : donorDonations) {

                    if (!d.getId().equals(donation.getId())) {

                        Calendar calendarBefore = Calendar.getInstance();
                        calendarBefore.setTime(d.getDonationDate());
                        calendarBefore.add(Calendar.MONTH, -3);

                        Calendar calendarAfter = Calendar.getInstance();
                        calendarAfter.setTime(d.getDonationDate());
                        calendarAfter.add(Calendar.MONTH, 3);


                        if (donation.getDonationDate().after(calendarBefore.getTime())
                                && donation.getDonationDate().before(calendarAfter.getTime())) {
                            return new DonationResponse("Failure", "Cannot update donation: A donor can only donate once every 3 months.");
                        }
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
