package com.bcn.bmc.models;

public class UserAuthorize {

    private long userId;
    private int organization;
    private String userRole;

    public UserAuthorize() {
    }

    public UserAuthorize(long userId, int organization, String userRole) {
        this.userId = userId;
        this.organization = organization;
        this.userRole = userRole;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getOrganization() {
        return organization;
    }

    public void setOrganization(int organization) {
        this.organization = organization;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}
