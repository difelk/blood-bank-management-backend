package com.bcn.bmc.services;

import com.bcn.bmc.models.*;
import com.bcn.bmc.repositories.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class DonorService {

    private final DonorRepository donorRepository;
    private final DonorAddressRepository donorAddressRepository;

    private final DonorDocumentRepository donorDocumentRepository;
    private final DonationRepositories donationRepositories;

    public DonorService(DonorRepository donorRepository, DonorAddressRepository donorAddressRepository, DonorDocumentRepository donorDocumentRepository, DonationRepositories donationRepositories) {
        this.donorRepository = donorRepository;
        this.donorAddressRepository = donorAddressRepository;
        this.donorDocumentRepository = donorDocumentRepository;
        this.donationRepositories = donationRepositories;
    }


    public DonorDetails getDonorByID(UserAuthorize userAuthorize, Long donorId) {
        try {
            Optional<Donor> optionalDonor = donorRepository.findById(donorId);

            if (optionalDonor.isEmpty()) {
                throw new RuntimeException("Donor not found for ID: " + donorId);
            }
            Donor donor = optionalDonor.get();
            DonorAddress donorAddress = null;
            List<DonorDocument> donorDocument = null;
            List<Donation> donorDonations = null;
            if (donor.getId() != null) {
                donorAddress = donorAddressRepository.findDonorAddressByUserId(donorId);
                donorDocument = donorDocumentRepository.findDonorDocumentsByUserId(donorId);
                donorDonations = donationRepositories.findDonationByDonorId(donorId);
            }
            return new DonorDetails(donor, donorAddress, donorDocument, donorDonations);

        } catch (Exception e) {
            throw new RuntimeException("Error creating donor: " + e.getMessage(), e);
        }
    }


    public List<DonorDetails> getAllDonorsDetails(UserAuthorize userAuthorize) {
        try {
            List<Donor> donors = donorRepository.findAll();
            List<DonorDetails> donorDetailsList = new ArrayList<>();

            for (Donor donor : donors) {
                DonorAddress donorAddress = null;
                List<DonorDocument> donorDocument = null;
                List<Donation> donorDonations = null;
                if (donor.getId() != null) {
                    donorAddress = donorAddressRepository.findDonorAddressByUserId(donor.getId());
                    donorDocument = donorDocumentRepository.findDonorDocumentsByUserId(donor.getId());
                    donorDonations = donationRepositories.findDonationByDonorId(donor.getId());
                }
                donorDetailsList.add(new DonorDetails(donor, donorAddress, donorDocument, donorDonations));
            }
            return donorDetailsList;

        } catch (Exception e) {
            throw new RuntimeException("Error fetching donor details: " + e.getMessage(), e);
        }
    }


    public DonorResponse register(UserAuthorize userAuthorize, Donor donor) {
        try {
            donor.setDate(new Date());

            donor.setCreatedBy(userAuthorize.getUserId());
            Donor newDonor = donorRepository.save(donor);

            if (newDonor.getId() > 0) {
                return new DonorResponse("Success", "Donor registered successfully.");
            } else {
                return new DonorResponse("Failure", "Donor registration failed.");
            }

        } catch (Exception e) {
            throw new RuntimeException("Error creating donor: " + e.getMessage(), e);
        }
    }


    public DonorAddressResponse saveAddress(UserAuthorize userAuthorize, DonorAddress donorAddress) {
        try {

            donorAddress.setDonorId(userAuthorize.getUserId());
            DonorAddress donorAddress1 = donorAddressRepository.save(donorAddress);

            if (donorAddress1.getId() > 0) {
                return new DonorAddressResponse("Success", "Donor registered successfully.");
            } else {
                return new DonorAddressResponse("Failure", "Donor registration failed.");
            }

        } catch (Exception e) {
            throw new RuntimeException("Error creating donor: " + e.getMessage(), e);
        }
    }


    public DonorDocumentResponse saveDocument(UserAuthorize userAuthorize, DonorDocument donorDocument) {
        try {

            donorDocument.setDonorId(userAuthorize.getUserId());
            DonorDocument donorDocument1 = donorDocumentRepository.save(donorDocument);

            if (donorDocument1.getId() > 0) {
                return new DonorDocumentResponse("Success", "Donor Document Saved successfully.");
            } else {
                return new DonorDocumentResponse("Failure", "Donor Document Save failed.");
            }

        } catch (Exception e) {
            throw new RuntimeException("Error creating donor: " + e.getMessage(), e);
        }
    }
}
