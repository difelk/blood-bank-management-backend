package com.bcn.bmc.models;

public class PatientDocumentResponse {
    private String status;
    private String message;
    private Long patientId;

    private Long id;
    private String fileName;
    private String fileType;
    private Long fileSize;
    private String data;

    public PatientDocumentResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public PatientDocumentResponse(String status, String message, Long patientId) {
        this.status = status;
        this.message = message;
        this.patientId = patientId;
    }

    public PatientDocumentResponse(Long id, String fileName, String fileType, Long fileSize, String data) {
        this.id = id;
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.data = data;
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

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
