package com.bcn.bmc.repositories;

import com.bcn.bmc.models.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long> {
    Optional<Hospital> findByHospitalName(String hospitalName);
    List<Hospital> findBySector(String sector);
}