package com.bcn.bmc.controllers;

import com.bcn.bmc.helper.TokenData;
import com.bcn.bmc.models.*;
import com.bcn.bmc.services.DonorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/donor")
public class DonorController {


    private final DonorService donorService;

    @Autowired
    private TokenData tokenHelper;

    public DonorController(DonorService donorService){
        this.donorService = donorService;
    }
    @PostMapping("/register")
    public ResponseEntity<DonorResponse> register(@RequestHeader("Authorization") String tokenHeader, @RequestBody Donor donor
    ) {
        System.out.println("inside donor cont service");
        UserAuthorize userAuthorize =   tokenHelper.parseToken(tokenHeader);
        donor.setCreatedBy(userAuthorize.getUserId());
        return ResponseEntity.ok(donorService.register(userAuthorize,  donor));
    }

    @PostMapping("/address/{donorId}")
    public ResponseEntity<DonorAddressResponse> saveAddress(@RequestHeader("Authorization") String tokenHeader,
                                                            @PathVariable Long donorId,
                                                            @RequestBody DonorAddress donorAddress) {
        UserAuthorize userAuthorize = tokenHelper.parseToken(tokenHeader);
        return ResponseEntity.ok(donorService.saveAddress(userAuthorize, donorId, donorAddress));
    }


    @PostMapping("/documents")
    public ResponseEntity<DonorDocumentResponse> saveDocument(@RequestHeader("Authorization") String tokenHeader, @RequestBody DonorDocument donorDocument
    ) {
        UserAuthorize userAuthorize =   tokenHelper.parseToken(tokenHeader);
        return ResponseEntity.ok(donorService.saveDocument(userAuthorize,  donorDocument));
    }

    @PostMapping("/documents/upload")
    public ResponseEntity<DonorDocument> uploadFile(@RequestParam("file") MultipartFile file,
                                                       @RequestParam("donorId") Long donorId) {
        try {
            DonorDocument savedDocument = donorService.storeFile(file, donorId);
            return new ResponseEntity<>(savedDocument, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public List<DonorDetails> getAllDonorDetails(@RequestHeader("Authorization") String tokenHeader) {
        UserAuthorize userAuthorize = tokenHelper.parseToken(tokenHeader);
        return donorService.getAllDonorsDetails(userAuthorize);
    }


//    @GetMapping("/{donorId}")
//    public DonorDetails getDonorDetails(@RequestHeader("Authorization") String tokenHeader, @PathVariable Long donorId){
//        UserAuthorize userAuthorize =  tokenHelper.parseToken(tokenHeader);
//        return donorService.getDonorByID(userAuthorize, donorId);
//    }

    @PutMapping("/{donorId}")
    public ResponseEntity<DonorResponse> updateDonor(
            @RequestHeader("Authorization") String tokenHeader,
            @PathVariable Long donorId,
            @RequestBody Donor updatedDonor
    ) {
        System.out.println("Incoming update donor request for donorId: " + donorId);
        System.out.println("Updated Donor Data: " + updatedDonor);
        UserAuthorize userAuthorize = tokenHelper.parseToken(tokenHeader);
        updatedDonor.setId(donorId);
        return ResponseEntity.ok(donorService.updateDonor(userAuthorize, updatedDonor));
    }

    @DeleteMapping("/delete/{donorId}")
    public ResponseEntity<DonorResponse> deleteDonorById(@PathVariable Long donorId) {
        return ResponseEntity.ok(donorService.deleteDonorById(donorId));
    }

    @DeleteMapping("/documents/{documentId}")
    public ResponseEntity<DonorDocumentResponse> deleteDocumentById(@PathVariable Long documentId) {
        return donorService.deleteDocumentById(documentId);
    }

    @GetMapping("/nic/{nic}")
    public Donor getDonorByNic(@PathVariable String nic) {
        return donorService.getDonorByNic(nic);
    }

    @GetMapping("/")
    public List<Donor> getAllDonors(@RequestHeader("Authorization") String tokenHeader) {
        UserAuthorize userAuthorize =  tokenHelper.parseToken(tokenHeader);
        return donorService.getAllDonors(userAuthorize);
    }

    @GetMapping("/address")
    public List<DonorAddress> getAllAddresses() {
        return donorService.getAllAddresses();
    }

    @GetMapping("/documents/{donorId}")
    public ResponseEntity<List<DonorDocumentResponse>> getDonorDocuments(@PathVariable Long donorId) {
        List<DonorDocument> donorDocuments = donorService.getDonorDocumentsById(donorId);
        List<DonorDocumentResponse> response = donorDocuments.stream().map(doc -> new DonorDocumentResponse(
                doc.getId(),
                doc.getFileName(),
                doc.getFileType(),
                doc.getFileSize(),
                java.util.Base64.getEncoder().encodeToString(doc.getData())
        )).collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{donorId}")
    public Donor getDonorById(@PathVariable Long donorId) {
        return donorService.getDonorById(donorId);
    }


    @PostMapping(path = "/csv")
    public DonorResponse uploadCsvFile( @RequestHeader("Authorization") String tokenHeader, @RequestParam("file") MultipartFile file) {
        UserAuthorize userAuthorize = tokenHelper.parseToken(tokenHeader);
        return donorService.createDonorFromCsv(userAuthorize, file);
    }

    @PutMapping("/address")
    public ResponseEntity<DonorAddressResponse> updateAddress(@RequestHeader("Authorization") String tokenHeader, @RequestBody DonorAddress address) {
        UserAuthorize userAuthorize = tokenHelper.parseToken(tokenHeader);
        return ResponseEntity.ok(donorService.updateAddress(userAuthorize, address));
    }
}
