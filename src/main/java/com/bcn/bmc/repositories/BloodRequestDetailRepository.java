package com.bcn.bmc.repositories;

import com.bcn.bmc.models.BloodRequest;
import com.bcn.bmc.models.BloodRequestDetail;
import com.bcn.bmc.models.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BloodRequestDetailRepository  extends JpaRepository<BloodRequestDetail, String> {

    @Query("select brd from BloodRequestDetail brd where brd.bloodRequest = ?1")
    List<BloodRequestDetail> findByBloodRequestId(@Param("id") long id);
}
