package com.bcn.bmc.repositories;

import com.bcn.bmc.models.BloodType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BloodTypeRepository extends JpaRepository<BloodType, String> {
}
