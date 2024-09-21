package com.bcn.bmc.models;

public class PatientAddressResponse {

    private String status;
    private String message;
    private Long patientId;

    public PatientAddressResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public PatientAddressResponse(String status, String message, Long patientId) {
        this.status = status;
        this.message = message;
        this.patientId = patientId;
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

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }
}
