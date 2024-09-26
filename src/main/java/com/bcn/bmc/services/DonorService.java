package com.bcn.bmc.services;

import com.bcn.bmc.enums.ActiveStatus;
import com.bcn.bmc.enums.ActivityStatus;
import com.bcn.bmc.enums.Gender;
import com.bcn.bmc.models.*;
import com.bcn.bmc.repositories.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;
//import java.time.temporal.ChronoUnit;


@Service
public class DonorService {

    private final DonorRepository donorRepository;
    private final DonorAddressRepository donorAddressRepository;

    private final DonorDocumentRepository donorDocumentRepository;
    private final DonationRepositories donationRepositories;

    private final UserActivityRepository userActivityRepository;

    private final DonationService donationService;

    public DonorService(DonorRepository donorRepository, DonorAddressRepository donorAddressRepository, DonorDocumentRepository donorDocumentRepository, DonationRepositories donationRepositories, DonationService donationService, UserActivityRepository userActivityRepository
    ) {
        this.donorRepository = donorRepository;
        this.donorAddressRepository = donorAddressRepository;
        this.donorDocumentRepository = donorDocumentRepository;
        this.donationRepositories = donationRepositories;
        this.donationService = donationService;
        this.userActivityRepository = userActivityRepository;

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
                userActivityRepository.save(new UserActivity(userAuthorize.getUserId(), "Register Donor", "Register Donor: " + newDonor.getId(), "", LocalDateTime.now(), ActivityStatus.SUCCESS));

                return new DonorResponse("Success", "Donor registered successfully.", newDonor.getId());
            } else {
                userActivityRepository.save(new UserActivity(userAuthorize.getUserId(), "Register Donor", "Register Donor Failed", "", LocalDateTime.now(), ActivityStatus.FAILURE));
                return new DonorResponse("Failure", "Donor registration failed.");
            }

        } catch (Exception e) {
            userActivityRepository.save(new UserActivity(userAuthorize.getUserId(), "Register Donor", "Register Donor Failed", "", LocalDateTime.now(), ActivityStatus.FAILURE));
            throw new RuntimeException("Error creating donor: " + e.getMessage(), e);
        }
    }

    public DonorResponse createDonorFromCsv(UserAuthorize userAuthorize, MultipartFile file) {
        List<Donor> successfulDonors = new ArrayList<>();
        List<String> failedDonorRows = new ArrayList<>();
        List<String> failedDonationRows = new ArrayList<>();
        List<String> failedDonorMessages = new ArrayList<>();
        List<String> failedDonationMessages = new ArrayList<>();
        int successCount = 0;

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("M/d/yyyy");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String[] headers = reader.readLine().trim().split(",");
            String line;

            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",");
                Map<String, String> row = new HashMap<>();

                for (int i = 0; i < headers.length && i < columns.length; i++) {
                    row.put(headers[i].replaceAll("^\\W+", "").trim(), columns[i]);
                }

                try {
                    Donor donor = new Donor();
                    donor.setNic(row.get("nic"));
                    donor.setFirstName(row.get("firstName"));
                    donor.setLastName(row.get("lastName"));

                    String gender = row.get("gender");
                    if (gender != null) {
                        donor.setGender(Gender.valueOf(gender.toUpperCase()));
                    }
                    donor.setBloodType(row.get("bloodType"));

                    try {
                        donor.setDob(LocalDate.parse(row.get("dob"), dateFormatter));
                    } catch (DateTimeParseException e) {
                        failedDonorRows.add(line);
                        failedDonorMessages.add("Failed to parse DOB for donor NIC: " + donor.getNic() + ". Error: " + e.getMessage());
                        continue; // Skip to next row
                    }

                    donor.setContact(row.get("contact"));

                    DonorResponse donorResponse = register(userAuthorize, donor);
                    if ("Success".equals(donorResponse.getStatus())) {
                        successfulDonors.add(donor);
                        successCount++;

                        String donationQtyStr = row.get("donationQty");
                        if (donationQtyStr != null && !donationQtyStr.isEmpty()) {
                            double donationQty;
                            try {
                                donationQty = Double.parseDouble(donationQtyStr);
                            } catch (NumberFormatException e) {
                                failedDonationRows.add(line);
                                failedDonationMessages.add("Invalid donation quantity for donor NIC: " + donor.getNic() + ". Error: " + e.getMessage());
                                continue;
                            }

                            if (donationQty > 0) {
                                Donation donation = new Donation();
                                donation.setDonor(donorResponse.getDonorId());
                                donation.setBloodType(donor.getBloodType());
                                donation.setQuantity(donationQty);

                                String donationDateStr = row.get("donationDate");
                                if (donationDateStr == null || donationDateStr.isEmpty()) {
                                    donation.setDonationDate(java.sql.Date.valueOf(LocalDate.now()));
                                } else {
                                    try {
                                        donation.setDonationDate(java.sql.Date.valueOf(LocalDate.parse(donationDateStr, dateFormatter)));
                                    } catch (DateTimeParseException e) {
                                        failedDonationRows.add(line);
                                        failedDonationMessages.add("Failed to parse donation date for donor NIC: " + donor.getNic() + ". Error: " + e.getMessage());
                                        continue;
                                    }
                                }

                                DonationResponse donationResponse = donationService.createDonation(userAuthorize, donation);
                                if (!"Success".equals(donationResponse.getStatus())) {
                                    failedDonationRows.add(line);
                                    failedDonationMessages.add("Failed to create donation for donor NIC: " + donor.getNic() + ". Error: " + donationResponse.getMessage());
                                }
                            }
                        }

                    } else {
                        failedDonorRows.add(line);
                        failedDonorMessages.add("Failed to register donor NIC: " + donor.getNic() + ". Error: " + donorResponse.getMessage());
                    }

                } catch (Exception e) {
                    failedDonorRows.add(line);
                    failedDonorMessages.add("Failed to process row: " + line + ". Error: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error processing CSV: " + e.getMessage(), e);
        }

        String resultMessage = "Successfully registered " + successCount + " donors. Failed donors: " + failedDonorRows.size() + ". Failed donations: " + failedDonationRows.size();

        if (!failedDonorMessages.isEmpty()) {
            System.out.println("Failed Donors: " + failedDonorMessages);
        }

        if (!failedDonationMessages.isEmpty()) {
            System.out.println("Failed Donations: " + failedDonationMessages);
        }

        return new DonorResponse("Completed", resultMessage, (Long) null);
    }




    public DonorAddressResponse saveAddress(UserAuthorize userAuthorize, Long donorId, DonorAddress donorAddress) {
        try {
            donorAddress.setDonorId(donorId);
            DonorAddress donorAddress1 = donorAddressRepository.save(donorAddress);

            if (donorAddress1.getId() > 0) {
                userActivityRepository.save(new UserActivity(userAuthorize.getUserId(), "Register Donor Address", "Register Donor Address: " + donorAddress1.getDonorId(), "", LocalDateTime.now(), ActivityStatus.SUCCESS));

                return new DonorAddressResponse("Success", "Donor address registered successfully.");
            } else {
                userActivityRepository.save(new UserActivity(userAuthorize.getUserId(), "Register Donor Address", "Register Donor Address Failed", "", LocalDateTime.now(), ActivityStatus.FAILURE));
                return new DonorAddressResponse("Failure", "Donor address registration failed.");
            }

        } catch (Exception e) {
            userActivityRepository.save(new UserActivity(userAuthorize.getUserId(), "Register Donor Address", "Register Donor Address Failed", "", LocalDateTime.now(), ActivityStatus.FAILURE));
            throw new RuntimeException("Error creating donor: " + e.getMessage(), e);
        }
    }


    public DonorDocumentResponse saveDocument(UserAuthorize userAuthorize, DonorDocument donorDocument) {
        try {

            donorDocument.setDonorId(userAuthorize.getUserId());
            DonorDocument donorDocument1 = donorDocumentRepository.save(donorDocument);

            if (donorDocument1.getId() > 0) {
                userActivityRepository.save(new UserActivity(userAuthorize.getUserId(), "Register Donor Document", "Register Donor Document: " + donorDocument1.getDonorId(), "", LocalDateTime.now(), ActivityStatus.SUCCESS));
                return new DonorDocumentResponse("Success", "Donor Document Saved successfully.");
            } else {
                userActivityRepository.save(new UserActivity(userAuthorize.getUserId(), "Register Donor Document", "Register Donor Document Failed", "", LocalDateTime.now(), ActivityStatus.FAILURE));
                return new DonorDocumentResponse("Failure", "Donor Document Save failed.");
            }

        } catch (Exception e) {
            userActivityRepository.save(new UserActivity(userAuthorize.getUserId(), "Register Donor Document", "Register Donor Document Failed", "", LocalDateTime.now(), ActivityStatus.FAILURE));
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

            userActivityRepository.save(new UserActivity(userAuthorize.getUserId(), "Update Donor", "Update Donor: " + existingDonor.getId(), "", LocalDateTime.now(), ActivityStatus.SUCCESS));
            return new DonorResponse("Success", "Donor details updated successfully.", updatedDonor.getId());

        } catch (Exception e) {
            userActivityRepository.save(new UserActivity(userAuthorize.getUserId(), "Update Donor", "Update Donor Failed", "", LocalDateTime.now(), ActivityStatus.FAILURE));
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
//            if (admin.getOrganization() == 1) {
                return donorRepository.findAllByStatus(ActiveStatus.ACTIVE);
//            }else{
//                return donorRepository.findAllByOrganizationId(admin.getOrganization());
//            }
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


    @Transactional
    public DonorAddressResponse updateAddress(UserAuthorize userAuthorize, DonorAddress newAddress) {
        System.out.println("DONNER ADDRESS - DONOR ID: "+ newAddress.getDonorId());
        Optional<DonorAddress> addressOptional = donorAddressRepository.findAddressByDonorId(newAddress.getDonorId());


        if (addressOptional.isPresent()) {
            DonorAddress existingAddress = addressOptional.get();
            existingAddress.setStreetNumber(newAddress.getStreetNumber());
            existingAddress.setStreetName(newAddress.getStreetName());
            existingAddress.setCity(newAddress.getCity());
            existingAddress.setState(newAddress.getState());
            existingAddress.setZipCode(newAddress.getZipCode());



            DonorAddress savedAddress = donorAddressRepository.save(existingAddress);

            userActivityRepository.save(new UserActivity(userAuthorize.getUserId(), "Update Donor Address", "Update Donor Address: " + savedAddress.getDonorId(), "", LocalDateTime.now(), ActivityStatus.SUCCESS));
            return new DonorAddressResponse("Success", "Address updated successfully", savedAddress.getId());
        } else {
            userActivityRepository.save(new UserActivity(userAuthorize.getUserId(), "Update Donor Address", "Update Donor Address Failed", "", LocalDateTime.now(), ActivityStatus.FAILURE));
            return saveAddress(userAuthorize, newAddress.getDonorId(), newAddress);
        }
    }


    public DonorFilterResponse filterDonors(DonorFilterRequest request) {
        List<String> bloodTypes = request.getBloodTypes();
        String donorsCount = request.getDonorsCount();
        Date donorsAreFrom = request.getDonorsAreFrom();
        Date donorsAreTo = request.getDonorsAreTo();
        boolean eligibleDonors = request.isEligibleDonors();


        LocalDate from = donorsAreFrom.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate to = donorsAreTo.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();


        List<Donor> allDonors = donorRepository.findByStatusAndBloodTypeIn(ActiveStatus.ACTIVE, bloodTypes);

        System.out.println("ALL DONORS: "+ allDonors);

        List<Long> donorIds = new ArrayList<>();
        if (eligibleDonors) {
            donorIds = filterDonorsByDonationDate(allDonors, from, to);
        } else {
            donorIds = allDonors.stream().map(Donor::getId).collect(Collectors.toList());
        }

        if (!donorsCount.isBlank()) {
            int count = Integer.parseInt(donorsCount);
            donorIds = donorIds.stream().limit(count).collect(Collectors.toList());
        }

        List<Donor> filteredDonors = donorRepository.findAllById(donorIds);

        System.out.println("filteredDonors: "+ filteredDonors);


        DonorFilterResponse response = new DonorFilterResponse(
                filteredDonors,
                bloodTypes,
                from,
                to,
                donorsCount,
                eligibleDonors,
                request.getTextMessage()
        );

        System.out.println("RESPONSE: "+ response);
        return response;
    }

    private List<Long> filterDonorsByDonationDate(List<Donor> donors, LocalDate from, LocalDate to) {
        return donors.stream()
                .filter(donor -> {
                    Date lastDonationDate = donationRepositories.findLastDonationByDonor(donor.getId())
                            .map(donation -> donation.getDonationDate())
                            .orElse(null);

                    if (lastDonationDate == null) {
                        return true;
                    }

                    LocalDate lastDonation = lastDonationDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    LocalDate threeMonthsAgo = LocalDate.now().minusMonths(3);

                    return lastDonation.isBefore(threeMonthsAgo) &&
                            (lastDonation.isEqual(from) || lastDonation.isAfter(from)) &&
                            (lastDonation.isEqual(to) || lastDonation.isBefore(to));
                })
                .map(Donor::getId)
                .collect(Collectors.toList());
    }




}







