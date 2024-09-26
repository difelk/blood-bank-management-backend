package com.bcn.bmc.services;

import com.bcn.bmc.enums.ActivityStatus;
import com.bcn.bmc.enums.ConditionStatus;
import com.bcn.bmc.enums.Status;
import com.bcn.bmc.models.CustomResponse;
import com.bcn.bmc.models.MedicalCondition;
import com.bcn.bmc.models.UserActivity;
import com.bcn.bmc.models.UserAuthorize;
import com.bcn.bmc.repositories.MedicalConditionRepository;
import com.bcn.bmc.repositories.UserActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MedicalConditionService {

    private final MedicalConditionRepository medicalConditionRepository;
    private final UserActivityRepository userActivityRepository;

    @Autowired
    public MedicalConditionService(MedicalConditionRepository medicalConditionRepository, UserActivityRepository userActivityRepository) {
        this.medicalConditionRepository = medicalConditionRepository;
        this.userActivityRepository = userActivityRepository;
    }

    public List<MedicalCondition> getAllMedicalConditions(UserAuthorize userAuthorize) {
        return medicalConditionRepository.findByStatus(ConditionStatus.ACTIVE);
    }

    public CustomResponse createCondition(UserAuthorize userAuthorize, MedicalCondition medicalCondition) {
        Optional<MedicalCondition> existingCondition = medicalConditionRepository.findByConditionName(medicalCondition.getConditionName());

        if (existingCondition.isPresent() && existingCondition.get().getStatus() == ConditionStatus.ACTIVE) {
            return new CustomResponse(0, "Condition already exists", "CONDITION_EXISTS", Status.FAILED);
        }

        MedicalCondition savedCondition = medicalConditionRepository.save(medicalCondition);
        userActivityRepository.save(new UserActivity(userAuthorize.getUserId(), "Create Condition", "Create Condition: " + savedCondition.getConditionName(), "", LocalDateTime.now(), ActivityStatus.SUCCESS));
        return new CustomResponse(savedCondition.getId().intValue(), "Condition created successfully", "CONDITION_CREATED", Status.SUCCESS);
    }

    public CustomResponse updateCondition(UserAuthorize userAuthorize, Long conditionId, MedicalCondition medicalCondition) {
        Optional<MedicalCondition> existingCondition = medicalConditionRepository.findById(conditionId);

        if (existingCondition.isEmpty() || existingCondition.get().getStatus() == ConditionStatus.INACTIVE) {
            return new CustomResponse(0, "Condition not found", "CONDITION_NOT_FOUND", Status.FAILED);
        }

        Optional<MedicalCondition> conditionWithSameName = medicalConditionRepository.findByConditionName(medicalCondition.getConditionName());
        if (conditionWithSameName.isPresent() && !conditionWithSameName.get().getId().equals(conditionId)) {
            return new CustomResponse(0, "Condition name already exists", "CONDITION_NAME_EXISTS", Status.FAILED);
        }

        MedicalCondition conditionToUpdate = existingCondition.get();
        conditionToUpdate.setConditionName(medicalCondition.getConditionName());
        conditionToUpdate.setDescription(medicalCondition.getDescription());
        conditionToUpdate.setSymptoms(medicalCondition.getSymptoms());

        medicalConditionRepository.save(conditionToUpdate);

        userActivityRepository.save(new UserActivity(userAuthorize.getUserId(), "Update Condition", "Update Condition: " + conditionToUpdate.getConditionName(), "", LocalDateTime.now(), ActivityStatus.SUCCESS));
        return new CustomResponse(conditionToUpdate.getId().intValue(), "Condition updated successfully", "CONDITION_UPDATED", Status.SUCCESS);
    }

    public CustomResponse checkConditionNameExists(UserAuthorize userAuthorize, String conditionName) {
        Optional<MedicalCondition> existingCondition = medicalConditionRepository.findByConditionName(conditionName);

        if (existingCondition.isPresent() && existingCondition.get().getStatus() == ConditionStatus.ACTIVE) {
            return new CustomResponse(1, "Condition name exists", "CONDITION_EXISTS", Status.SUCCESS);
        }

        return new CustomResponse(0, "Condition name does not exist", "CONDITION_NOT_FOUND", Status.FAILED);
    }


    public CustomResponse deleteCondition(UserAuthorize userAuthorize, Long conditionId) {
        Optional<MedicalCondition> existingCondition = medicalConditionRepository.findById(conditionId);

        if (existingCondition.isEmpty()) {
            return new CustomResponse(0, "Condition not found", "CONDITION_NOT_FOUND", Status.FAILED);
        }

        MedicalCondition conditionToDelete = existingCondition.get();
        conditionToDelete.setStatus(ConditionStatus.INACTIVE);
        medicalConditionRepository.save(conditionToDelete);

        userActivityRepository.save(new UserActivity(userAuthorize.getUserId(), "Delete Condition", "Delete Condition: " + conditionToDelete.getConditionName(), "", LocalDateTime.now(), ActivityStatus.SUCCESS));
        return new CustomResponse(conditionId.intValue(), "Condition marked as INACTIVE", "CONDITION_INACTIVE", Status.SUCCESS);
    }
}
