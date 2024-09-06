package com.bcn.bmc.models;

import jakarta.persistence.*;

import java.util.List;

public class HospitalJoinedDetails {

    private Long id;

    private String hospitalName;

    private String contactNo1;

    private String contactNo2;

    private String sector;

    private Long hospitalId;

    private String streetNumber;

    private String streetName;

    private String city;

    private List<HospitalDocument> hospitalDocuments;

    public HospitalJoinedDetails(){

    }
    public HospitalJoinedDetails(Long id, String hospitalName, String contactNo1, String contactNo2, String sector,
                                 Long hospitalId, String streetNumber, String streetName, String city,
                                 List<HospitalDocument> hospitalDocuments) {
        this.id = id;
        this.hospitalName = hospitalName;
        this.contactNo1 = contactNo1;
        this.contactNo2 = contactNo2;
        this.sector = sector;
        this.hospitalId = hospitalId;
        this.streetNumber = streetNumber;
        this.streetName = streetName;
        this.city = city;
        this.hospitalDocuments = hospitalDocuments;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getContactNo1() {
        return contactNo1;
    }

    public void setContactNo1(String contactNo1) {
        this.contactNo1 = contactNo1;
    }

    public String getContactNo2() {
        return contactNo2;
    }

    public void setContactNo2(String contactNo2) {
        this.contactNo2 = contactNo2;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public Long getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(Long hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<HospitalDocument> getHospitalDocuments() {
        return hospitalDocuments;
    }

    public void setHospitalDocuments(List<HospitalDocument> hospitalDocuments) {
        this.hospitalDocuments = hospitalDocuments;
    }
}
