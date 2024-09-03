package com.bcn.bmc.repositories;

import com.bcn.bmc.models.HospitalDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HospitalDocumentRepository extends JpaRepository<HospitalDocument, Long> {
    List<HospitalDocument> findByHospitalId(Long hospitalId);
}