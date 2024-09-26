package com.bcn.bmc.services;

import com.bcn.bmc.enums.ActiveStatus;
import com.bcn.bmc.enums.ActivityStatus;
import com.bcn.bmc.models.*;
import com.bcn.bmc.repositories.HospitalRepository;
import com.bcn.bmc.repositories.OrganizationRepository;
import com.bcn.bmc.repositories.UserActivityRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class HospitalService {

    private final HospitalRepository hospitalRepository;

    private final OrganizationRepository organizationRepository;

    private final HospitalAddressService hospitalAddressService;

    private final HospitalDocumentService hospitalDocumentService;

    private final DonationService donationService;

    private final StockService stockService;

    private final UserActivityRepository userActivityRepository;


    public HospitalService(HospitalRepository hospitalRepository, OrganizationRepository organizationRepository, HospitalAddressService hospitalAddressService, HospitalDocumentService hospitalDocumentService,  DonationService donationService, StockService stockService, UserActivityRepository userActivityRepository
    ) {
        this.hospitalRepository = hospitalRepository;
        this.organizationRepository = organizationRepository;
        this.hospitalAddressService = hospitalAddressService;
        this.hospitalDocumentService = hospitalDocumentService;
        this.donationService = donationService;
        this.stockService = stockService;
        this.userActivityRepository = userActivityRepository;

    }

    @Transactional
    public Hospital createHospital(Hospital hospital) {
        try {
            Hospital savedHospital = hospitalRepository.save(hospital);

            Organization organization = new Organization(
                    Math.toIntExact(savedHospital.getId()),
                    savedHospital.getHospitalName(),
                    LocalDate.now()
            );
            organizationRepository.save(organization);
            userActivityRepository.save(new UserActivity(1L, "Save Hospital", "Save Hospital: " + savedHospital.getId(), "", LocalDateTime.now(), ActivityStatus.SUCCESS));
            return savedHospital;
        } catch (Exception e) {
            userActivityRepository.save(new UserActivity(1L, "Save Hospital", "Save Hospital Failed", "", LocalDateTime.now(), ActivityStatus.FAILURE));
            throw new RuntimeException("Error creating hospital and organization: " + e.getMessage(), e);
        }
    }

    public List<HospitalDetails> getAllHospitals(UserAuthorize admin) {

        List<HospitalDetails> allHospitalsDetails = new ArrayList<>();
         List<Hospital> hospitals = new ArrayList<>();
         HospitalAddress hospitalAddress;
         List<HospitalDocument> hospitalDocuments;
         List<DonationDetails> donations;
         List<Stock> stocks;


        try {
            if (admin.getOrganization() == 1) {
                hospitals  = hospitalRepository.findAllByStatus(ActiveStatus.ACTIVE);
            }else{
                hospitals = hospitalRepository.findAllByOrganizationId(admin.getOrganization());
            }

            if(!hospitals.isEmpty()){
                for(Hospital hospital : hospitals){
                    hospitalAddress = hospitalAddressService.getAddressByHospitalId(hospital.getId());
                    hospitalDocuments = hospitalDocumentService.getHospitalDocumentsById(hospital.getId());
                    donations = donationService.getAllDonationsForStock(hospital.getId());
                    stocks = stockService.getAllStock(hospital.getId());
                    allHospitalsDetails.add(new HospitalDetails(hospital, hospitalAddress, hospitalDocuments, donations, stocks));
                }
            }
                return allHospitalsDetails;

        } catch (Exception e) {
            System.out.println("Error fetching all hospitals: " + e.getMessage());
            return null;
        }
    }


    public HospitalDetails getHospitalDetails(UserAuthorize admin) {
        HospitalDetails hospitalDetails = null;

        try {
            long organizationId = (long) admin.getOrganization();

            Hospital hospital = hospitalRepository.findByOrganizationId(organizationId);
            if (hospital == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Hospital not found");
            }

            HospitalAddress hospitalAddress = hospitalAddressService.getAddressByHospitalId(organizationId);
//            if (hospitalAddress == null) {
//                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Hospital address not found");
//            }

            List<HospitalDocument> hospitalDocuments = hospitalDocumentService.getHospitalDocumentsById(organizationId);
            List<DonationDetails> donations = donationService.getAllDonationsForStock(organizationId);
            List<Stock> stocks = stockService.getAllStock(organizationId);

            hospitalDetails = new HospitalDetails(hospital, hospitalAddress, hospitalDocuments, donations, stocks);

        } catch (NullPointerException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while fetching hospital details", e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred, " + e.getMessage());
        }

        return hospitalDetails;
    }


    public Hospital getHospitalById(long id) {
        try {
            return hospitalRepository.findById(id).orElse(null);
        } catch (Exception e) {
            System.out.println("Error fetching hospital by ID: " + e.getMessage());
            return null;
        }
    }

    public Hospital getHospitalByName(String hospitalName) {
        try {
            return hospitalRepository.findByHospitalName(hospitalName).orElse(null);
        } catch (Exception e) {
            System.out.println("Error fetching hospital by name: " + e.getMessage());
            return null;
        }
    }

    public List<Hospital> getHospitalBySector(String sector) {
        try {
            return hospitalRepository.findBySector(sector);
        } catch (Exception e) {
            System.out.println("Error fetching hospitals by sector: " + e.getMessage());
            return null;
        }
    }

    @Transactional
    public HospitalResponse updateHospital(Hospital hospital) {
        try {
            if (hospitalRepository.existsById(hospital.getId())) {
                Hospital updatedHospital = hospitalRepository.save(hospital);
                userActivityRepository.save(new UserActivity(1L, "Update Hospital", "Update Hospital: " + updatedHospital.getId(), "", LocalDateTime.now(), ActivityStatus.SUCCESS));
                return new HospitalResponse(Math.toIntExact(updatedHospital.getId()), "Hospital updated successfully");
            } else {
                return new HospitalResponse(-1, "Hospital not found");
            }
        } catch (Exception e) {
            userActivityRepository.save(new UserActivity(1L, "Update Hospital", "Update Hospital Failed", "", LocalDateTime.now(), ActivityStatus.FAILURE));
            return new HospitalResponse(-1, "Failed to update hospital: " + e.getMessage());
        }
    }

    @Transactional
    public HospitalResponse deleteHospitalById(long id) {
        try {
            Hospital hospital = hospitalRepository.findById(id).orElse(null);
            if (hospital != null) {
                hospital.setStatus(ActiveStatus.INACTIVE);
                hospitalRepository.save(hospital);
                userActivityRepository.save(new UserActivity(1L, "Delete Hospital", "Delete Hospital: " + hospital.getId(), "", LocalDateTime.now(), ActivityStatus.SUCCESS));
                return new HospitalResponse(Math.toIntExact(id), "Hospital marked as inactive successfully");
            } else {
                return new HospitalResponse(-1, "Hospital not found");
            }
        } catch (Exception e) {
            userActivityRepository.save(new UserActivity(1L, "Delete Hospital", "Delete Hospital Failed", "", LocalDateTime.now(), ActivityStatus.FAILURE));
            return new HospitalResponse(-1, "Failed to mark hospital as inactive: " + e.getMessage());
        }
    }


}