package com.bcn.bmc.models;

import java.util.Date;

public class DonationDetails {

    private long id;
    private KeyValue donor;
    private KeyValue organization;
    private double quantity;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public KeyValue getDonor() {
        return donor;
    }

    public void setDonor(KeyValue donor) {
        this.donor = donor;
    }

    public KeyValue getOrganization() {
        return organization;
    }

    public void setOrganization(KeyValue organization) {
        this.organization = organization;
    }




    public KeyValue getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(KeyValue createdBy) {
        this.createdBy = createdBy;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public Date getDonationDate() {
        return donationDate;
    }



    public void setDonationDate(Date donationDate) {
        this.donationDate = donationDate;
    }

    private KeyValue createdBy;
    private String bloodType;

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    private Date donationDate;

//    public DonationDetails(Long id, KeyValue donatedDonor, KeyValue donatedOrganization, Double quantity, KeyValue donatedHandledUser, String bloodType, Date donationDate) {
//    }

    public DonationDetails(long id, KeyValue donor, KeyValue organization, double quantity, KeyValue createdBy, String bloodType, Date donationDate) {
        this.id = id;
        this.donor = donor;
        this.organization = organization;
        this.quantity = quantity;
        this.createdBy = createdBy;
        this.bloodType = bloodType;
        this.donationDate = donationDate;
    }

    public DonationDetails(long id, KeyValue donor, KeyValue organization, double quantity, String bloodType, Date donationDate) {
        this.id = id;
        this.donor = donor;
        this.organization = organization;
        this.quantity = quantity;
        this.bloodType = bloodType;
        this.donationDate = donationDate;
    }


}
