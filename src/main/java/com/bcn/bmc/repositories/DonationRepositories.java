package com.bcn.bmc.repositories;

import com.bcn.bmc.models.Donation;
import com.bcn.bmc.models.DonorAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface DonationRepositories  extends JpaRepository<Donation, Long> {

    @Query("select da from Donation da where da.status <> 'INACTIVE'")
    List<Donation> findAllDonations();


    @Query("select da from Donation da where  da.organizationId = :organizationId AND da.status <> 'INACTIVE'")
    List<Donation> findAllDonationsByOrg(@Param("organizationId") long organizationId);

    @Query("select da from Donation da where da.donor = ?1")
    List<Donation> findDonationByDonorId(@Param("donor") Long donor);

    @Query("select da from Donation da where da.donor = :donor AND da.organizationId = :organizationId AND da.status <> 'INACTIVE'")
    List<Donation> findDonationByDonorIdWithinOrg(@Param("donor") Long donor, @Param("organizationId") long organizationId);
    @Query("SELECT da FROM Donation da WHERE da.donor = :donorId ORDER BY da.donationDate DESC")
    Optional<Donation> findLastDonationByDonor(@Param("donorId") Long donorId);

    List<Donation> findByDonor(Long donorId);


    List<Donation> findByDonationDateBetween(Date donationFrom, Date donationTo);

}
