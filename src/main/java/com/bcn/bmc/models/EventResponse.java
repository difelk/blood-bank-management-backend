package com.bcn.bmc.models;

public class EventResponse {
    private String status;
    private String message;

    private Long eventId;

    public EventResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
    public EventResponse(String status, String message, Long eventId) {
        this.status = status;
        this.message = message;
        this.eventId = eventId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
