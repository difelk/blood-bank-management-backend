package com.bcn.bmc.repositories;

import com.bcn.bmc.models.BloodRequest;
import com.bcn.bmc.models.BloodType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BloodRequestRepository  extends JpaRepository<BloodRequest, String> {
}
