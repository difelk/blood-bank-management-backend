package com.bcn.bmc.repositories;

import com.bcn.bmc.models.PatientDocuments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientDocumentRepository extends JpaRepository<PatientDocuments, Long> {
    List<PatientDocuments> findByPatientId(Long patientId);
}

