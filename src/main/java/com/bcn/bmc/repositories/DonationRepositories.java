package com.bcn.bmc.repositories;

import com.bcn.bmc.models.Donation;
import com.bcn.bmc.models.DonorAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DonationRepositories  extends JpaRepository<Donation, Long> {

    @Query("select da from Donation da where da.donor = ?1")
    List<Donation> findDonationByDonorId(@Param("donor") Long donor);
}
