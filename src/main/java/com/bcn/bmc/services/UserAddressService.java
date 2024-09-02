package com.bcn.bmc.services;

import com.bcn.bmc.models.Address;
import com.bcn.bmc.models.User;
import com.bcn.bmc.models.UserAddressResponse;
import com.bcn.bmc.repositories.UserAddressRepository;
import com.bcn.bmc.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserAddressService {

    @Autowired
    private final UserAddressRepository addressRepository;


    public UserAddressService(UserAddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Transactional
    public UserAddressResponse createAddress(Address address) {
        System.out.println("called createAddress service");
        try {
            Address savedAddress = addressRepository.save(address);
            return new UserAddressResponse(savedAddress.getId(), "Address created successfully");
        } catch (Exception e) {
            return new UserAddressResponse(-1, "Failed to create address: " + e.getMessage());
        }
    }


    @Transactional
    public UserAddressResponse updateAddress(Address newAddress) {
        Optional<Address> addressOptional = addressRepository.findAddressByUserId(Long.parseLong(newAddress.getUserId()));
        if (addressOptional.isPresent()) {
            Address existingAddress = addressOptional.get();
            existingAddress.setStreetNumber(newAddress.getStreetNumber());
            existingAddress.setStreetName(newAddress.getStreetName());
            existingAddress.setCity(newAddress.getCity());
            existingAddress.setState(newAddress.getState());
            existingAddress.setZipCode(newAddress.getZipCode());
            Address savedAddress = addressRepository.save(existingAddress);
            return new UserAddressResponse(savedAddress.getId(), "Address Updated successfully");
        } else {
            return createAddress(newAddress);
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
