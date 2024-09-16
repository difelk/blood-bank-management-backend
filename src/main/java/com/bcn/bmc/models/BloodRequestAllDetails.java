package com.bcn.bmc.models;

import com.bcn.bmc.enums.FulfillmentStatus;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class BloodRequestAllDetails {

    private long id;

    private KeyValue hospital;


    private FulfillmentStatus status;

    public long getId() {
        return id;
    }
    public BloodRequestAllDetails(){}
    public BloodRequestAllDetails(long id, KeyValue hospital, FulfillmentStatus status, LocalDateTime requestedDate, LocalDateTime receiveDate, List<BloodKeyValue> bloodGroups) {
        this.id = id;
        this.hospital = hospital;
        this.status = status;
        this.requestedDate = requestedDate;
        this.receiveDate = receiveDate;
        this.bloodGroups = bloodGroups;
    }

    public void setId(long id) {
        this.id = id;
    }

    public KeyValue getHospital() {
        return hospital;
    }

    public void setHospital(KeyValue hospital) {
        this.hospital = hospital;
    }

    public FulfillmentStatus getStatus() {
        return status;
    }

    public void setStatus(FulfillmentStatus status) {
        this.status = status;
    }

    public LocalDateTime getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(LocalDateTime requestedDate) {
        this.requestedDate = requestedDate;
    }

    public LocalDateTime getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(LocalDateTime receiveDate) {
        this.receiveDate = receiveDate;
    }

    public List<BloodKeyValue> getBloodGroups() {
        return bloodGroups;
    }

    public void setBloodGroups(List<BloodKeyValue> bloodGroups) {
        this.bloodGroups = bloodGroups;
    }

    private LocalDateTime requestedDate;

    private LocalDateTime receiveDate;

    private List<BloodKeyValue> bloodGroups;




}
