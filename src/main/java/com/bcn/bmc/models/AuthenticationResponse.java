package com.bcn.bmc.models;

public class AuthenticationResponse {

    private long id;
    private String token;
    private String message;

    public AuthenticationResponse(long id, String token, String message) {
        this.id = id;
        this.token = token;
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public String getMessage() {
        return message;
    }

    public long getId() {
        return id;
    }

}
