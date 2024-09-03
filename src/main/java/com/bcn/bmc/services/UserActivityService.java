package com.bcn.bmc.services;

import com.bcn.bmc.models.Address;
import com.bcn.bmc.models.UserActivity;
import com.bcn.bmc.models.UserActivityResponse;
import com.bcn.bmc.models.UserAddressResponse;
import com.bcn.bmc.repositories.UserActivityRepository;
import com.bcn.bmc.repositories.UserAddressRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserActivityService {


    UserActivityRepository userActivityRepository;


    public UserActivityService(UserActivityRepository userActivityRepository) {
        this.userActivityRepository = userActivityRepository;
    }

    @Transactional
    public UserActivityResponse createActivity(UserActivity userActivity) {
        System.out.println("called createAddress service");
        try {
            UserActivity saveActivity = userActivityRepository.save(userActivity);
            return new UserActivityResponse(saveActivity.getId(), "Activity created successfully");
        } catch (Exception e) {
            return new UserActivityResponse(-1, "Failed to create Activity: " + e.getMessage());
        }
    }


    public List<UserActivity> getAllActivities() {
        try {
            return userActivityRepository.findAll();
        } catch (Exception e) {
            System.out.println("Failed to fetch all Activities: " + e.getMessage());
            return null;
        }
    }


}
