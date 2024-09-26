package com.bcn.bmc.services;

import com.bcn.bmc.enums.ActivityStatus;
import com.bcn.bmc.models.*;
import com.bcn.bmc.repositories.UserActivityRepository;
import com.bcn.bmc.repositories.UserAddressRepository;
import com.bcn.bmc.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserAddressService {

    @Autowired
    private final UserAddressRepository addressRepository;
    private final UserActivityRepository userActivityRepository;

    public UserAddressService(UserAddressRepository addressRepository, UserActivityRepository userActivityRepository) {
        this.addressRepository = addressRepository;
        this.userActivityRepository = userActivityRepository;
    }

    @Transactional
    public UserAddressResponse createAddress(UserAuthorize admin, Address address) {
        System.out.println("called createAddress service");
        try {
            Address savedAddress = addressRepository.save(address);
            userActivityRepository.save(new UserActivity(admin.getUserId(), "User Add Address", "Add Address: " + address.getUserId(), "", LocalDateTime.now(), ActivityStatus.SUCCESS));
            return new UserAddressResponse(savedAddress.getId(), "Address created successfully");
        } catch (Exception e) {
            userActivityRepository.save(new UserActivity(admin.getUserId(), "User Add Address", "Add Address Failed: " + address.getUserId(), "", LocalDateTime.now(), ActivityStatus.FAILURE));
            return new UserAddressResponse(-1, "Failed to create address: " + e.getMessage());
        }
    }


    @Transactional
    public UserAddressResponse updateAddress(UserAuthorize admin, Address newAddress) {
        Optional<Address> addressOptional = addressRepository.findAddressByUserId(Long.parseLong(newAddress.getUserId()));
        if (addressOptional.isPresent()) {
            Address existingAddress = addressOptional.get();
            existingAddress.setStreetNumber(newAddress.getStreetNumber());
            existingAddress.setStreetName(newAddress.getStreetName());
            existingAddress.setCity(newAddress.getCity());
            existingAddress.setState(newAddress.getState());
            existingAddress.setZipCode(newAddress.getZipCode());
            Address savedAddress = addressRepository.save(existingAddress);
            userActivityRepository.save(new UserActivity(admin.getUserId(), "User Address Update", "User Address Update: " + newAddress.getUserId(), "", LocalDateTime.now(), ActivityStatus.SUCCESS));
            return new UserAddressResponse(savedAddress.getId(), "Address Updated successfully");
        } else {
            return createAddress(admin, newAddress);
        }
    }


    public Address getAddressByUserId(Long userId){
        try {
            Optional<Address> address = addressRepository.findAddressByUserId(userId);
            System.out.println("user - " + address);
            if(address.isPresent()) {
                return address.get();
            }
            return null;
        } catch (Exception e) {
            System.out.println("Error finding address by userid: " + e.getMessage());
            return null;
        }

    }
}
