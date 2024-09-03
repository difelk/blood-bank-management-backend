package com.bcn.bmc.models;

public class HospitalResponse {

    String Message;
    Integer status;

    public HospitalResponse(Integer status,String Message){
        this.Message = Message;
        this.status = status;

    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
