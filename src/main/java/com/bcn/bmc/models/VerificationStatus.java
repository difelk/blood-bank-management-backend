package com.bcn.bmc.models;

public class VerificationStatus {
    public boolean isExist() {
        return isExist;
    }

    public VerificationStatus(boolean isExist, String ExtraDetails, long userId){
        this.isExist = isExist;
        this.ExtraDetails = ExtraDetails;
        this.userid = userId;
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

    private long userid;

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    private boolean isExist;

    private String ExtraDetails;
}
