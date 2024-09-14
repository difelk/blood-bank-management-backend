package com.bcn.bmc.services;

import com.bcn.bmc.models.Stock;
import com.bcn.bmc.repositories.StockRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class StockService {

    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository){
        this.stockRepository = stockRepository;
    }

    public  Optional<Stock> getStockByOrganizationAndBloodType(Long organizationId, String bloodType) {
        return stockRepository.findByOrganizationIdAndBloodType(organizationId, bloodType);
    }

    public Stock addStock(Long organizationId, String bloodType, Double quantity) {
        Optional<Stock> stockOptional = getStockByOrganizationAndBloodType(organizationId, bloodType);
        Stock stock;

        if (stockOptional.isPresent()) {
            stock = stockOptional.get();
            stock.setQuantity(stock.getQuantity() + quantity);
            stock.setLastUpdated(new Date());
        } else {
            stock = new Stock(organizationId, bloodType, quantity);
        }

        return stockRepository.save(stock);
    }



}
