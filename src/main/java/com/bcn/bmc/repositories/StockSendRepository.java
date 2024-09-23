package com.bcn.bmc.repositories;

import com.bcn.bmc.models.BloodRequest;
import com.bcn.bmc.models.StockSend;
import com.bcn.bmc.models.StockTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StockSendRepository extends JpaRepository<StockSend, Long> {


    @Query("select bt from StockSend bt where  bt.bloodRequestId = :id")
   List<StockSend> findByRequestId(@Param("id") long id);
}
