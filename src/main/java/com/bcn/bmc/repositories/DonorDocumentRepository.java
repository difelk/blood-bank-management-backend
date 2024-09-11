package com.bcn.bmc.repositories;

import com.bcn.bmc.models.DonorDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DonorDocumentRepository extends JpaRepository<DonorDocument, Long> {


    @Query("select dd from DonorDocument dd where dd.donorId = ?1")
    List<DonorDocument> findDonorDocumentsByUserId(@Param("donorId") Long donorId);
}
