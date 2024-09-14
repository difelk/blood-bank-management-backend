package com.bcn.bmc.repositories;

import com.bcn.bmc.models.Stock;
import com.bcn.bmc.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StockRepository  extends JpaRepository<Stock, Long> {

    @Query("select s from Stock s where  s.organizationId = :organization AND s.bloodType = :bloodType")
    Optional<Stock> findByOrganizationIdAndBloodType(@Param("organization") long organization, @Param("bloodType") String bloodType);

}
