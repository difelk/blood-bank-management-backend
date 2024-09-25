package com.bcn.bmc.models;

import java.time.LocalDateTime;

public class PatientConditionsHistory {


    private long patientHistoryId;

    private LocalDateTime patientHistoryDate;

    private KeyValue patient;

    private KeyValue organization;


    private String PatientBloodType;


    private KeyValue patientCondition;

    private double donatedQty;

    public PatientConditionsHistory() {
    }

    public long getPatientHistoryId() {
        return patientHistoryId;
    }

    public void setPatientHistoryId(long patientHistoryId) {
        this.patientHistoryId = patientHistoryId;
    }

    public LocalDateTime getPatientHistoryDate() {
        return patientHistoryDate;
    }

    public void setPatientHistoryDate(LocalDateTime patientHistoryDate) {
        this.patientHistoryDate = patientHistoryDate;
    }

    public KeyValue getPatient() {
        return patient;
    }

    public void setPatient(KeyValue patient) {
        this.patient = patient;
    }

    public KeyValue getOrganization() {
        return organization;
    }

    public void setOrganization(KeyValue organization) {
        this.organization = organization;
    }

    public String getPatientBloodType() {
        return PatientBloodType;
    }

    public void setPatientBloodType(String patientBloodType) {
        PatientBloodType = patientBloodType;
    }

    public KeyValue getPatientCondition() {
        return patientCondition;
    }

    public void setPatientCondition(KeyValue patientCondition) {
        this.patientCondition = patientCondition;
    }

    public double getDonatedQty() {
        return donatedQty;
    }

    public void setDonatedQty(double donatedQty) {
        this.donatedQty = donatedQty;
    }

    public PatientConditionsHistory(long patientHistoryId, LocalDateTime patientHistoryDate,
                                    KeyValue patient, KeyValue organization,
                                    String PatientBloodType, KeyValue patientCondition, double donatedQty) {
        this.patientHistoryId = patientHistoryId;
        this.patientHistoryDate = patientHistoryDate;
        this.patient = patient;
        this.organization = organization;
        this.PatientBloodType = PatientBloodType;
        this.patientCondition = patientCondition;
        this.donatedQty = donatedQty;
    }

}
