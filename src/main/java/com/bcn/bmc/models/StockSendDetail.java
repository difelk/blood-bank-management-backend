package com.bcn.bmc.models;


import com.bcn.bmc.enums.SendStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "stock_send_details")
public class StockSendDetail {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stock_send_id", nullable = false)
    private long stockSend;


    @Column(name = "blood_request_detail_id", nullable = false)
    private long bloodRequestDetail;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getBloodRequestDetail() {
        return bloodRequestDetail;
    }

    public void setBloodRequestDetail(long bloodRequestDetail) {
        this.bloodRequestDetail = bloodRequestDetail;
    }


    public double getSentQuantity() {
        return sentQuantity;
    }

    public void setSentQuantity(double sentQuantity) {
        this.sentQuantity = sentQuantity;
    }

    public SendStatus getStatus() {
        return status;
    }

    public void setStatus(SendStatus status) {
        this.status = status;
    }

    @Column(name = "sent_quantity", nullable = false)
    private double sentQuantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SendStatus status;

    public long getStockSend() {
        return stockSend;
    }

    public void setStockSend(long stockSend) {
        this.stockSend = stockSend;
    }

    public StockSendDetail() {
    }

    public StockSendDetail(long stockSend, long bloodRequestDetail, double sentQuantity, SendStatus status) {
        this.stockSend = stockSend;
        this.bloodRequestDetail = bloodRequestDetail;
        this.sentQuantity = sentQuantity;
        this.status = status;
    }


}
