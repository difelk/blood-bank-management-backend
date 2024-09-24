package com.bcn.bmc.services;

import com.bcn.bmc.enums.BloodType;
import com.bcn.bmc.models.*;
import com.bcn.bmc.repositories.BloodTypeRepository;
import com.bcn.bmc.repositories.HospitalRepository;
import com.bcn.bmc.repositories.OrganizationRepository;
import com.bcn.bmc.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InfoService {


    @Autowired
    private BloodTypeRepository bloodTypeRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private HospitalRepository hospitalRepository;
    @Autowired
    private UserRepository userRepository;

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
        if (admin.getOrganization() == 1) {
            return organizationRepository.findAll();
        } else {
            return organizationRepository.findAllByOrganization(admin.getOrganization());
        }

    }

    public List<CountSummary> getAllOrganizationsUsers(UserAuthorize admin) {
        List<CountSummary> organizationsUsers = new ArrayList<>();
        try {
            if((admin.getOrganization() == 1)) {
                int organizationCount = hospitalRepository.getTotalHospitalsCount();
                int organizationActiveCount = hospitalRepository.getTotalActiveHospitals();
                int organizationDeActiveCount = hospitalRepository.getTotalDeActiveHospitals();
                organizationsUsers.add(new CountSummary("Total Registered Organizations", organizationCount, organizationActiveCount, organizationDeActiveCount));
                int totalUsersCount = userRepository.getTotalUsers();
                int activeUsersCount = userRepository.getTotalActiveUsers();
                int deActiveUsersCount = userRepository.getTotalDeActiveUsers();
                organizationsUsers.add(new CountSummary("Total Users", totalUsersCount, activeUsersCount, deActiveUsersCount));
            }else{
                int organizationCount = hospitalRepository.getTotalHospitalsCountByOrgId(admin.getOrganization());
                int organizationActiveCount = hospitalRepository.getTotalActiveHospitalsByOrgId(admin.getOrganization());
                int organizationDeActiveCount = hospitalRepository.getTotalDeActiveHospitalsByOrgId(admin.getOrganization());
                organizationsUsers.add(new CountSummary("Total Registered Organizations", organizationCount, organizationActiveCount, organizationDeActiveCount));
                int totalUsersCount = userRepository.getTotalUsersByOrgId(admin.getOrganization());
                int activeUsersCount = userRepository.getTotalActiveUsersByOrgId(admin.getOrganization());
                int deActiveUsersCount = userRepository.getTotalDeActiveUsersByOrgId(admin.getOrganization());
                organizationsUsers.add(new CountSummary("Total Users", totalUsersCount, activeUsersCount, deActiveUsersCount));
            }
            return organizationsUsers;
        } catch (Exception e) {
            System.out.println("get count error: " + e.getMessage());
        }
        return organizationsUsers;

    }
}
