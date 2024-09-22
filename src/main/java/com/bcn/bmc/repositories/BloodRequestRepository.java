package com.bcn.bmc.repositories;

import com.bcn.bmc.enums.FulfillmentStatus;
import com.bcn.bmc.models.BloodRequest;
import com.bcn.bmc.models.BloodRequestDetail;
import com.bcn.bmc.models.BloodType;
import com.bcn.bmc.models.Donation;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BloodRequestRepository  extends JpaRepository<BloodRequest, String> {

    @Query("select bt from BloodRequest bt where  bt.requestorOrganizationId = :organizationId")
    List<BloodRequest> findAllByOrgId(@Param("organizationId") long organizationId);

    @Query("select bt from BloodRequest bt where  bt.providerOrganizationId = :providerId")
    List<BloodRequest> findAllByProviderId(@Param("providerId") long providerId);

    @Modifying
    @Transactional
    @Query("UPDATE BloodRequestDetail brd SET brd.quantity = :newQty WHERE brd.id = :id AND brd.bloodType = :bloodType")
    BloodRequestDetail updateDetailsByIdAndBloodType(@Param("id") long id, @Param("bloodType") String type, @Param("newQty") double newQty);


    @Modifying
    @Transactional
    @Query("UPDATE BloodRequest br SET br.fulfillmentStatus = :status WHERE br.id = :id")
    int deleteByRequestId(@Param("id") long id, @Param("status") FulfillmentStatus status);



}
