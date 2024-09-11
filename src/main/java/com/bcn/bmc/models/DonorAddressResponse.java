package com.bcn.bmc.models;

public class DonorAddressResponse {

    private String status;
    private String message;
    private Long donorId;

    public DonorAddressResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public DonorAddressResponse(String status, String message, Long donorId) {
        this.status = status;
        this.message = message;
        this.donorId = donorId;
    }

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

    public Long getDonorId() {
        return donorId;
    }

    public void setDonorId(Long donorId) {
        this.donorId = donorId;
    }
}
