package com.bcn.bmc.controllers;

import com.bcn.bmc.helper.TokenData;
import com.bcn.bmc.models.*;
import com.bcn.bmc.services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    @Autowired
    private TokenData tokenHelper;

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping("/create")
    public ResponseEntity<PatientResponse> createPatient(@RequestHeader("Authorization") String tokenHeader,
                                                         @RequestBody Patient patient) {
        UserAuthorize userAuthorize = tokenHelper.parseToken(tokenHeader);
        patient.setCreatedBy(userAuthorize.getUserId());
        return ResponseEntity.ok(patientService.createPatient(userAuthorize, patient));
    }

    @GetMapping("/all")
    public List<Patient> getAllPatients(@RequestHeader("Authorization") String tokenHeader) {
        UserAuthorize userAuthorize = tokenHelper.parseToken(tokenHeader);
        return patientService.getAllPatients(userAuthorize);
    }

    @PutMapping("/update")
    public ResponseEntity<PatientResponse> updatePatient(@RequestHeader("Authorization") String tokenHeader,
                                                         @RequestBody Patient patient) {
        UserAuthorize userAuthorize = tokenHelper.parseToken(tokenHeader);
        return ResponseEntity.ok(patientService.updatePatient(userAuthorize, patient));
    }

    @DeleteMapping("/delete/nic/{nic}")
    public ResponseEntity<PatientResponse> deletePatientByNic(@RequestHeader("Authorization") String tokenHeader,
                                                              @PathVariable String nic) {
        UserAuthorize userAuthorize = tokenHelper.parseToken(tokenHeader);
        return ResponseEntity.ok(patientService.deletePatientByNic(userAuthorize, nic));
    }

    @GetMapping("/{patientId}")
    public Patient getPatientById(@RequestHeader("Authorization") String tokenHeader,
                                  @PathVariable Long patientId) {
        UserAuthorize userAuthorize = tokenHelper.parseToken(tokenHeader);
        return patientService.getPatientById(userAuthorize, patientId);
    }

    @PostMapping("/address/{patientId}")
    public ResponseEntity<PatientAddressResponse> saveAddress(@RequestHeader("Authorization") String tokenHeader,
                                                              @PathVariable Long patientId,
                                                              @RequestBody PatientAddress patientAddress) {
        UserAuthorize userAuthorize = tokenHelper.parseToken(tokenHeader);
        return ResponseEntity.ok(patientService.saveAddress(userAuthorize, patientId, patientAddress));
    }

    @GetMapping("/address/{patientId}")
    public List<PatientAddress> getAddressByPatientId(@RequestHeader("Authorization") String tokenHeader,
                                                      @PathVariable Long patientId) {
        UserAuthorize userAuthorize = tokenHelper.parseToken(tokenHeader);
        return patientService.getAddressByPatientId(userAuthorize, patientId);
    }

//    @PostMapping("/documents/upload")
//    public ResponseEntity<PatientDocumentResponse> saveDocument(@RequestHeader("Authorization") String tokenHeader,
//                                                                @RequestBody PatientDocuments patientDocument) {
//        UserAuthorize userAuthorize = tokenHelper.parseToken(tokenHeader);
//        return ResponseEntity.ok(patientService.saveDocument(userAuthorize, patientDocument));
//    }

    @PostMapping("/documents/upload")
    public ResponseEntity<PatientDocuments> uploadFile(@RequestParam("file") MultipartFile file,
                                                       @RequestParam("patientId") Long patientId) {
        try {
            PatientDocuments savedDocument = patientService.storeFile(file, patientId);
            return new ResponseEntity<>(savedDocument, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/documents/{patientId}")
    public List<PatientDocuments> getDocumentsByPatientId(@RequestHeader("Authorization") String tokenHeader,
                                                          @PathVariable Long patientId) {
        UserAuthorize userAuthorize = tokenHelper.parseToken(tokenHeader);
        return patientService.getDocumentsByPatientId(userAuthorize, patientId);
    }

    @DeleteMapping("/documents/{documentId}")
    public ResponseEntity<PatientDocumentResponse> deleteDocumentById(@RequestHeader("Authorization") String tokenHeader,
                                                                      @PathVariable Long documentId) {
        UserAuthorize userAuthorize = tokenHelper.parseToken(tokenHeader);
        return patientService.deleteDocumentById(userAuthorize, documentId);
    }

//    @PutMapping(value = "/address", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<PatientAddressResponse> updateAddress(@RequestHeader("Authorization") String tokenHeader, @RequestBody PatientAddress address) {
//        UserAuthorize userAuthorize = tokenHelper.parseToken(tokenHeader);
//        PatientAddressResponse response = patientService.updateAddress(userAuthorize, address);
//        return ResponseEntity.ok(response);
//    }

    @PutMapping("/address")
    public ResponseEntity<PatientAddressResponse> updateAddress(@RequestBody PatientAddress address) {
        System.out.println("CONTROLLER address: " + address.getPatientId());
        PatientAddressResponse response = patientService.updateAddress(address);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }


    @GetMapping("/address")
    public List<PatientAddress> getAllAddresses() {
        return patientService.getAllAddresses();
    }

    @GetMapping("/nic/{nic}")
    public Patient getPatientByNic(@PathVariable String nic) {
        return patientService.getPatientByNic(nic);
    }


    @DeleteMapping("/delete/{patientId}")
    public ResponseEntity<PatientResponse> deletePatientById(@PathVariable Long patientId) {
        return ResponseEntity.ok(patientService.deletePatientById(patientId));
    }

}
