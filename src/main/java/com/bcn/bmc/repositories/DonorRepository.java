package com.bcn.bmc.repositories;

import com.bcn.bmc.models.BloodType;
import com.bcn.bmc.models.Donor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DonorRepository  extends JpaRepository<Donor, Long> {

    @Query("select d from Donor d where d.nic =?1")
    Optional<Donor> findDonorByNic(@Param("nic") String nic);
}
