package com.bcn.bmc.repositories;

import com.bcn.bmc.models.StockSend;
import com.bcn.bmc.models.StockTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockSendRepository extends JpaRepository<StockSend, Long> {
}
