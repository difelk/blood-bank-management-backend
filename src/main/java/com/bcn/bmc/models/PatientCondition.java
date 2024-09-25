package com.bcn.bmc.models;

import jakarta.persistence.*;

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

    public PatientCondition() {}

    public PatientCondition(long patient, long condition, int bloodDonationCount) {
        this.patient = patient;
        this.condition = condition;
        this.bloodDonationCount = bloodDonationCount;
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
