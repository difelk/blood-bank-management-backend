package com.bcn.bmc.services;

import com.bcn.bmc.enums.ActiveStatus;
import com.bcn.bmc.models.*;
import com.bcn.bmc.repositories.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final PatientAddressRepository patientAddressRepository;
    private final PatientDocumentRepository patientDocumentRepository;

    public PatientService(PatientRepository patientRepository,
                          PatientAddressRepository patientAddressRepository,
                          PatientDocumentRepository patientDocumentRepository) {
        this.patientRepository = patientRepository;
        this.patientAddressRepository = patientAddressRepository;
        this.patientDocumentRepository = patientDocumentRepository;
    }

    @Transactional
    public PatientResponse createPatient(UserAuthorize userAuthorize, Patient patient) {
        try {
            if (patientRepository.existsByNic(patient.getNic())) {
                return new PatientResponse("Failure", "Patient with NIC already exists.");
            }

            patient.setCreatedBy(userAuthorize.getUserId());
            patient.setDate(new Date());
            Patient newPatient = patientRepository.save(patient);

            return new PatientResponse("Success", "Patient created successfully.", newPatient.getId());
        } catch (Exception e) {
            throw new RuntimeException("Error creating patient: " + e.getMessage(), e);
        }
    }

//    public List<Patient> getAllPatients(UserAuthorize userAuthorize) {
//        try {
//            return patientRepository.findAll();
//        } catch (Exception e) {
//            throw new RuntimeException("Error fetching all patients: " + e.getMessage(), e);
//        }
//    }


    public List<Patient> getAllPatients(UserAuthorize userAuthorize) {

        try {
//            if (admin.getOrganization() == 1) {
            return patientRepository.findAllByStatus(ActiveStatus.ACTIVE);
//            }else{
//                return donorRepository.findAllByOrganizationId(admin.getOrganization());
//            }
        } catch (Exception e) {
            System.out.println("Error fetching all patients: " + e.getMessage());
            return null;
        }


    }

    @Transactional
    public PatientResponse updatePatient(UserAuthorize userAuthorize, Patient updatedPatient) {
        try {

            System.out.println("PATIENT ID in UPDATE: " + updatedPatient.getId());
            Optional<Patient> existingPatient = patientRepository.findById(updatedPatient.getId());
            if (existingPatient.isEmpty()) {
                throw new RuntimeException("Patient not found for ID: " + updatedPatient.getId());
            }

            if (!existingPatient.get().getNic().equals(updatedPatient.getNic()) && patientRepository.existsByNic(updatedPatient.getNic())) {
                return new PatientResponse("Failure", "Patient with NIC already exists.");
            }

            existingPatient.get().setNic(updatedPatient.getNic());
            existingPatient.get().setGender(updatedPatient.getGender());
            existingPatient.get().setFirstName(updatedPatient.getFirstName());
            existingPatient.get().setLastName(updatedPatient.getLastName());
            existingPatient.get().setBloodType(updatedPatient.getBloodType());
            existingPatient.get().setDob(updatedPatient.getDob());
            existingPatient.get().setContactNo(updatedPatient.getContactNo());
            existingPatient.get().setEmergencyContactPersonName(updatedPatient.getEmergencyContactPersonName());
            existingPatient.get().setEmergencyContactPersonContactNo(updatedPatient.getEmergencyContactPersonContactNo());

            patientRepository.save(existingPatient.get());

            return new PatientResponse("Success", "Patient details updated successfully.", updatedPatient.getId());
        } catch (Exception e) {
            throw new RuntimeException("Error updating patient: " + e.getMessage(), e);
        }
    }

    @Transactional
    public PatientResponse deletePatientByNic(UserAuthorize userAuthorize, String nic) {
        try {
            Optional<Patient> patient = patientRepository.findByNic(nic);
            if (patient.isEmpty()) {
                return new PatientResponse("Failure", "Patient with NIC not found.");
            }

            patientRepository.delete(patient.get());

            return new PatientResponse("Success", "Patient deleted successfully.");
        } catch (Exception e) {
            throw new RuntimeException("Error deleting patient: " + e.getMessage(), e);
        }
    }

    public Patient getPatientById(UserAuthorize userAuthorize, Long patientId) {
        try {
            return patientRepository.findById(patientId)
                    .orElseThrow(() -> new RuntimeException("Patient not found for ID: " + patientId));
        } catch (Exception e) {
            throw new RuntimeException("Error fetching patient by ID: " + e.getMessage(), e);
        }
    }

    @Transactional
    public PatientAddressResponse saveAddress(UserAuthorize userAuthorize, Long patientId, PatientAddress patientAddress) {
        try {
            patientAddress.setPatientId(patientId);
            PatientAddress savedAddress = patientAddressRepository.save(patientAddress);

            return new PatientAddressResponse("Success", "Address saved successfully.");
        } catch (Exception e) {
            throw new RuntimeException("Error saving address: " + e.getMessage(), e);
        }
    }

    public List<PatientAddress> getAddressByPatientId(UserAuthorize userAuthorize, Long patientId) {
        try {
            return patientAddressRepository.findByPatientId(patientId);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching addresses: " + e.getMessage(), e);
        }
    }

//    @Transactional
//    public PatientDocumentResponse saveDocument(UserAuthorize userAuthorize, PatientDocuments patientDocument) {
//        try {
//            PatientDocuments savedDocument = patientDocumentRepository.save(patientDocument);
//
//            return new PatientDocumentResponse("Success", "Document saved successfully.");
//        } catch (Exception e) {
//            throw new RuntimeException("Error saving document: " + e.getMessage(), e);
//        }
//    }

    @Transactional
    public PatientDocuments storeFile(MultipartFile file, Long patientId) throws IOException {

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid patient ID"));


        PatientDocuments patientDocument = new PatientDocuments();
        patientDocument.setFileName(file.getOriginalFilename());
        patientDocument.setFileType(file.getContentType());
        patientDocument.setFileSize(file.getSize());
        patientDocument.setData(file.getBytes());
        patientDocument.setPatientId(patient.getId());

        return patientDocumentRepository.save(patientDocument);
    }


    public List<PatientDocuments> getDocumentsByPatientId(UserAuthorize userAuthorize, Long patientId) {
        try {
            return patientDocumentRepository.findByPatientId(patientId);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching documents: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ResponseEntity<PatientDocumentResponse> deleteDocumentById(UserAuthorize userAuthorize, Long documentId) {
        try {
            patientDocumentRepository.deleteById(documentId);

            PatientDocumentResponse response = new PatientDocumentResponse("Success", "Document deleted successfully.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting document: " + e.getMessage(), e);
        }
    }

//    @Transactional
//    public PatientAddressResponse updateAddress(UserAuthorize userAuthorize, PatientAddress newAddress) {
//        System.out.println("PATIENT ADDRESS - PATIENT ID: "+ newAddress.getPatientId());
//        Optional<PatientAddress> addressOptional = patientAddressRepository.findAddressByPatientId(newAddress.getPatientId());
//
//
//        if (addressOptional.isPresent()) {
//            PatientAddress existingAddress = addressOptional.get();
//            existingAddress.setStreetNumber(newAddress.getStreetNumber());
//            existingAddress.setStreetName(newAddress.getStreetName());
//            existingAddress.setCity(newAddress.getCity());
//            existingAddress.setState(newAddress.getState());
//            existingAddress.setZipCode(newAddress.getZipCode());
//
//
//
//            PatientAddress savedAddress = patientAddressRepository.save(existingAddress);
//
//
//            return new PatientAddressResponse("Success", "Address updated successfully", savedAddress.getId());
//        } else {
//
//            return saveAddress(userAuthorize, newAddress.getPatientId(), newAddress);
//        }
//    }


    @Transactional
    public PatientAddressResponse updateAddress(PatientAddress newAddress) {

        Optional<PatientAddress> addressOptional = patientAddressRepository.findAddressByPatientId(newAddress.getPatientId());
        System.out.println("addressOptional: "+ addressOptional);
        if (addressOptional.isPresent()) {
            PatientAddress existingAddress = addressOptional.get();
            existingAddress.setStreetNumber(newAddress.getStreetNumber());
            existingAddress.setStreetName(newAddress.getStreetName());
            existingAddress.setCity(newAddress.getCity());
            PatientAddress savedAddress = patientAddressRepository.save(existingAddress);

            return new PatientAddressResponse("Success", "Address updated successfully", savedAddress.getId());
        } else {
           return new PatientAddressResponse("Failed", "Address update unsuccessful");
        }
    }


    public List<PatientAddress> getAllAddresses() {
        try {

            return patientAddressRepository.findAll();
        } catch (Exception e) {
            System.out.println("Error fetching all adresses: " + e.getMessage());
            return null;
        }
    }

    public Patient getPatientByNic(String patientNic) {
        try {
            return patientRepository.findPatientByNic(patientNic).orElse(null);
        } catch (Exception e) {
            System.out.println("Error fetching patient by nic: " + e.getMessage());
            return null;
        }
    }


    @Transactional
    public PatientResponse deletePatientById(long patientId) {
        try {
            Patient patient = patientRepository.findById(patientId).orElse(null);
            if (patient != null) {
                patient.setStatus(ActiveStatus.INACTIVE);
                patientRepository.save(patient);
                return new PatientResponse("Success", "Patient marked as inactive successfully");
            } else {
                return new PatientResponse("Failed", "Patient not found");
            }
        } catch (Exception e) {
            return new PatientResponse("Failed", "Failed to mark patient as inactive: " + e.getMessage());
        }
    }

}
