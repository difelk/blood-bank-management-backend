package com.bcn.bmc.models;

import com.bcn.bmc.enums.TransactionType;

public class StockHistory {

    private Long stockId;

    private TransactionType type;

    private double qty;

    private String sourceOrganization;

    private String destinationOrganization;

    private String donor;

    private String patient;

    public StockHistory(){

    }
    public StockHistory(Long stockId, TransactionType type, double qty, String sourceOrganization, String destinationOrganization, String donor, String patient) {
        this.stockId = stockId;
        this.type = type;
        this.qty = qty;
        this.sourceOrganization = sourceOrganization;
        this.destinationOrganization = destinationOrganization;
        this.donor = donor;
        this.patient = patient;
    }

    public Long getStockId() {
        return stockId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public String getSourceOrganization() {
        return sourceOrganization;
    }

    public void setSourceOrganization(String sourceOrganization) {
        this.sourceOrganization = sourceOrganization;
    }

    public String getDestinationOrganization() {
        return destinationOrganization;
    }

    public void setDestinationOrganization(String destinationOrganization) {
        this.destinationOrganization = destinationOrganization;
    }

    public String getDonor() {
        return donor;
    }

    public void setDonor(String donor) {
        this.donor = donor;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }
}
