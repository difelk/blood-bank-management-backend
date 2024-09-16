package com.bcn.bmc.repositories;

import com.bcn.bmc.models.BloodRequest;
import com.bcn.bmc.models.BloodType;
import com.bcn.bmc.models.Donation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BloodRequestRepository  extends JpaRepository<BloodRequest, String> {

    @Query("select bt from BloodRequest bt where  bt.requestorOrganizationId = :organizationId")
    List<BloodRequest> findAllByOrgId(@Param("organizationId") long organizationId);


}
