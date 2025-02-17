package com.bcn.bmc.repositories;

import com.bcn.bmc.models.Stock;
import com.bcn.bmc.models.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StockRepository  extends JpaRepository<Stock, Long> {

    @Query("select s from Stock s where  s.organizationId = :organization AND s.bloodType = :bloodType")
    Optional<Stock> findByOrganizationIdAndBloodType(@Param("organization") long organization, @Param("bloodType") String bloodType);


//    @Query("select s from Stock s where  s.organizationId = :organization AND s.bloodType = :bloodType")
//    List<Stock> findStockForBloodTypeByOrganizationId(@Param("organization") long organization, @Param("bloodType") String bloodType);

    @Query("select s from Stock s where  s.organizationId = :organization")
    List<Stock> findStocksByOrganization(@Param("organization") long organization);


    @Query("select s from Stock s where s.organizationId != :organization")
    List<Stock> findStockByBloodTypeFromOutsideOrg(@Param("organization") long organization);

    @Query("select s from Stock s where s.organizationId = :organization AND s.bloodType = :bloodType")
    Stock findStocksByOrganizationAndBloodType(@Param("organization") long organization, @Param("bloodType") String bloodType);

    @Modifying
    @Transactional
    @Query("UPDATE Stock s SET s.quantity = :newQuantity WHERE s.organizationId = :organization AND s.bloodType = :bloodType")
    int updateStockQuantityByOrganizationAndBloodType(@Param("organization") long organization, @Param("bloodType") String bloodType, @Param("newQuantity") double newQuantity);


}
