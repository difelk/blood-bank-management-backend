package com.bcn.bmc.repositories;

import com.bcn.bmc.models.Address;
import com.bcn.bmc.models.HospitalAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HospitalAddressRepository extends JpaRepository<HospitalAddress, Long> {
    List<HospitalAddress> findByHospitalId(Long hospitalId);

    @Query("select h from HospitalAddress h where h.hospitalId = ?1")
    Optional<HospitalAddress> findAddressByHospitalId(@Param("hospitalId") Long hospitalId);
}