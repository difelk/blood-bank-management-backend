package com.bcn.bmc.repositories;

import com.bcn.bmc.models.BloodType;
import com.bcn.bmc.models.Donation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BloodTypeRepository extends JpaRepository<BloodType, String> {


}
