package com.bcn.bmc.repositories;

import com.bcn.bmc.models.BloodRequest;
import com.bcn.bmc.models.BloodRequestDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BloodRequestDetailRepository  extends JpaRepository<BloodRequestDetail, String> {
}
