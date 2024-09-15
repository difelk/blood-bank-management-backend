package com.bcn.bmc.repositories;

import com.bcn.bmc.models.Donation;
import com.bcn.bmc.models.StockTransaction;
import com.bcn.bmc.models.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StockTransactionRepository  extends JpaRepository<StockTransaction, Long> {

    @Query("select st from StockTransaction st where  st.sourceOrganizationId = :organizationId")
    List<StockTransaction> findStockTransactionByOrg(@Param("organizationId") long organizationId);


}
