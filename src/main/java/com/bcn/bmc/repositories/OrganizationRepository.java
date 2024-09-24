package com.bcn.bmc.repositories;

import com.bcn.bmc.models.Address;
import com.bcn.bmc.models.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrganizationRepository extends JpaRepository<Organization, Integer> {

    @Query("select o from Organization o where o.id = ?1")
    Optional<Organization> findOrganizationById(@Param("id") int id);

    @Query("select o from Organization o where o.id = :organization")
    List<Organization> findAllByOrganization(@Param("organization") int organization);


    @Query("SELECT COUNT(o) FROM Organization o")
    int getTotalOrganizationsCount();

    @Query("SELECT COUNT(o) FROM Organization o")
    int getTotalActiveOrganizations();


}