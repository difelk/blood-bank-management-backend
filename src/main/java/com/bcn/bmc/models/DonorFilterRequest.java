package com.bcn.bmc.models;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class DonorFilterRequest {

    private List<String> bloodTypes;
    private Date donorsAreFrom;
    private Date donorsAreTo;
    private String donorsCount;
    private boolean eligibleDonors;
    private String textMessage;

    public List<String> getBloodTypes() {
        return bloodTypes;
    }

    public void setBloodTypes(List<String> bloodTypes) {
        this.bloodTypes = bloodTypes;
    }

    public Date getDonorsAreFrom() {
        return donorsAreFrom;
    }

    public void setDonorsAreFrom(Date donorsAreFrom) {
        this.donorsAreFrom = donorsAreFrom;
    }

    public Date getDonorsAreTo() {
        return donorsAreTo;
    }

    public void setDonorsAreTo(Date donorsAreTo) {
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
