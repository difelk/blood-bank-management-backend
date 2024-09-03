package com.bcn.bmc.controllers;

import com.bcn.bmc.models.UserActivity;
import com.bcn.bmc.models.UserActivityResponse;
import com.bcn.bmc.services.UserActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/users/activity")
public class UserActivityController {

    @Autowired
    UserActivityService userActivityService;


    @PostMapping(path = "/")
    public UserActivityResponse createAddress(@RequestBody UserActivity userActivity){
        return userActivityService.createActivity(userActivity);
    }


    @GetMapping(path = "/")
    public List<UserActivity> getAllActivities(){
        return userActivityService.getAllActivities();
    }


}
