package com.bcn.bmc.services;

import com.bcn.bmc.models.*;
import com.bcn.bmc.repositories.MedicalConditionRepository;
import com.bcn.bmc.repositories.OrganizationRepository;
import com.bcn.bmc.repositories.PatientConditionRepository;
import com.bcn.bmc.repositories.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PatientConditionService {


    private final PatientConditionRepository patientConditionRepository;

    private final PatientRepository patientRepository;

    private final OrganizationRepository organizationRepository;

    private final MedicalConditionRepository medicalConditionRepository;


    @Autowired
    public PatientConditionService(PatientConditionRepository patientConditionRepository, PatientRepository patientRepository, OrganizationRepository organizationRepository, MedicalConditionRepository medicalConditionRepository) {
        this.patientConditionRepository = patientConditionRepository;
        this.patientRepository = patientRepository;
        this.organizationRepository = organizationRepository;
        this.medicalConditionRepository = medicalConditionRepository;
    }


    public List<PatientCondition> getAllPatientConditions() {
        return patientConditionRepository.findAll();
    }


    public Optional<PatientCondition> getPatientConditionById(Long id) {
        return patientConditionRepository.findById(id);
    }


    public PatientCondition savePatientCondition(PatientCondition patientCondition) {
        return patientConditionRepository.save(patientCondition);
    }

    public List<PatientConditionsHistory> getPatientConditionHistoryById(long id) {
        try {
            List<PatientConditionsHistory> patientConditionsHistoryList = new ArrayList<>();

            List<PatientCondition> patientConditions = patientConditionRepository.findByPatientId(id);
            Optional<Patient> optionalPatient = patientRepository.findById(id);

            if (optionalPatient.isEmpty()) {
                throw new RuntimeException("Patient not found");
            }

            Patient patient = optionalPatient.get();
            String patientBloodType = patient.getBloodType();
            long patientID = patient.getId();

            if (patientConditions.isEmpty()) {
                return patientConditionsHistoryList;
            }

            for (PatientCondition patientCondition : patientConditions) {
                LocalDateTime conditionDate = patientCondition.getDate();

                KeyValue patientDetails = new KeyValue(patientID, patient.getFirstName() + " " + patient.getLastName());

                Optional<Organization> optionalOrganization = organizationRepository.findOrganizationByLongId(patientCondition.getOrganization());
                KeyValue organizationDetails = optionalOrganization.map(org -> new KeyValue((long) org.getOrganizationId(), org.getOrganizationName()))
                        .orElse(new KeyValue());

                Optional<MedicalCondition> optionalMedicalCondition = medicalConditionRepository.findById(patientCondition.getCondition());
                KeyValue medicalConditionDetails = optionalMedicalCondition.map(cond -> new KeyValue(cond.getId(), cond.getConditionName()))
                        .orElse(new KeyValue());

                patientConditionsHistoryList.add(new PatientConditionsHistory(patientID, conditionDate, patientDetails, organizationDetails, patientBloodType, medicalConditionDetails, patientCondition.getBloodDonationCount()));
            }

            return patientConditionsHistoryList;
        } catch (Exception e) {
            throw new RuntimeException("ERROR in get Patient History: " + e.getMessage(), e);
        }
    }



    public PatientCondition updatePatientCondition(Long id, PatientCondition updatedPatientCondition) {
        Optional<PatientCondition> optionalPatientCondition = patientConditionRepository.findById(id);

        if (optionalPatientCondition.isPresent()) {
            PatientCondition existingPatientCondition = optionalPatientCondition.get();

            int oldBloodDonationCount = existingPatientCondition.getBloodDonationCount();
            int newBloodDonationCount = updatedPatientCondition.getBloodDonationCount();
            int difference = newBloodDonationCount - oldBloodDonationCount;

            if (difference != 0) {
                if (difference > 0) {
//                    stockService.reduceStock(difference);
                } else {
//                    stockService.increaseStock(Math.abs(difference));
                }
            }

            existingPatientCondition.setPatient(updatedPatientCondition.getPatient());
            existingPatientCondition.setCondition(updatedPatientCondition.getCondition());
            existingPatientCondition.setBloodDonationCount(newBloodDonationCount);

            return patientConditionRepository.save(existingPatientCondition);
        } else {
            throw new RuntimeException("Patient condition not found");
        }
    }


    public void deletePatientCondition(Long id) {
        patientConditionRepository.deleteById(id);
    }


}
