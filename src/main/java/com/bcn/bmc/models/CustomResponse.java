package com.bcn.bmc.models;

import com.bcn.bmc.enums.Status;

public class CustomResponse {

   private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public CustomResponse(){}
    public CustomResponse(int id, String message, String code, Status status) {
        this.id = id;
        this.message = message;
        this.code = code;
        this.status = status;
    }

    private String message;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    private String code;

    private Status status;

}
