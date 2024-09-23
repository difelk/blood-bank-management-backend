package com.bcn.bmc.models;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class DonorFilterResponse {
    private List<Donor> filteredDonors;
    private List<String> bloodTypes;
    private LocalDate donorsAreFrom;
    private LocalDate donorsAreTo;
    private String donorsCount;
    private boolean eligibleDonors;
    private String textMessage;


    public DonorFilterResponse(List<Donor> filteredDonors, List<String> bloodTypes,
                               LocalDate donorsAreFrom, LocalDate donorsAreTo,
                               String donorsCount, boolean eligibleDonors,
                               String textMessage) {
        this.filteredDonors = filteredDonors;
        this.bloodTypes = bloodTypes;
        this.donorsAreFrom = donorsAreFrom;
        this.donorsAreTo = donorsAreTo;
        this.donorsCount = donorsCount;
        this.eligibleDonors = eligibleDonors;
        this.textMessage = textMessage;
    }

    public List<Donor> getFilteredDonors() {
        return filteredDonors;
    }

    public void setFilteredDonors(List<Donor> filteredDonors) {
        this.filteredDonors = filteredDonors;
    }

    public List<String> getBloodTypes() {
        return bloodTypes;
    }

    public void setBloodTypes(List<String> bloodTypes) {
        this.bloodTypes = bloodTypes;
    }

    public LocalDate getDonorsAreFrom() {
        return donorsAreFrom;
    }

    public void setDonorsAreFrom(LocalDate donorsAreFrom) {
        this.donorsAreFrom = donorsAreFrom;
    }

    public LocalDate getDonorsAreTo() {
        return donorsAreTo;
    }

    public void setDonorsAreTo(LocalDate donorsAreTo) {
        this.donorsAreTo = donorsAreTo;
    }

    public String getDonorsCount() {
        return donorsCount;
    }

    public void setDonorsCount(String donorsCount) {
        this.donorsCount = donorsCount;
    }

    public boolean isEligibleDonors() {
        return eligibleDonors;
    }

    public void setEligibleDonors(boolean eligibleDonors) {
        this.eligibleDonors = eligibleDonors;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }
}

