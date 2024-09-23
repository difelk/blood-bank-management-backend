package com.bcn.bmc.models;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class DonorResponse {


        private String status;
        private String message;
        private Long donorId;
    private Map<String, List<String>> failureDetails;


    private Long id;
    private String firstName;
    private String lastName;
    private String bloodType;
    private String contact;

    public DonorResponse(String status, String message) {
            this.status = status;
            this.message = message;
        }

        public DonorResponse(String status, String message, Long donorId) {
            this.status = status;
            this.message = message;
            this.donorId = donorId;
        }

    public DonorResponse(String status, String message, Map<String, List<String>> failureDetails) {
        this.status = status;
        this.message = message;
        this.failureDetails = failureDetails;
    }


    public DonorResponse(Long id, String firstName, String lastName, String bloodType) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.bloodType = bloodType;
    }


    public DonorResponse(Long id, String firstName, String lastName, String bloodType, String contact) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.bloodType = bloodType;
        this.contact = contact;
    }


    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public Map<String, List<String>> getFailureDetails() {
        return failureDetails;
    }

    public void setFailureDetails(Map<String, List<String>> failureDetails) {
        this.failureDetails = failureDetails;
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

        public Long getDonorId() {
            return donorId;
        }

        public void setDonorId(Long donorId) {
            this.donorId = donorId;
        }

}
