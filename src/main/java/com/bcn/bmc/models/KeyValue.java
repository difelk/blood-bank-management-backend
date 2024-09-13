package com.bcn.bmc.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KeyValue {

    private Long key;
    private String value;

    @JsonProperty("id")
    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    @JsonProperty("value")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public KeyValue(){

    }

    public KeyValue(Long key, String value){
        this.key = key;
        this.value = value;
    }


}
