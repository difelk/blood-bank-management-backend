package com.bcn.bmc.models;

import jakarta.persistence.*;

@Entity
@Table(name = "organizations")
public class Organization {

    public Organization() {
    }
    public Organization(Integer id) {
        this.id = id;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "type", nullable = false)
    private String type;
}
