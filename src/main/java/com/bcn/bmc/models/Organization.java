package com.bcn.bmc.models;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "organizations")
public class Organization {

    public Organization() {
    }

    public Organization(int id, String organizationName, LocalDate date) {
        this.organizationId = id;
        this.organizationName = organizationName;
        this.date = date;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "organization_id")
    private int organizationId;

    @Column(name = "organization_name")
    private String organizationName;

    @Column(name = "date")
    private LocalDate date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(int organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}