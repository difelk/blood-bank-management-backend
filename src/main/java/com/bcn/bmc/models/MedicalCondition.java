package com.bcn.bmc.models;

import com.bcn.bmc.enums.ConditionStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "medical_conditions")
public class MedicalCondition {

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConditionName() {
        return conditionName;
    }

    public void setConditionName(String conditionName) {
        this.conditionName = conditionName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "condition_name")
    private String conditionName;
    @Column(name = "description")
    private String description;

    public ConditionStatus getStatus() {
        return status;
    }

    public void setStatus(ConditionStatus status) {
        this.status = status;
    }

    @Column(name = "symptoms")
    private String symptoms;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ConditionStatus status = ConditionStatus.ACTIVE;


}
