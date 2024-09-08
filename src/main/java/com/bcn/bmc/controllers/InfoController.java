package com.bcn.bmc.controllers;

import com.bcn.bmc.enums.BloodType;
import com.bcn.bmc.helper.TokenData;
import com.bcn.bmc.models.BloodTypeDTO;
import com.bcn.bmc.models.Organization;
import com.bcn.bmc.models.User;
import com.bcn.bmc.models.UserAuthorize;
import com.bcn.bmc.services.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/info")
public class InfoController {
    @Autowired
    InfoService infoService;

    @Autowired
    private TokenData tokenHelper;

    @GetMapping("/blood-groups")
    public List<BloodTypeDTO> getAllBloodGroups() {
        return infoService.getAllBloodGroups();
    }

    @GetMapping("/organizations")
    public List<Organization> getAllOrganizations(@RequestHeader("Authorization") String tokenHeader) {
        UserAuthorize userAuthorize =   tokenHelper.parseToken(tokenHeader);
        return infoService.getAllOrganizations(userAuthorize);
    }
}
