package com.bcn.bmc.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "blood_types")
public class BloodType {

    @Id
    @Column(name = "blood_type")
    private String bloodType;

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public com.bcn.bmc.enums.BloodType getBloodTypeEnum() {
        return com.bcn.bmc.enums.BloodType.fromDisplayName(bloodType);
    }

    public void setBloodTypeEnum(com.bcn.bmc.enums.BloodType bloodType) {
        this.bloodType = bloodType.getDisplayName();
    }
}
