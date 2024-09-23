package com.bcn.bmc.repositories;

import com.bcn.bmc.models.StockSendDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockSendDetailsRepository extends JpaRepository<StockSendDetail, Long> {
}
