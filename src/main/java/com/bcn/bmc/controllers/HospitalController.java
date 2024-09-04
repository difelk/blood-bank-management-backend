package com.bcn.bmc.controllers;

import com.bcn.bmc.models.*;
import com.bcn.bmc.services.HospitalAddressService;
import com.bcn.bmc.services.HospitalDocumentService;
import com.bcn.bmc.services.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/hospitals")
public class HospitalController {

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private HospitalAddressService hospitalAddressService;

    @Autowired
    private HospitalDocumentService hospitalDocumentService;


    @PostMapping("/")
    public ResponseEntity<Hospital> createHospital(@RequestBody Hospital hospital) {
        try {
            Hospital createdHospital = hospitalService.createHospital(hospital);
            return new ResponseEntity<>(createdHospital, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/")
    public List<Hospital> getAllHospitals() {
        return hospitalService.getAllHospitals();
    }

    @GetMapping("/{id}")
    public Hospital getHospitalById(@PathVariable Long id) {
        return hospitalService.getHospitalById(id);
    }

    @GetMapping("/name/{name}")
    public Hospital getHospitalByName(@PathVariable String name) {
        return hospitalService.getHospitalByName(name);
    }

    @GetMapping("/sector/{sector}")
    public List<Hospital> getHospitalBySector(@PathVariable String sector) {
        return hospitalService.getHospitalBySector(sector);
    }

    @PutMapping("/")
    public ResponseEntity<HospitalResponse> updateHospital(@RequestBody Hospital hospital) {
        return ResponseEntity.ok(hospitalService.updateHospital(hospital));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HospitalResponse> deleteHospitalById(@PathVariable Long id) {
        return ResponseEntity.ok(hospitalService.deleteHospitalById(id));
    }

    // Address endpoints

    @PostMapping("/address")
    public ResponseEntity<HospitalAddressResponse> createAddress(@RequestBody HospitalAddress address) {
        return ResponseEntity.ok(hospitalAddressService.createAddress(address));
    }

    @GetMapping("/address")
    public List<HospitalAddress> getAllAddresses() {
        return hospitalAddressService.getAllAddresses();
    }

    @PutMapping("/address")
    public ResponseEntity<HospitalAddressResponse> updateAddress(@RequestBody HospitalAddress address) {
        return ResponseEntity.ok(hospitalAddressService.updateAddress(address));
    }

    @GetMapping("/address/{hospitalId}")
    public HospitalAddress getAddressByHospital(@PathVariable Long hospitalId) {
        return hospitalAddressService.getAddressByHospitalId(hospitalId);
    }

    // Document endpoints

    @PostMapping("/documents/upload")
    public ResponseEntity<HospitalDocument> uploadFile(@RequestParam("file") MultipartFile file,
                                                       @RequestParam("hospitalId") Long hospitalId) {
        try {
            HospitalDocument savedDocument = hospitalDocumentService.storeFile(file, hospitalId);
            return new ResponseEntity<>(savedDocument, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/documents/{hospitalId}")
    public ResponseEntity<List<HospitalDocumentResponse>> getHospitalDocuments(@PathVariable Long hospitalId) {
        List<HospitalDocument> hospitalDocuments = hospitalDocumentService.getHospitalDocumentsById(hospitalId);
        List<HospitalDocumentResponse> response = hospitalDocuments.stream().map(doc -> new HospitalDocumentResponse(
                doc.getId(),
                doc.getFileName(),
                doc.getFileType(),
                doc.getFileSize(),
                java.util.Base64.getEncoder().encodeToString(doc.getData())
        )).collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/documents/{documentId}")
    public ResponseEntity<HospitalDocumentResponse> deleteDocumentById(@PathVariable Long documentId) {
        return hospitalDocumentService.deleteDocumentById(documentId);
    }
}