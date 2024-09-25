package com.bcn.bmc.controllers;

import com.bcn.bmc.enums.Status;
import com.bcn.bmc.helper.TokenData;
import com.bcn.bmc.models.CustomResponse;
import com.bcn.bmc.models.PatientCondition;
import com.bcn.bmc.models.PatientConditionsHistory;
import com.bcn.bmc.models.UserAuthorize;
import com.bcn.bmc.services.PatientConditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/patient-conditions")
public class PatientConditionController {

    private final PatientConditionService patientConditionService;

    @Autowired
    private TokenData tokenHelper;


    @Autowired
    public  PatientConditionController(PatientConditionService patientConditionService){
        this.patientConditionService = patientConditionService;
    }

    @GetMapping("")
    public List<PatientCondition> getAllPatientConditions() {
        return patientConditionService.getAllPatientConditions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientCondition> getPatientConditionById(@PathVariable Long id) {
        Optional<PatientCondition> patientCondition = patientConditionService.getPatientConditionById(id);
        return patientCondition.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public CustomResponse createPatientCondition(@RequestHeader("Authorization") String tokenHeader, @RequestBody PatientCondition patientCondition) {
        UserAuthorize userAuthorize = tokenHelper.parseToken(tokenHeader);
        return patientConditionService.savePatientCondition(userAuthorize, patientCondition);
    }

    @PutMapping("/{id}")
    public CustomResponse updatePatientCondition(@RequestHeader("Authorization") String tokenHeader, @PathVariable Long id, @RequestBody PatientCondition patientConditionDetails) {
        Optional<PatientCondition> optionalPatientCondition = patientConditionService.getPatientConditionById(id);
        UserAuthorize userAuthorize = tokenHelper.parseToken(tokenHeader);
        if (optionalPatientCondition.isPresent()) {
            PatientCondition patientCondition = optionalPatientCondition.get();
            patientCondition.setPatient(patientConditionDetails.getPatient());
            patientCondition.setCondition(patientConditionDetails.getCondition());
            patientCondition.setBloodDonationCount(patientConditionDetails.getBloodDonationCount());
            return patientConditionService.savePatientCondition(userAuthorize, patientCondition);
        } else {
            return new CustomResponse(-1, "Something went wrong", "failed_at_controller_update_patient_condition", Status.FAILED);
        }
    }


    @GetMapping("/{id}/history")
    public List<PatientConditionsHistory> getPatientConditionHistoryById(@PathVariable Long id) {
       return patientConditionService.getPatientConditionHistoryById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatientCondition(@PathVariable Long id) {
        Optional<PatientCondition> patientCondition = patientConditionService.getPatientConditionById(id);
        if (patientCondition.isPresent()) {
            patientConditionService.deletePatientCondition(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
