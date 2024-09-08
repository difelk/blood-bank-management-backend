package com.bcn.bmc.services;

import com.bcn.bmc.models.*;
import com.bcn.bmc.repositories.HospitalAddressRepository;
import com.bcn.bmc.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class HospitalAddressService {

    @Autowired
    private HospitalAddressRepository hospitalAddressRepository;


    private static final Logger logger = LoggerFactory.getLogger(HospitalAddressService.class);

//    public HospitalAddressService(HospitalAddressRepository hospitalAddressRepository) {
//        this.hospitalAddressRepository = hospitalAddressRepository;
//    }
    @Transactional
    public HospitalAddressResponse createAddress(HospitalAddress address) {
        try {
            HospitalAddress savedAddress = hospitalAddressRepository.save(address);
            return new HospitalAddressResponse(savedAddress.getId(), "Address created successfully");
        } catch (Exception e) {
            return new HospitalAddressResponse(-1, "Failed to create address: " + e.getMessage());
        }
    }

//    @Transactional
//    public HospitalAddressResponse updateAddress(HospitalAddress newAddress) {
//        try {
//            Optional<HospitalAddress> addressOptional = hospitalAddressRepository.findById(newAddress.getId());
//
//            if (addressOptional.isPresent()) {
//                HospitalAddress existingAddress = addressOptional.get();
//                existingAddress.setStreetNumber(newAddress.getStreetNumber());
//                existingAddress.setStreetName(newAddress.getStreetName());
//                existingAddress.setCity(newAddress.getCity());
//                HospitalAddress savedAddress = hospitalAddressRepository.save(existingAddress);
//
//                return new HospitalAddressResponse(savedAddress.getId(), "Address updated successfully");
//            } else {
//                return createAddress(newAddress);
//            }
//        } catch (Exception e) {
//            logger.error("Failed to update address", e);
//            return new HospitalAddressResponse(-1, "Failed to update address: " + e.getMessage());
//        }
//    }

    @Transactional
    public HospitalAddressResponse updateAddress(HospitalAddress newAddress) {
        System.out.println("NEW ADDRESS ID: "+newAddress.getId());
        System.out.println("NEW ADDRESS HOSPITAL ID: "+newAddress.getHospitalId());
        Optional<HospitalAddress> addressOptional = hospitalAddressRepository.findAddressByHospitalId(newAddress.getHospitalId());
        System.out.println("addressOptional: "+ addressOptional);
        if (addressOptional.isPresent()) {
            HospitalAddress existingAddress = addressOptional.get();
            existingAddress.setStreetNumber(newAddress.getStreetNumber());
            existingAddress.setStreetName(newAddress.getStreetName());
            existingAddress.setCity(newAddress.getCity());
            HospitalAddress savedAddress = hospitalAddressRepository.save(existingAddress);
            System.out.println("savedAddress hos ID: "+ savedAddress.getHospitalId());
            return new HospitalAddressResponse(savedAddress.getId(), "Address updated successfully");
        } else {
            return createAddress(newAddress);
        }
    }

    public HospitalAddress getAddressByHospitalId(Long hospitalId) {
        try {
            return hospitalAddressRepository.findByHospitalId(hospitalId).stream().findFirst().orElse(null);
        } catch (Exception e) {
            System.out.println("Error finding address by hospital ID: " + e.getMessage());
            return null;
        }
    }

    public List<HospitalAddress> getAllAddresses() {
        try {

            return hospitalAddressRepository.findAll();
        } catch (Exception e) {
            System.out.println("Error fetching all adresses: " + e.getMessage());
            return null;
        }
    }
}