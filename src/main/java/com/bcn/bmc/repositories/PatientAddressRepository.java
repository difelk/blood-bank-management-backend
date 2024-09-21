package com.bcn.bmc.repositories;

import com.bcn.bmc.models.HospitalAddress;
import com.bcn.bmc.models.PatientAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientAddressRepository extends JpaRepository<PatientAddress, Long> {
    List<PatientAddress> findByPatientId(Long patientId);

    @Query("select p from PatientAddress p where p.patientId = ?1")
    Optional<PatientAddress> findAddressByPatientId(@Param("patientId") Long patientId);

    List<PatientAddress> findAllByPatientId(Long patientId);
}
