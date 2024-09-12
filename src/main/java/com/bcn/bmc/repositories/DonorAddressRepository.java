package com.bcn.bmc.repositories;

import com.bcn.bmc.models.Address;
import com.bcn.bmc.models.Donor;
import com.bcn.bmc.models.DonorAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DonorAddressRepository  extends JpaRepository<DonorAddress, Long> {
    @Query("select da from DonorAddress da where da.donorId = ?1")
    DonorAddress findDonorAddressByUserId(@Param("donorId") Long donorId);
}
