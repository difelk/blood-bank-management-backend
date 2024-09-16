package com.bcn.bmc.models;

import com.bcn.bmc.enums.FulfillmentStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "blood_requests")
public class BloodRequest {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "requestor_organization_id", nullable = false)
    private int requestorOrganizationId;

    @Column(name = "provider_organization_id", nullable = false)
    private long providerOrganizationId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getRequestorOrganizationId() {
        return requestorOrganizationId;
    }

    public void setRequestorOrganizationId(int requestorOrganizationId) {
        this.requestorOrganizationId = requestorOrganizationId;
    }

    public long getProviderOrganizationId() {
        return providerOrganizationId;
    }

    public void setProviderOrganizationId(long providerOrganizationId) {
        this.providerOrganizationId = providerOrganizationId;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }

    public FulfillmentStatus getFulfillmentStatus() {
        return fulfillmentStatus;
    }

    public void setFulfillmentStatus(FulfillmentStatus fulfillmentStatus) {
        this.fulfillmentStatus = fulfillmentStatus;
    }

    @Column(name = "request_date", nullable = false, updatable = false)
    private LocalDateTime requestDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "fulfillment_status", nullable = false)
    private FulfillmentStatus fulfillmentStatus;


}
