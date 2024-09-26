package com.bcn.bmc.models;

import com.bcn.bmc.enums.ActivityStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_Activity")
public class UserActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "activity_type", nullable = false)
    private String activityType;

    @Column(name = "activity_details", nullable = true, length = 1000)
    private String activityDetails;

    @Column(name = "ip_address", nullable = true)
    private String ipAddress;

    public UserActivity(){

    }
    public UserActivity(Long userId, String activityType, String activityDetails, String ipAddress, LocalDateTime timestamp, ActivityStatus activityStatus) {
        this.userId = userId;
        this.activityType = activityType;
        this.activityDetails = activityDetails;
        this.ipAddress = ipAddress;
        this.timestamp = timestamp;
        this.activityStatus = activityStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public UserActivity(){

    }
    public UserActivity(Long userId, String activityType, String activityDetails, String ipAddress, LocalDateTime timestamp, ActivityStatus activityStatus) {
        this.userId = userId;
        this.activityType = activityType;
        this.activityDetails = activityDetails;
        this.ipAddress = ipAddress;
        this.timestamp = timestamp;
        this.activityStatus = activityStatus;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getActivityDetails() {
        return activityDetails;
    }

    public void setActivityDetails(String activityDetails) {
        this.activityDetails = activityDetails;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();

    @Column(name = "activity_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ActivityStatus activityStatus = ActivityStatus.SUCCESS;

    public ActivityStatus getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(ActivityStatus activityStatus) {
        this.activityStatus = activityStatus;
    }


}
