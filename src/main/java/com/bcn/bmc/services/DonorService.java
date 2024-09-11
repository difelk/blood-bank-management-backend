package com.bcn.bmc.services;

import com.bcn.bmc.models.*;
import com.bcn.bmc.repositories.DonorAddressRepository;
import com.bcn.bmc.repositories.DonorDocumentRepository;
import com.bcn.bmc.repositories.DonorRepository;
import com.bcn.bmc.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class DonorService {

    private final DonorRepository donorRepository;
    private final DonorAddressRepository donorAddressRepository;

    private final DonorDocumentRepository donorDocumentRepository;


    public DonorService(DonorRepository donorRepository, DonorAddressRepository donorAddressRepository, DonorDocumentRepository donorDocumentRepository) {
        this.donorRepository = donorRepository;
        this.donorAddressRepository = donorAddressRepository;
        this.donorDocumentRepository = donorDocumentRepository;

    }


    public DonorDetails getAllDonors(UserAuthorize userAuthorize, Long donorId) {
        try {
            Optional<Donor> optionalDonor = donorRepository.findById(donorId);

            if (optionalDonor.isEmpty()) {
                throw new RuntimeException("Donor not found for ID: " + donorId);
            }
            Donor donor = optionalDonor.get();
            DonorAddress donorAddress = null;
            List<DonorDocument> donorDocument = null;
            if (donor.getId() != null) {
                donorAddress = donorAddressRepository.findDonorAddressByUserId(donorId);
                donorDocument = donorDocumentRepository.findDonorDocumentsByUserId(donorId);
            }
            return new DonorDetails(donor, donorAddress, donorDocument);

        } catch (Exception e) {
            throw new RuntimeException("Error creating donor: " + e.getMessage(), e);
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
