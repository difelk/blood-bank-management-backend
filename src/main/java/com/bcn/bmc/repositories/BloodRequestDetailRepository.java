package com.bcn.bmc.repositories;

import com.bcn.bmc.models.BloodRequest;
import com.bcn.bmc.models.BloodRequestDetail;
import com.bcn.bmc.models.Organization;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BloodRequestDetailRepository  extends JpaRepository<BloodRequestDetail, String> {

    @Query("select brd from BloodRequestDetail brd where brd.bloodRequest = ?1")
    List<BloodRequestDetail> findByBloodRequestId(@Param("id") long id);

    @Query("select brd from BloodRequestDetail brd WHERE brd.id = :id AND brd.bloodType = :bloodType")
    BloodRequestDetail getDetailsByIdAndBloodType(@Param("id") long id, @Param("bloodType") String type);

    @Modifying
    @Transactional
    @Query("UPDATE BloodRequestDetail brd SET brd.quantity = :newQty WHERE brd.id = :id AND brd.bloodType = :bloodType")
    int updateDetailsByIdAndBloodType(@Param("id") long id, @Param("bloodType") String type, @Param("newQty") double newQty);

}
