package com.bcn.bmc.models;

import java.util.List;

public class HospitalDetails {

    private Hospital hospital;

    private HospitalAddress hospitalAddress;

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    public HospitalAddress getHospitalAddress() {
        return hospitalAddress;
    }

    public void setHospitalAddress(HospitalAddress hospitalAddress) {
        this.hospitalAddress = hospitalAddress;
    }

    public List<HospitalDocument> getHospitalDocument() {
        return hospitalDocument;
    }

    public HospitalDetails(){}
    public HospitalDetails(Hospital hospital, HospitalAddress hospitalAddress, List<HospitalDocument> hospitalDocument, List<DonationDetails> hospitalDonation, List<Stock> hospitalStock){
        this.hospital = hospital;
        this.hospitalAddress = hospitalAddress;
        this.hospitalDocument = hospitalDocument;
        this.hospitalDonation = hospitalDonation;
        this.hospitalStock = hospitalStock;
    }

    public void setHospitalDocument(List<HospitalDocument> hospitalDocument) {
        this.hospitalDocument = hospitalDocument;
    }

    public List<Stock> getHospitalStock() {
        return hospitalStock;
    }

    public void setHospitalStock(List<Stock> hospitalStock) {
        this.hospitalStock = hospitalStock;
    }

    public List<DonationDetails> getHospitalDonation() {
        return hospitalDonation;
    }

    public void setHospitalDonation(List<DonationDetails> hospitalDonation) {
        this.hospitalDonation = hospitalDonation;
    }

    private List<HospitalDocument> hospitalDocument;

    private List<Stock> hospitalStock;

    private List<DonationDetails> hospitalDonation;
}
