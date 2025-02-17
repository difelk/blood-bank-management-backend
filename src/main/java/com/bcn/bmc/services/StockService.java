package com.bcn.bmc.services;

import com.bcn.bmc.enums.TransactionType;
import com.bcn.bmc.models.*;
import com.bcn.bmc.repositories.HospitalRepository;
import com.bcn.bmc.repositories.StockRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StockService {

    private final StockRepository stockRepository;
    private final StockTransactionService stockTransactionService;

    private final HospitalRepository hospitalRepository;



    public StockService(StockRepository stockRepository, StockTransactionService stockTransactionService, HospitalRepository hospitalRepository) {
        this.stockRepository = stockRepository;
        this.stockTransactionService = stockTransactionService;
        this.hospitalRepository = hospitalRepository;
    }

    public Optional<Stock> getStockByOrganizationAndBloodType(Long organizationId, String bloodType) {
        return stockRepository.findByOrganizationIdAndBloodType(organizationId, bloodType);
    }

    public Stock addStock(Long organizationId, String bloodType, Double quantity, long donorId) {
        try {


            Optional<Stock> stockOptional = getStockByOrganizationAndBloodType(organizationId, bloodType);
            Stock stock;

            if (stockOptional.isPresent()) {
                stock = stockOptional.get();
                stock.setQuantity(stock.getQuantity() + quantity);
                stock.setLastUpdated(new Date());
            } else {
                stock = new Stock(organizationId, bloodType, quantity);
            }
            Stock stock1 = stockRepository.save(stock);
            if (stock1.getId() > 0) {
                stockTransactionService.createStockTransaction(new StockTransaction(stock1.getId(), TransactionType.DONATION, quantity, new Date(), donorId, organizationId, null, null));
            }
            return stock1;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error while adding stock: " + e.getMessage());
        }

    }


    public void updateStock(Long organizationId, String bloodType, Double quantityDifference, Long donorId) {
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
                stockTransactionService.createStockTransaction(new StockTransaction(stock.getId(), TransactionType.CORRECTION, quantityDifference, new Date(), donorId, organizationId, null, null));
            } else {
                if (quantityDifference < 0) {
                    throw new IllegalArgumentException("Stock cannot be negative for new entries.");
                }
                Stock newStock = new Stock(organizationId, bloodType, quantityDifference);
                Stock stock = stockRepository.save(newStock);

                stockTransactionService.createStockTransaction(new StockTransaction(stock.getId(), TransactionType.CORRECTION, quantityDifference, new Date(), donorId, organizationId, null, null));
            }


        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error while adding stock: " + e.getMessage());
        }
    }

    public List<KeyValue> getHospitalsWithMatchingStock(UserAuthorize admin, List<String> bloodTypes, List<Double> quantities) {
        List<Stock> stocks = stockRepository.findStockByBloodTypeFromOutsideOrg(admin.getOrganization());
        List<Hospital> hospitals = hospitalRepository.findAll();
        List<KeyValue> hospitalsWithAvailableQty = new ArrayList<>();

        if (bloodTypes.size() != quantities.size()) {
            throw new IllegalArgumentException("Blood types and quantities must match in size.");
        }

        if (!stocks.isEmpty()) {
            for (Stock stock : stocks) {
                for (int i = 0; i < bloodTypes.size(); i++) {
                    String bloodType = bloodTypes.get(i);
                    double requiredQty = quantities.get(i);

                    System.out.println("bloodTypes from request: " + bloodType + " blood type of stock: " + stock.getBloodType());

                    if (Objects.equals(stock.getBloodType().replace(" ", ""), bloodType.replace(" ", "")) && stock.getQuantity() > requiredQty) {
                        hospitals.stream()
                                .filter(hospital -> Objects.equals(hospital.getId(), stock.getOrganizationId()))
                                .findFirst()
                                .ifPresent(hospital -> hospitalsWithAvailableQty.add(
                                        new KeyValue(stock.getOrganizationId(), hospital.getHospitalName())
                                ));
                    }
                }
            }
        }

        return hospitalsWithAvailableQty;
    }






    public List<Stock> getAllStock(long id) {
//        if((admin.getOrganization() == 1)){
//        return stockRepository.findAll();
//        }else{
        return stockRepository.findStocksByOrganization(id);
//        }

    }


}
