package com.bcn.bmc.services;

import com.bcn.bmc.enums.ActiveStatus;
import com.bcn.bmc.models.*;
import com.bcn.bmc.repositories.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;


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
        System.out.println("inside donor reg service");
        try {
            if(donorRepository.findDonorByNic(donor.getNic()).isPresent()){
                return new DonorResponse("Failure", "Donor Nic Already Exist.",donor.getId());
            }

            donor.setDate(new Date());

            donor.setCreatedBy(userAuthorize.getUserId());
            Donor newDonor = donorRepository.save(donor);

            if (newDonor.getId() > 0) {
                return new DonorResponse("Success", "Donor registered successfully.", newDonor.getId());
            } else {
                return new DonorResponse("Failure", "Donor registration failed.");
            }

        } catch (Exception e) {
            throw new RuntimeException("Error creating donor: " + e.getMessage(), e);
        }
    }

    public DonorResponse createDonorFromCsv(UserAuthorize userAuthorize, MultipartFile file) {

        try {
            InputStream inputStream = file.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String[] headers = reader.readLine().trim().split(",");
            List<Map<String, String>> data = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",");
                Map<String, String> row = new HashMap<>();
                for (int i = 0; i < headers.length && i < columns.length; i++) {
                    row.put(headers[i].replaceAll("^\\W+", "").trim(), columns[i]);
                    System.out.println("headers[i] - " + headers[i]);
                    System.out.println("columns[i] - " + (columns[i].isEmpty() ? "" : columns[i]));
                }
                try {
//                    CsvHandler.validateCsvDonor(row);
                    data.add(row);
                } catch (Exception e) {
                    System.out.println("Validation error for row: " + e.getMessage());
                }
            }
        }catch (Exception e){

        }finally {
            return new DonorResponse("Failure", "Donor registration with csv failed.");
        }
    }


    public DonorAddressResponse saveAddress(UserAuthorize userAuthorize, Long donorId, DonorAddress donorAddress) {
        try {
            donorAddress.setDonorId(donorId);
            DonorAddress donorAddress1 = donorAddressRepository.save(donorAddress);

            if (donorAddress1.getId() > 0) {
                return new DonorAddressResponse("Success", "Donor address registered successfully.");
            } else {
                return new DonorAddressResponse("Failure", "Donor address registration failed.");
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

    @Transactional
    public DonorDocument storeFile(MultipartFile file, Long donorId) throws IOException {
        Donor donor = donorRepository.findById(donorId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid donor ID"));

        DonorDocument donorDocument = new DonorDocument();
        donorDocument.setFileName(file.getOriginalFilename());
        donorDocument.setFileType(file.getContentType());
        donorDocument.setFileSize(file.getSize());
        donorDocument.setData(file.getBytes());
        donorDocument.setDonorId(donor.getId());

        return donorDocumentRepository.save(donorDocument);
    }


    @Transactional
    public DonorResponse updateDonor(UserAuthorize userAuthorize, Donor updatedDonor) {
        try {
            Optional<Donor> donorWithSameNic = donorRepository.findDonorByNic(updatedDonor.getNic());
            if (donorWithSameNic.isPresent() && !donorWithSameNic.get().getId().equals(updatedDonor.getId())) {
                return new DonorResponse("Failed", "Donor NIC already exists.");
            }

            Optional<Donor> donor = donorRepository.findById(updatedDonor.getId());
            if (donor.isEmpty()) {
                throw new RuntimeException("Donor not found for ID: " + updatedDonor.getId());
            }

            Donor existingDonor = donor.get();
            existingDonor.setFirstName(updatedDonor.getFirstName());
            existingDonor.setLastName(updatedDonor.getLastName());
            existingDonor.setNic(updatedDonor.getNic());
            existingDonor.setGender(updatedDonor.getGender());
            existingDonor.setBloodType(updatedDonor.getBloodType());
            existingDonor.setDob(updatedDonor.getDob());
            existingDonor.setContact(updatedDonor.getContact());

            donorRepository.save(existingDonor);

            return new DonorResponse("Success", "Donor details updated successfully.", updatedDonor.getId());

        } catch (Exception e) {
            throw new RuntimeException("Error updating donor: " + e.getMessage(), e);
        }
    }


    @Transactional
    public DonorResponse deleteDonorById(long donorId) {
        try {
            Donor donor = donorRepository.findById(donorId).orElse(null);
            if (donor != null) {
                donor.setStatus(ActiveStatus.INACTIVE);
                donorRepository.save(donor);
                return new DonorResponse("Success", "Donor marked as inactive successfully");
            } else {
                return new DonorResponse("Failed", "Donor not found");
            }
        } catch (Exception e) {
            return new DonorResponse("Failed", "Failed to mark donor as inactive: " + e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<DonorDocumentResponse> deleteDocumentById(Long documentId) {
        try {
            if (!donorDocumentRepository.existsById(documentId)) {
                return new ResponseEntity<>(
                        new DonorDocumentResponse("NOT FOUND","Document not found"),
                        HttpStatus.NOT_FOUND
                );
            }
            DonorDocument document = donorDocumentRepository.findById(documentId).orElse(null);
            donorDocumentRepository.deleteById(documentId);

            return new ResponseEntity<>(
                    new DonorDocumentResponse(
                            "Success",
                            "Document deleted successfully"
                    ),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new DonorDocumentResponse("Failed", "Error occurred while deleting document"),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    public Donor getDonorByNic(String donorNic) {
        try {
            return donorRepository.findDonorByNic(donorNic).orElse(null);
        } catch (Exception e) {
            System.out.println("Error fetching donor by nic: " + e.getMessage());
            return null;
        }
    }

    public List<Donor> getAllDonors(UserAuthorize admin) {
        try {
            if (admin.getOrganization() == 1) {
                return donorRepository.findAllByStatus(ActiveStatus.ACTIVE);
            }else{
                return donorRepository.findAllByOrganizationId(admin.getOrganization());
            }
        } catch (Exception e) {
            System.out.println("Error fetching all donors: " + e.getMessage());
            return null;
        }


    }

    public List<DonorAddress> getAllAddresses() {
        try {

            return donorAddressRepository.findAll();
        } catch (Exception e) {
            System.out.println("Error fetching all adresses: " + e.getMessage());
            return null;
        }
    }


    public List<DonorDocument> getDonorDocumentsById(Long donorId) {
        return donorDocumentRepository.findDonorDocumentsByUserId(donorId);
    }


    public Donor getDonorById(long donorId) {
        try {
            return donorRepository.findById(donorId).orElse(null);
        } catch (Exception e) {
            System.out.println("Error fetching donor by ID: " + e.getMessage());
            return null;
        }
    }
}
