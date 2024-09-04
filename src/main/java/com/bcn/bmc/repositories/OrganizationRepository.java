package com.bcn.bmc.repositories;

import com.bcn.bmc.models.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository extends JpaRepository<Organization, Integer> {
}