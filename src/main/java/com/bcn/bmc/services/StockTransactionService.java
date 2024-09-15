package com.bcn.bmc.services;

import com.bcn.bmc.models.StockTransaction;
import com.bcn.bmc.repositories.StockTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockTransactionService {


   private final StockTransactionRepository stockTransactionRepository;


    @Autowired
    public StockTransactionService(StockTransactionRepository stockTransactionRepository){
        this.stockTransactionRepository = stockTransactionRepository;
    }



    public StockTransaction createStockTransaction(StockTransaction stockTransaction){
        try {
         return  stockTransactionRepository.save(stockTransaction);
        }catch (Exception e){
            return  null;
        }
    }

}
