package com.bcn.bmc.repositories;

import com.bcn.bmc.enums.ActiveStatus;
import com.bcn.bmc.models.Hospital;
import com.bcn.bmc.models.HospitalDocument;
import com.bcn.bmc.models.HospitalJoinedDetails;
import com.bcn.bmc.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long> {
    Optional<Hospital> findByHospitalName(String hospitalName);

    List<Hospital> findBySector(String sector);

    List<Hospital> findAllByStatus(ActiveStatus status);

    @Query("select h from Hospital h where  h.id = :organization AND h.status <> 'INACTIVE'")
    List<Hospital> findAllByOrganizationId(@Param("organization") int organization);

    @Query("select h from Hospital h where  h.id = :organization AND h.status <> 'INACTIVE'")
    Hospital findAllByOrganizationIdLong(@Param("organization") long organization);

    @Query("select h from Hospital h where  h.id = :organization AND h.status <> 'INACTIVE'")
    Hospital findByOrganizationId(@Param("organization") long organization);

    @Query("SELECT COUNT(h) FROM Hospital h")
    int getTotalHospitalsCount();

    @Query("SELECT COUNT(h) FROM Hospital h WHERE h.status <> 'INACTIVE'")
    int getTotalActiveHospitals();

    @Query("SELECT COUNT(h) FROM Hospital h WHERE h.status = 'INACTIVE'")
    int getTotalDeActiveHospitals();


    @Query("SELECT COUNT(h) FROM Hospital h WHERE h.id = :organization")
    int getTotalHospitalsCountByOrgId(@Param("organization") long organization);

    @Query("SELECT COUNT(h) FROM Hospital h WHERE h.id = :organization AND h.status <> 'INACTIVE'")
    int getTotalActiveHospitalsByOrgId(@Param("organization") long organization);

    @Query("SELECT COUNT(h) FROM Hospital h WHERE h.id = :organization AND  h.status = 'INACTIVE'")
    int getTotalDeActiveHospitalsByOrgId(@Param("organization") long organization);



}