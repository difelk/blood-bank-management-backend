package com.bcn.bmc.services;

import com.bcn.bmc.enums.TransactionType;
import com.bcn.bmc.models.*;
import com.bcn.bmc.repositories.DonorRepository;
import com.bcn.bmc.repositories.HospitalRepository;
import com.bcn.bmc.repositories.StockTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StockTransactionService {


    private final StockTransactionRepository stockTransactionRepository;
    private final DonorRepository donorRepository;

    private final HospitalRepository hospitalRepository;


    public StockTransactionService(StockTransactionRepository stockTransactionRepository, HospitalRepository hospitalRepository, DonorRepository donorRepository) {
        this.stockTransactionRepository = stockTransactionRepository;
        this.donorRepository = donorRepository;
        this.hospitalRepository = hospitalRepository;
    }


    public StockTransaction createStockTransaction(StockTransaction stockTransaction) {
        try {
            return stockTransactionRepository.save(stockTransaction);
        } catch (Exception e) {
            return null;
        }
    }

    public List<StockHistory> getHospitalTransactionHistory(UserAuthorize admin) {
        try {
            List<StockHistory> stockHistories = new ArrayList<>();
            List<StockTransaction> stockTransactions = stockTransactionRepository.findStockTransactionByOrg(admin.getOrganization());

            for (StockTransaction stockTransaction : stockTransactions) {
                String donorNic = "";
                Hospital org = null;
                Hospital des = null;

                // Check if donorId is present
                if (stockTransaction.getDonorId() != null) {
                    Optional<Donor> donor = donorRepository.findById(stockTransaction.getDonorId());
                    if (donor.isPresent()) {
                        donorNic = donor.get().getNic() + " - " + donor.get().getFirstName() + " " + donor.get().getLastName();
                    }
                } else {
                    System.out.println("Donor ID is null for stockTransaction: " + stockTransaction.getStockId());
                }

                // Check if sourceOrganizationId is present
                if (stockTransaction.getSourceOrganizationId() != null) {
                    org = hospitalRepository.findAllByOrganizationIdLong(stockTransaction.getSourceOrganizationId());
                } else {
                    System.out.println("Source Organization ID is null for stockTransaction: " + stockTransaction.getStockId());
                }

                // Check if destinationOrganizationId is present
                if (stockTransaction.getDestinationOrganizationId() != null) {
                    des = hospitalRepository.findAllByOrganizationIdLong(stockTransaction.getDestinationOrganizationId());
                } else {
                    System.out.println("Destination Organization ID is null for stockTransaction: " + stockTransaction.getStockId());
                }

                String orgName = org != null ? org.getHospitalName() : "Unknown Source";
                String desName = des != null ? des.getHospitalName() : "Unknown Destination";

                TransactionType transactionType = stockTransaction.getTransactionType();
                double quantity = stockTransaction.getQuantity();

                stockHistories.add(new StockHistory(stockTransaction.getStockId(), transactionType, quantity, orgName, desName, donorNic, ""));
            }

            return stockHistories;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }



}
