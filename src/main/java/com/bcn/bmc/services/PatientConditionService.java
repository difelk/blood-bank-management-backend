package com.bcn.bmc.services;

import com.bcn.bmc.enums.Status;
import com.bcn.bmc.models.*;
import com.bcn.bmc.repositories.*;
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

    private final StockRepository stockRepository;


    @Autowired
    public PatientConditionService(PatientConditionRepository patientConditionRepository, PatientRepository patientRepository, OrganizationRepository organizationRepository, MedicalConditionRepository medicalConditionRepository, StockRepository stockRepository) {
        this.patientConditionRepository = patientConditionRepository;
        this.patientRepository = patientRepository;
        this.organizationRepository = organizationRepository;
        this.medicalConditionRepository = medicalConditionRepository;
        this.stockRepository = stockRepository;
    }


    public List<PatientCondition> getAllPatientConditions() {
        return patientConditionRepository.findAll();
    }


    public Optional<PatientCondition> getPatientConditionById(Long id) {
        return patientConditionRepository.findById(id);
    }

    public boolean isStockAvailable(long organizationId, long patientId, double qty) {

        Optional<Patient> patient = patientRepository.findById(patientId);
        if (patient.isPresent()) {

            String bloodType = patient.get().getBloodType().equals("O+") ? patient.get().getBloodType() : patient.get().getBloodType().replace("+", " +").replace("-", " -");
            Stock stock = stockRepository.findStocksByOrganizationAndBloodType(organizationId, bloodType);
            if (stock != null) {

                return stock.getQuantity() > qty;
            } else {

                return false;
            }
        } else {

            return false;
        }
    }

    public boolean reduceFromStock(long organizationId, long patientId, double qty) {
        System.out.println("reduceFromStock organizationId " + organizationId);
        System.out.println("reduceFromStock patientId " + patientId);
        System.out.println("reduceFromStock qty " + qty);
        Optional<Patient> patient = patientRepository.findById(patientId);
        if (patient.isPresent()) {
            System.out.println("patient.isPresent()");
            String bloodType = patient.get().getBloodType().equals("O+") ? patient.get().getBloodType() : patient.get().getBloodType().replace("+", " +").replace("-", " -");
            Stock stock = stockRepository.findStocksByOrganizationAndBloodType(organizationId, bloodType);
            if (stock != null) {
                System.out.println("stock != null stock org - " + stock.getOrganizationId());
                System.out.println("stock != null current stock - " + stock.getQuantity());
                System.out.println("stock != null reeducation qty - " + qty);
                double newStockQty = stock.getQuantity() - qty;
                int effected = stockRepository.updateStockQuantityByOrganizationAndBloodType(organizationId, bloodType, newStockQty);
                return effected > 0;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public CustomResponse savePatientCondition(UserAuthorize userAuthorize, PatientCondition patientCondition) {
        System.out.println("userAuthorize org id: " + userAuthorize.getOrganization());
        if (isStockAvailable(userAuthorize.getOrganization(), patientCondition.getPatient(), patientCondition.getBloodDonationCount())) {
            try {
                PatientCondition patientCondition1 = patientConditionRepository.save(patientCondition);
                if (patientCondition1.getId() > 0) {
                    reduceFromStock(userAuthorize.getOrganization(), patientCondition.getPatient(), patientCondition.getBloodDonationCount());
                    return new CustomResponse(1, "Record added successfully", "success_at_save", Status.SUCCESS);
                } else {
                    return new CustomResponse(-1, "Record added failed", "failed_at_save_patient_condition", Status.FAILED);
                }
            } catch (Exception e) {
                return new CustomResponse(-1, "Record added failed", "failed_at_save_patient_condition", Status.FAILED);
            }
        } else {
            return new CustomResponse(-1, "No Enough Stock Available", "no_enough_stock", Status.FAILED);
        }
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
