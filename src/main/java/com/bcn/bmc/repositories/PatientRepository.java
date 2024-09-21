package com.bcn.bmc.repositories;

import com.bcn.bmc.enums.ActiveStatus;
import com.bcn.bmc.models.Donor;
import com.bcn.bmc.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByNic(String nic);
    boolean existsByNic(String nic);

    @Query("select p from Patient p where p.nic =?1")
    Optional<Patient> findPatientByNic(@Param("nic") String nic);

    List<Patient> findAllByStatus(ActiveStatus status);
}