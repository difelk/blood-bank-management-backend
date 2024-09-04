package com.bcn.bmc.services;

import com.bcn.bmc.models.Hospital;
import com.bcn.bmc.models.HospitalAddress;
import com.bcn.bmc.models.HospitalAddressResponse;
import com.bcn.bmc.repositories.HospitalAddressRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HospitalAddressService {

    @Autowired
    private HospitalAddressRepository hospitalAddressRepository;

    @Transactional
    public HospitalAddressResponse createAddress(HospitalAddress address) {
        try {
            HospitalAddress savedAddress = hospitalAddressRepository.save(address);
            return new HospitalAddressResponse(savedAddress.getId(), "Address created successfully");
        } catch (Exception e) {
            return new HospitalAddressResponse(-1, "Failed to create address: " + e.getMessage());
        }
    }

    @Transactional
    public HospitalAddressResponse updateAddress(HospitalAddress newAddress) {
        try {
            Optional<HospitalAddress> addressOptional = hospitalAddressRepository.findById(newAddress.getId());
            if (addressOptional.isPresent()) {
                HospitalAddress existingAddress = addressOptional.get();
                existingAddress.setStreetNumber(newAddress.getStreetNumber());
                existingAddress.setStreetName(newAddress.getStreetName());
                existingAddress.setCity(newAddress.getCity());
                HospitalAddress savedAddress = hospitalAddressRepository.save(existingAddress);
                return new HospitalAddressResponse(savedAddress.getId(), "Address updated successfully");
            } else {
                return createAddress(newAddress);
            }
        } catch (Exception e) {
            return new HospitalAddressResponse(-1, "Failed to update address: " + e.getMessage());
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