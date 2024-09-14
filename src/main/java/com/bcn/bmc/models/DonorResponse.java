package com.bcn.bmc.models;

import java.util.List;
import java.util.Map;

public class DonorResponse {


        private String status;
        private String message;
        private Long donorId;
    private Map<String, List<String>> failureDetails;

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
