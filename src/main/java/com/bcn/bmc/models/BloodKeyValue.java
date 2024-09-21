package com.bcn.bmc.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BloodKeyValue {


    private long id;
    private String key;
    private Double value;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public BloodKeyValue(){

    }

    public BloodKeyValue(long id, String key, Double value){
        this.id = id;
        this.key = key;
        this.value = value;
    }

}
