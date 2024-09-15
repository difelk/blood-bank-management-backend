package com.bcn.bmc.models;

import com.bcn.bmc.enums.TransactionType;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "stock_transactions")
public class StockTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stock_id", nullable = false)
    private Long stockId;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType;

    @Column(name = "quantity", nullable = false)
    private Double quantity;

    @Column(name = "transaction_date", nullable = false)
    private Date transactionDate;

    @Column(name = "donor_id", nullable = true)
    private Long  donorId;


    @Column(name = "source_organization_id", nullable = true)
    private Long  sourceOrganizationId;

    @Column(name = "destination_organization_id", nullable = true)
    private Long  destinationOrganizationId;


    public StockTransaction() {
    }


    public StockTransaction(Long stockId, TransactionType transactionType, Double quantity, Date transactionDate,
                            Long  donorId, Long  sourceOrganizationId, Long  destinationOrganizationId, Long  patientId) {
        this.stockId = stockId;
        this.transactionType = transactionType;
        this.quantity = quantity;
        this.transactionDate = transactionDate;
        this.donorId = donorId;
        this.sourceOrganizationId = sourceOrganizationId;
        this.destinationOrganizationId = destinationOrganizationId;
        this.patientId = patientId;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStockId() {
        return stockId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Long  getDonorId() {
        return donorId;
    }

    public void setDonorId(Long  donorId) {
        this.donorId = donorId;
    }

    public Long  getSourceOrganizationId() {
        return sourceOrganizationId;
    }

    public void setSourceOrganizationId(Long  sourceOrganizationId) {
        this.sourceOrganizationId = sourceOrganizationId;
    }

    public Long  getDestinationOrganizationId() {
        return destinationOrganizationId;
    }

    public void setDestinationOrganizationId(Long  destinationOrganizationId) {
        this.destinationOrganizationId = destinationOrganizationId;
    }

    public Long  getPatientId() {
        return patientId;
    }

    public void setPatientId(Long  patientId) {
        this.patientId = patientId;
    }

    @Column(name = "patient_id", nullable = true)
    private Long  patientId;
}
