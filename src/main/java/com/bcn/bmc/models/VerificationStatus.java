package com.bcn.bmc.models;

public class VerificationStatus {
    public boolean isExist() {
        return isExist;
    }

    public VerificationStatus(boolean isExist, String ExtraDetails){
        this.isExist = isExist;
        this.ExtraDetails = ExtraDetails;
    }
    public void setExist(boolean exist) {
        isExist = exist;
    }

    public String getExtraDetails() {
        return ExtraDetails;
    }

    public void setExtraDetails(String extraDetails) {
        ExtraDetails = extraDetails;
    }

    private boolean isExist;

    private String ExtraDetails;
}
