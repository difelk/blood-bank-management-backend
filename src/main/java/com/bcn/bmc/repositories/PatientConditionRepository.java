package com.bcn.bmc.repositories;

import com.bcn.bmc.models.PatientCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientConditionRepository  extends JpaRepository<PatientCondition, Long> {
}
