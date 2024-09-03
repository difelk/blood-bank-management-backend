package com.bcn.bmc.models;

public class HospitalAddressResponse {

    private long id;
    private String message;

    public HospitalAddressResponse(long id, String message) {
        this.id = id;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public long getId() {
        return id;
    }
}
