package com.bcn.bmc.repositories;

import com.bcn.bmc.models.HospitalAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HospitalAddressRepository extends JpaRepository<HospitalAddress, Long> {
    List<HospitalAddress> findByHospitalId(Long hospitalId);
}