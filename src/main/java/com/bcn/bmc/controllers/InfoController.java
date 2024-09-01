package com.bcn.bmc.controllers;

import com.bcn.bmc.enums.BloodType;
import com.bcn.bmc.models.BloodTypeDTO;
import com.bcn.bmc.models.User;
import com.bcn.bmc.services.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/info")
public class InfoController {
    @Autowired
    InfoService infoService;

    @GetMapping("/blood-groups")
    public List<BloodTypeDTO> getAllBloodGroups() {
        return infoService.getAllBloodGroups();
    }
}
