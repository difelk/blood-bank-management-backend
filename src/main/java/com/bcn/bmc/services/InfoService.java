package com.bcn.bmc.services;

import com.bcn.bmc.enums.BloodType;
import com.bcn.bmc.models.BloodTypeDTO;
import com.bcn.bmc.repositories.BloodTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InfoService {


    @Autowired
    private BloodTypeRepository bloodTypeRepository;

    public List<BloodTypeDTO> getAllBloodGroups() {
        return bloodTypeRepository.findAll().stream()
                .map(bloodTypeEntity -> new BloodTypeDTO(
                        bloodTypeEntity.getBloodTypeEnum().name(),
                        bloodTypeEntity.getBloodTypeEnum().getDisplayName()
                ))
                .collect(Collectors.toList());
    }
}
