package com.bcn.bmc.controllers;

import com.bcn.bmc.helper.TokenData;
import com.bcn.bmc.models.*;
import com.bcn.bmc.services.BloodRequestService;
import com.bcn.bmc.services.StockService;
import com.bcn.bmc.services.StockTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/stock")
public class StockController {


    @Autowired
    StockTransactionService stockTransactionService;
    @Autowired
    StockService stockService;

    @Autowired
    BloodRequestService bloodRequestService;
    @Autowired
    private TokenData tokenHelper;


    @GetMapping("/history")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<StockHistory> getHospitalTransactionHistory(@RequestHeader("Authorization") String tokenHeader){
        UserAuthorize userAuthorize = tokenHelper.parseToken(tokenHeader);
        return stockTransactionService.getHospitalTransactionHistory(userAuthorize);
    }


    @GetMapping("/matching-hospitals")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<KeyValue> getHospitalsWithMatchingStock(@RequestHeader("Authorization") String tokenHeader, @RequestParam("bloodType") List<String> bloodTypes, @RequestParam("qty") List<Double> quantities ) {
        UserAuthorize userAuthorize = tokenHelper.parseToken(tokenHeader);
        return stockService.getHospitalsWithMatchingStock(userAuthorize, bloodTypes, quantities);
    }

    @PostMapping(path = "/request")
    public CustomResponse requestStock(@RequestHeader("Authorization") String tokenHeader, @RequestBody List<HospitalStockRequest> hospitalStockRequests){
        UserAuthorize userAuthorize =  tokenHelper.parseToken(tokenHeader);
        return bloodRequestService.requestBlood(userAuthorize, hospitalStockRequests);
    }

    @GetMapping("/requests")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<BloodRequestAllDetails> getAllRequestStock(@RequestHeader("Authorization") String tokenHeader) {
        UserAuthorize userAuthorize = tokenHelper.parseToken(tokenHeader);
        return bloodRequestService.getAllRequestStock(userAuthorize);
    }


    @PutMapping("/requests/details")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public CustomResponse updateRequestDetails(@RequestHeader("Authorization") String tokenHeader, @RequestBody List<BloodKeyValue> bloodKeyValue) {
        UserAuthorize userAuthorize = tokenHelper.parseToken(tokenHeader);
        return bloodRequestService.updateRequestDetails(userAuthorize, bloodKeyValue);
    }

    @DeleteMapping("/requests/details/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public CustomResponse deleteStockRequestById(@RequestHeader("Authorization") String tokenHeader, @PathVariable long id) {
        UserAuthorize userAuthorize = tokenHelper.parseToken(tokenHeader);
        return bloodRequestService.deleteStockRequestById(userAuthorize, id);
    }


    @GetMapping("/request/provider/history")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<BloodRequestAllDetails> getAllRequestStockAccordingToProvider(@RequestHeader("Authorization") String tokenHeader) {
        UserAuthorize userAuthorize = tokenHelper.parseToken(tokenHeader);
        return bloodRequestService.getAllRequestStockAccordingToProvider(userAuthorize);
    }

    @PostMapping(path = "/share")
    public CustomResponse  shareStock(@RequestHeader("Authorization") String tokenHeader, @RequestBody BloodRequestAllDetails bloodRequestAllDetails){
        UserAuthorize userAuthorize =  tokenHelper.parseToken(tokenHeader);
       return bloodRequestService.shareStock(userAuthorize, bloodRequestAllDetails);
    }


}
