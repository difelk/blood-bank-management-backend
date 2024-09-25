package com.bcn.bmc.repositories;

import com.bcn.bmc.models.PatientCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientConditionRepository  extends JpaRepository<PatientCondition, Long> {

    @Query("SELECT pc FROM PatientCondition pc WHERE pc.patient = :id")
    List<PatientCondition> findByPatientId(@Param("id") long id);


}
