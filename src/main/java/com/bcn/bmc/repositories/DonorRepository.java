package com.bcn.bmc.repositories;

import com.bcn.bmc.enums.ActiveStatus;
import com.bcn.bmc.models.Donor;
import com.bcn.bmc.models.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DonorRepository  extends JpaRepository<Donor, Long> {

    @Query("select d from Donor d where d.nic =?1")
    Optional<Donor> findDonorByNic(@Param("nic") String nic);

    List<Donor> findAllByStatus(ActiveStatus status);

    @Query("select d from Donor d where  d.id = :organization AND d.status <> 'INACTIVE'")
    List<Donor> findAllByOrganizationId(@Param("organization") int organization);
}
