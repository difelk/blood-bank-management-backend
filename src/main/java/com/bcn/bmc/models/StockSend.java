package com.bcn.bmc.models;

import com.bcn.bmc.enums.SendStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "stock_send")
public class StockSend {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "blood_request_id", nullable = false)
    private long bloodRequestId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getBloodRequestId() {
        return bloodRequestId;
    }

    public void setBloodRequestId(long bloodRequestId) {
        this.bloodRequestId = bloodRequestId;
    }

    public LocalDateTime getSentDate() {
        return sentDate;
    }

    public void setSentDate(LocalDateTime sentDate) {
        this.sentDate = sentDate;
    }

    public Long getSentByUserId() {
        return sentByUserId;
    }

    public void setSentByUserId(Long sentByUserId) {
        this.sentByUserId = sentByUserId;
    }

    public SendStatus getStatus() {
        return status;
    }

    public void setStatus(SendStatus status) {
        this.status = status;
    }

    @Column(name = "sent_date", nullable = false)
    private LocalDateTime sentDate;

    @Column(name = "sent_by_user_id")
    private Long sentByUserId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SendStatus status;


    public StockSend() {
    }

    public StockSend(long bloodRequestId, LocalDateTime sentDate, Long sentByUserId, SendStatus status) {
        this.bloodRequestId = bloodRequestId;
        this.sentDate = sentDate;
        this.sentByUserId = sentByUserId;
        this.status = status;
    }











}
