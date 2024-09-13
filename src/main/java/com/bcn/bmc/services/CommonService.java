package com.bcn.bmc.services;

import com.bcn.bmc.models.Donor;
import com.bcn.bmc.models.KeyValue;
import com.bcn.bmc.models.Organization;
import com.bcn.bmc.models.User;
import com.bcn.bmc.repositories.DonorRepository;
import com.bcn.bmc.repositories.OrganizationRepository;
import com.bcn.bmc.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class CommonService {

        private final DonorRepository donorRepository;
        private final OrganizationRepository organizationRepository;
        private final UserRepository userRepository;

        @Autowired
        public CommonService(DonorRepository donorRepository, OrganizationRepository organizationRepository, UserRepository userRepository) {
            this.donorRepository = donorRepository;
            this.organizationRepository = organizationRepository;
            this.userRepository = userRepository;
        }

        public KeyValue fetchDonorKeyValue(Long donorId) {
            KeyValue donorKeyValue = new KeyValue();
            if (donorId != null) {
                Optional<Donor> donor = donorRepository.findById(donorId);
                donor.ifPresent(d -> {
                    donorKeyValue.setKey(d.getId());
                    donorKeyValue.setValue(d.getFirstName() + " " + d.getLastName());
                });
            }
            return donorKeyValue;
        }

        public KeyValue fetchOrganizationKeyValue(Long organizationId) {
            KeyValue organizationKeyValue = new KeyValue();
            if (organizationId != null) {
                Optional<Organization> organization = organizationRepository.findOrganizationById(organizationId.intValue());
                organization.ifPresent(org -> {
                    organizationKeyValue.setKey((long) org.getOrganizationId());
                    organizationKeyValue.setValue(org.getOrganizationName());
                });
            }
            return organizationKeyValue;
        }

        public KeyValue fetchUserKeyValue(Long userId) {
            KeyValue userKeyValue = new KeyValue();
            if (userId != null) {
                Optional<User> user = userRepository.findUserById(userId);
                user.ifPresent(u -> {
                    userKeyValue.setKey(u.getId());
                    userKeyValue.setValue(u.getFirstName() + " " + u.getLastName());
                });
            }
            return userKeyValue;
        }

}
