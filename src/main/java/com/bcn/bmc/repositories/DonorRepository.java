package com.bcn.bmc.repositories;

import com.bcn.bmc.models.BloodType;
import com.bcn.bmc.models.Donor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonorRepository  extends JpaRepository<Donor, Long> {
}
