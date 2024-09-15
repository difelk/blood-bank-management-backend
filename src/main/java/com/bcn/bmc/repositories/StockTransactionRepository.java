package com.bcn.bmc.repositories;

import com.bcn.bmc.models.StockTransaction;
import com.bcn.bmc.models.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockTransactionRepository  extends JpaRepository<StockTransaction, Long> {
}
