package com.bcn.bmc.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "patient_conditions")
public class PatientCondition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "patient_id", nullable = false)
    private long patient;


    @Column(name = "condition_id", nullable = false)
    private long condition;



    @Column(name = "blood_donation_count", nullable = false)
    private int bloodDonationCount = 0;


    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "organization_id", nullable = false)
    private long organization;

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public PatientCondition() {}

    public long getOrganization() {
        return organization;
    }

    public void setOrganization(long organization) {
        this.organization = organization;
    }

    public PatientCondition(long patient, long condition, int bloodDonationCount, LocalDateTime date, long organization) {
        this.patient = patient;
        this.condition = condition;
        this.bloodDonationCount = bloodDonationCount;
        this.date = date;
        this.organization = organization;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public long getPatient() {
        return patient;
    }

    public long getCondition() {
        return condition;
    }

    public void setPatient(long patient) {
        this.patient = patient;
    }



    public void setCondition(long condition) {
        this.condition = condition;
    }

    public int getBloodDonationCount() {
        return bloodDonationCount;
    }

    public void setBloodDonationCount(int bloodDonationCount) {
        this.bloodDonationCount = bloodDonationCount;
    }
}
