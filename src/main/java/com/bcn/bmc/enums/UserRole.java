package com.bcn.bmc.enums;

public enum UserRole {
    ADMIN("Admin"),
    SUPER_ADMIN("Super Admin"),
    USER("User");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
