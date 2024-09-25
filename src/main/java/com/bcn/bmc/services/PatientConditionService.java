package com.bcn.bmc.services;

import com.bcn.bmc.models.PatientCondition;
import com.bcn.bmc.repositories.PatientConditionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientConditionService {


    private final PatientConditionRepository patientConditionRepository;


    @Autowired
    public PatientConditionService(PatientConditionRepository patientConditionRepository){
        this.patientConditionRepository = patientConditionRepository;
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
                }
                else {
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
