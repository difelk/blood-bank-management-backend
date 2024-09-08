package com.bcn.bmc.services;

import com.bcn.bmc.enums.BloodType;
import com.bcn.bmc.models.BloodTypeDTO;
import com.bcn.bmc.models.Organization;
import com.bcn.bmc.models.UserAuthorize;
import com.bcn.bmc.repositories.BloodTypeRepository;
import com.bcn.bmc.repositories.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InfoService {


    @Autowired
    private BloodTypeRepository bloodTypeRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    public List<BloodTypeDTO> getAllBloodGroups() {
        return bloodTypeRepository.findAll().stream()
                .map(bloodTypeEntity -> new BloodTypeDTO(
                        bloodTypeEntity.getBloodTypeEnum().name(),
                        bloodTypeEntity.getBloodTypeEnum().getDisplayName()
                ))
                .collect(Collectors.toList());
    }


    public List<Organization> getAllOrganizations(UserAuthorize admin) {
        System.out.println("admin org : " + admin.getOrganization());
        if(admin.getOrganization() == 1){
            return organizationRepository.findAll();
        }else{
            return organizationRepository.findAllByOrganization(admin.getOrganization());
        }

    }
}
