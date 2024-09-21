package com.bcn.bmc.models;

import com.bcn.bmc.enums.ActiveStatus;
import com.bcn.bmc.enums.Gender;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "patients")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nic")
    private String nic;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "gender", nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "blood_type", nullable = false)
    private String bloodType;

    @Column(name = "dob")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dob;

    @Column(name = "contact_number", nullable = false)
    private String contactNo;

    @Column(name = "created_date", nullable = false)
    private Date date;

    @Column(name = "emergency_contact_person_name", nullable = false)
    private String emergencyContactPersonName;

    @Column(name = "emergency_contact_person_contact_no", nullable = false)
    private String emergencyContactPersonContactNo;

    @Column(name = "created_by", nullable = true)
    private Long createdBy;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ActiveStatus status = ActiveStatus.ACTIVE;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getEmergencyContactPersonName() {
        return emergencyContactPersonName;
    }

    public void setEmergencyContactPersonName(String emergencyContactPersonName) {
        this.emergencyContactPersonName = emergencyContactPersonName;
    }

    public String getEmergencyContactPersonContactNo() {
        return emergencyContactPersonContactNo;
    }

    public void setEmergencyContactPersonContactNo(String emergencyContactPersonContactNo) {
        this.emergencyContactPersonContactNo = emergencyContactPersonContactNo;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public ActiveStatus getStatus() {
        return status;
    }

    public void setStatus(ActiveStatus status) {
        this.status = status;
    }
}
