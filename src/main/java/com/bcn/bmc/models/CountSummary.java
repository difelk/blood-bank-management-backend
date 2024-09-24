package com.bcn.bmc.models;

public class CountSummary {

    private String name;
    private int totalCount;
    public CountSummary(){}
    public CountSummary(String name, int totalCount, int activeCount, int deActiveCount) {
        this.name = name;
        this.totalCount = totalCount;
        this.activeCount = activeCount;
        this.deActiveCount = deActiveCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getActiveCount() {
        return activeCount;
    }

    public void setActiveCount(int activeCount) {
        this.activeCount = activeCount;
    }

    public int getDeActiveCount() {
        return deActiveCount;
    }

    public void setDeActiveCount(int deActiveCount) {
        this.deActiveCount = deActiveCount;
    }

    private int activeCount;
    private int deActiveCount;
}
