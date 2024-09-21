package com.bcn.bmc.repositories;

import com.bcn.bmc.models.Address;
import com.bcn.bmc.models.Donor;
import com.bcn.bmc.models.DonorAddress;
import com.bcn.bmc.models.HospitalAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DonorAddressRepository  extends JpaRepository<DonorAddress, Long> {
    @Query("select da from DonorAddress da where da.donorId = ?1")
    DonorAddress findDonorAddressByUserId(@Param("donorId") Long donorId);


    @Query("select d from DonorAddress d where d.donorId = ?1")
    Optional<DonorAddress> findAddressByDonorId(@Param("donorId") Long donorId);
}
