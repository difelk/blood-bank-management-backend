package com.bcn.bmc.models;

public class HospitalStockRequest {

    private String ABMinus;
    private String ABPlus;
    private String AMinus;
    private String APlus;
    private String BMinus;
    private String BPlus;
    private String OMinus;
    private String OPlus;

    public String getABMinus() {
        return ABMinus;
    }

    public void setABMinus(String ABMinus) {
        this.ABMinus = ABMinus;
    }

    public String getABPlus() {
        return ABPlus;
    }

    public void setABPlus(String ABPlus) {
        this.ABPlus = ABPlus;
    }

    public String getAMinus() {
        return AMinus;
    }

    public void setAMinus(String AMinus) {
        this.AMinus = AMinus;
    }

    public String getAPlus() {
        return APlus;
    }

    public void setAPlus(String APlus) {
        this.APlus = APlus;
    }

    public String getBMinus() {
        return BMinus;
    }

    public void setBMinus(String BMinus) {
        this.BMinus = BMinus;
    }

    public String getBPlus() {
        return BPlus;
    }

    public void setBPlus(String BPlus) {
        this.BPlus = BPlus;
    }

    public String getOMinus() {
        return OMinus;
    }

    public void setOMinus(String OMinus) {
        this.OMinus = OMinus;
    }

    public String getOPlus() {
        return OPlus;
    }

    public void setOPlus(String OPlus) {
        this.OPlus = OPlus;
    }

    public long getHospitalId() {
        return id;
    }

    public void setHospitalId(long id) {
        this.id = id;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    private long id;
    private String hospitalName;


    public HospitalStockRequest(long id, String hospitalName, String ABMinus, String ABPlus, String AMinus, String APlus,
                                String BMinus, String BPlus, String OMinus, String OPlus) {
        this.id = id;
        this.hospitalName = hospitalName;
        this.ABMinus = ABMinus;
        this.ABPlus = ABPlus;
        this.AMinus = AMinus;
        this.APlus = APlus;
        this.BMinus = BMinus;
        this.BPlus = BPlus;
        this.OMinus = OMinus;
        this.OPlus = OPlus;
    }


}
