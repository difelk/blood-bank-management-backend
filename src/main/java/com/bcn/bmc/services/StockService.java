package com.bcn.bmc.services;

import com.bcn.bmc.models.Stock;
import com.bcn.bmc.models.UserAuthorize;
import com.bcn.bmc.repositories.StockRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
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



    public void updateStock(Long organizationId, String bloodType, Double quantityDifference) {
        System.out.println("updateStock called");
        try {
        Optional<Stock> stockOptional = getStockByOrganizationAndBloodType(organizationId, bloodType);
        System.out.println("is there a stock for this ? " + stockOptional.isPresent());
        if (stockOptional.isPresent()) {
            Stock stock = stockOptional.get();
            System.out.println("current stock  ? " + stock.getQuantity());
            System.out.println("new qty for  stock  ? " + quantityDifference);
            double newQuantity = stock.getQuantity() + quantityDifference;
            if (newQuantity < 0) {
                throw new IllegalArgumentException("Stock cannot be negative.");
            }
            stock.setQuantity(newQuantity);
            stock.setLastUpdated(new Date());
            stockRepository.save(stock);
        } else {
            if (quantityDifference < 0) {
                throw new IllegalArgumentException("Stock cannot be negative for new entries.");
            }
            Stock newStock = new Stock(organizationId, bloodType, quantityDifference);
            stockRepository.save(newStock);
        }
        }catch (Exception e){
            System.out.println("STOCK UPDATE ERROR: " + e.getMessage());
        }
    }


    public List<Stock> getAllStock(long id){
//        if((admin.getOrganization() == 1)){
//        return stockRepository.findAll();
//        }else{
            return stockRepository.findStocksByOrganization(id);
//        }

    }


}
