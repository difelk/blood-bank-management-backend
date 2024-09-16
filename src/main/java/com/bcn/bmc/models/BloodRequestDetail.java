package com.bcn.bmc.models;

import jakarta.persistence.*;

@Entity
@Table(name = "blood_request_details")
public class BloodRequestDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "blood_request_id", nullable = false)
    private Long bloodRequest;

    @Column(name = "blood_type", nullable = false)
    private String bloodType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public BloodRequestDetail(){}
    public BloodRequestDetail(Long bloodRequest, String bloodType, double quantity) {
        this.bloodRequest = bloodRequest;
        this.bloodType = bloodType;
        this.quantity = quantity;
    }

    public Long getBloodRequest() {
        return bloodRequest;
    }

    public void setBloodRequest(Long bloodRequest) {
        this.bloodRequest = bloodRequest;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    @Column(name = "quantity", nullable = false)
    private double quantity;


}
