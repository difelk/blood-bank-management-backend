package com.bcn.bmc.services;

import com.bcn.bmc.enums.ActiveStatus;
import com.bcn.bmc.models.*;
import com.bcn.bmc.repositories.HospitalRepository;
import com.bcn.bmc.repositories.OrganizationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class HospitalService {

    private final HospitalRepository hospitalRepository;

    private final OrganizationRepository organizationRepository;

    public HospitalService(HospitalRepository hospitalRepository, OrganizationRepository organizationRepository) {
        this.hospitalRepository = hospitalRepository;
        this.organizationRepository = organizationRepository;
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

            return savedHospital;
        } catch (Exception e) {
            throw new RuntimeException("Error creating hospital and organization: " + e.getMessage(), e);
        }
    }

    public List<Hospital> getAllHospitals(UserAuthorize admin) {
        try {
            if (admin.getOrganization() == 1) {
                return hospitalRepository.findAllByStatus(ActiveStatus.ACTIVE);
            }else{
                return hospitalRepository.findAllByOrganizationId(admin.getOrganization());
            }
//            return hospitalRepository.findAll();
        } catch (Exception e) {
            System.out.println("Error fetching all hospitals: " + e.getMessage());
            return null;
        }
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
                return new HospitalResponse(Math.toIntExact(updatedHospital.getId()), "Hospital updated successfully");
            } else {
                return new HospitalResponse(-1, "Hospital not found");
            }
        } catch (Exception e) {
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
                return new HospitalResponse(Math.toIntExact(id), "Hospital marked as inactive successfully");
            } else {
                return new HospitalResponse(-1, "Hospital not found");
            }
        } catch (Exception e) {
            return new HospitalResponse(-1, "Failed to mark hospital as inactive: " + e.getMessage());
        }
    }

//    public List<HospitalJoinedDetails> getAllHospitalJoinedDetails() {
//        List<HospitalJoinedDetails> hospitalJoinedDetails = new ArrayList<>();
//
//        HospitalAddressService hospitalAddressService = new HospitalAddressService();
//        HospitalDocumentService hospitalDocumentService = new HospitalDocumentService();
//
//        List<Hospital> hospitalBasicData = getAllHospitals();
//        List<HospitalAddress> hospitalAddressData = hospitalAddressService.getAllAddresses();
//        List<HospitalDocument> hospitalDocumentsData = hospitalDocumentService.getAllDocuments();
//
//        System.out.println("hospitalAddressData - " + hospitalAddressData.size());
//        System.out.println("hospitalDocumentsData - " + hospitalDocumentsData.size());
//
//        for(Hospital hospital : hospitalBasicData){
//
//            for(HospitalAddress address : hospitalAddressData){
//
//                if(hospital.getId() == address.getHospitalId()){
//
//                for(HospitalDocument document : hospitalDocumentsData){
//
//                    HospitalJoinedDetails hospitalJoinedDetail = new HospitalJoinedDetails();
//
//                    if(hospital.getId() == document.getHospital().getId()){
//                        hospitalJoinedDetail.setId(hospital.getId());
//                        hospitalJoinedDetail.setHospitalName(hospital.getHospitalName());
//                        hospitalJoinedDetail.setSector(hospital.getSector());
//                        hospitalJoinedDetail.setContactNo1(hospital.getContactNo1());
//                        hospitalJoinedDetail.setContactNo2(hospital.getContactNo2());
//                        hospitalJoinedDetail.setStreetNumber(address.getStreetNumber());
//                        hospitalJoinedDetail.setStreetName(address.getStreetName());
//                        hospitalJoinedDetail.setCity(address.getCity());
//
//                        List<HospitalDocument> docs = new ArrayList<>();
//
//                        for(HospitalDocument document2 : hospitalDocumentsData){
//                            if(hospital.getId() == document2.getHospital().getId()){
//                                docs.add(new HospitalDocument(document2.getId(),document2.getFileName(),document2.getFileType(),document2.getFileSize(),document2.getData(),document2.getHospital()));
//                            }
//                        }
//                        hospitalJoinedDetail.setHospitalDocuments(docs);
//                        hospitalJoinedDetails.add(hospitalJoinedDetail);
//                    }
//
//                }
//                }
//            }
//        }
//        return hospitalJoinedDetails;
//    }


}