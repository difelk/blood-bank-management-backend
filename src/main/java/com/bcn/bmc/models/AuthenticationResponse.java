package com.bcn.bmc.models;

import com.bcn.bmc.enums.UserRole;

public class AuthenticationResponse {

    private long id;
    private String token;

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String message;

    private String email;

    private Organization organization;

    private UserRole userRole;

    private String firstName;

    private String lastName;

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

    public AuthenticationResponse(long id, String token, String message, String email, Organization organization, UserRole userRole, String firstName, String lastName) {
        this.id = id;
        this.token = token;
        this.message = message;
        this.email = email;
        this.organization = organization;
        this.userRole = userRole;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    public String getToken() {
        return token;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public String getMessage() {
        return message;
    }

    public long getId() {
        return id;
    }

}
