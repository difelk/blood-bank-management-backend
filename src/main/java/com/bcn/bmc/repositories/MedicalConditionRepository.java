package com.bcn.bmc.repositories;

import com.bcn.bmc.enums.ConditionStatus;
import com.bcn.bmc.enums.Status;
import com.bcn.bmc.models.MedicalCondition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MedicalConditionRepository  extends JpaRepository<MedicalCondition, Long> {

    Optional<MedicalCondition> findByConditionName(String conditionName);

    List<MedicalCondition> findByStatus(ConditionStatus status);
}
