package com.bcn.bmc.controllers;

import com.bcn.bmc.helper.TokenData;
import com.bcn.bmc.models.StockHistory;
import com.bcn.bmc.models.StockTransaction;
import com.bcn.bmc.models.User;
import com.bcn.bmc.models.UserAuthorize;
import com.bcn.bmc.services.StockTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private TokenData tokenHelper;

    @GetMapping("/history")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<StockHistory> getHospitalTransactionHistory(@RequestHeader("Authorization") String tokenHeader){
        UserAuthorize userAuthorize = tokenHelper.parseToken(tokenHeader);
        return stockTransactionService.getHospitalTransactionHistory(userAuthorize);
    }


}
