package com.bcn.bmc.controllers;

import com.bcn.bmc.helper.TokenData;
import com.bcn.bmc.models.*;
import com.bcn.bmc.services.DonorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/address")
    public ResponseEntity<DonorAddressResponse> saveAddress(@RequestHeader("Authorization") String tokenHeader, @RequestBody DonorAddress donorAddress
    ) {
        UserAuthorize userAuthorize =   tokenHelper.parseToken(tokenHeader);
        return ResponseEntity.ok(donorService.saveAddress(userAuthorize,  donorAddress));
    }

    @PostMapping("/documents")
    public ResponseEntity<DonorDocumentResponse> saveDocument(@RequestHeader("Authorization") String tokenHeader, @RequestBody DonorDocument donorDocument
    ) {
        UserAuthorize userAuthorize =   tokenHelper.parseToken(tokenHeader);
        return ResponseEntity.ok(donorService.saveDocument(userAuthorize,  donorDocument));
    }

    @GetMapping("/all")
    public List<DonorDetails> getAllDonorDetails(@RequestHeader("Authorization") String tokenHeader) {
        UserAuthorize userAuthorize = tokenHelper.parseToken(tokenHeader);
        return donorService.getAllDonorsDetails(userAuthorize);
    }


    @GetMapping("/{donorId}")
    public DonorDetails getDonorDetails(@RequestHeader("Authorization") String tokenHeader, @PathVariable Long donorId){
        UserAuthorize userAuthorize =  tokenHelper.parseToken(tokenHeader);
        return donorService.getDonorByID(userAuthorize, donorId);
    }


}
