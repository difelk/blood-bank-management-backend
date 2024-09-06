package com.bcn.bmc.models;

public class AuthenticationResponse {

    private long id;
    private String token;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String message;

    private String email;

    public AuthenticationResponse(long id, String token, String message) {
        this.id = id;
        this.token = token;
        this.message = message;
    }


    public AuthenticationResponse(long id, String token, String message, String email) {
        this.id = id;
        this.token = token;
        this.message = message;
        this.email = email;
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
