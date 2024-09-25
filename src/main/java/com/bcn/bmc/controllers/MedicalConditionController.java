package com.bcn.bmc.controllers;

import com.bcn.bmc.helper.TokenData;
import com.bcn.bmc.models.*;
import com.bcn.bmc.services.MedicalConditionService;
import com.bcn.bmc.services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/medical-conditions")
public class MedicalConditionController {

    private final MedicalConditionService medicalConditionService;

    @Autowired
    private TokenData tokenHelper;

    @Autowired
    public MedicalConditionController(MedicalConditionService medicalConditionService) {
        this.medicalConditionService = medicalConditionService;
    }

    @PostMapping("")
    public CustomResponse createCondition(@RequestHeader("Authorization") String tokenHeader,
                                          @RequestBody MedicalCondition medicalCondition) {
        UserAuthorize userAuthorize = tokenHelper.parseToken(tokenHeader);
        return medicalConditionService.createCondition(userAuthorize, medicalCondition);
    }

    @GetMapping("/all")
    public List<MedicalCondition> getAllMedicalConditions(@RequestHeader("Authorization") String tokenHeader) {
        UserAuthorize userAuthorize = tokenHelper.parseToken(tokenHeader);
        return medicalConditionService.getAllMedicalConditions(userAuthorize);
    }

    @PutMapping("/{conditionId}")
    public CustomResponse updateCondition(@RequestHeader("Authorization") String tokenHeader,
                                          @PathVariable long conditionId,
                                          @RequestBody MedicalCondition medicalCondition) {
        UserAuthorize userAuthorize = tokenHelper.parseToken(tokenHeader);
        return medicalConditionService.updateCondition(userAuthorize, conditionId, medicalCondition);
    }

    @DeleteMapping("/{conditionId}")
    public CustomResponse deleteCondition(@RequestHeader("Authorization") String tokenHeader,
                                          @PathVariable long conditionId) {
        UserAuthorize userAuthorize = tokenHelper.parseToken(tokenHeader);
        return medicalConditionService.deleteCondition(userAuthorize, conditionId);
    }
}
