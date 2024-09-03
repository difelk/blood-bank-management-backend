package com.bcn.bmc.models;

public class UserActivityResponse {
    private long id;
    private String message;

    public UserActivityResponse(long id, String message) {
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
