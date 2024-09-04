package com.bcn.bmc.services;

import com.bcn.bmc.models.Hospital;
import com.bcn.bmc.models.HospitalResponse;
import com.bcn.bmc.models.Organization;
import com.bcn.bmc.repositories.HospitalRepository;
import com.bcn.bmc.repositories.OrganizationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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

    public List<Hospital> getAllHospitals() {
        try {

            return hospitalRepository.findAll();
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
            if (hospitalRepository.existsById(id)) {
                hospitalRepository.deleteById(id);
                return new HospitalResponse((int) id, "Hospital deleted successfully");
            } else {
                return new HospitalResponse(-1, "Hospital not found");
            }
        } catch (Exception e) {
            return new HospitalResponse(-1, "Failed to delete hospital: " + e.getMessage());
        }
    }
}