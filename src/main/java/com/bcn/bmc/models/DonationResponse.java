package com.bcn.bmc.models;

public class DonationResponse {

    private String status;
    private String message;
    private Long donationId;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getDonationId() {
        return donationId;
    }

    public void setDonationId(Long donationId) {
        this.donationId = donationId;
    }

    public Long getDonorId() {
        return donorId;
    }

    public void setDonorId(Long donorId) {
        this.donorId = donorId;
    }

    public Long getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(Long hospitalId) {
        this.hospitalId = hospitalId;
    }

    private Long donorId;
    private Long hospitalId;

    public DonationResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public DonationResponse(String status, String message, Long donationId) {
        this.status = status;
        this.message = message;
        this.donationId = donationId;
    }

    public DonationResponse(String status, String message, Long donationId, Long donorId, Long hospitalId) {
        this.status = status;
        this.message = message;
        this.donationId = donationId;
        this.donorId = donorId;
        this.hospitalId = hospitalId;
    }

}
