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

//    @Query("SELECT new com.bcn.bmc.models.HospitalJoinedDetails(" +
//            "h.id, h.hospitalName, h.contactNo1, h.contactNo2, h.sector, " +
//            "ha.hospitalId, ha.streetNumber, ha.streetName, ha.city) " +
//            "FROM Hospital h " +
//            "LEFT JOIN HospitalAddress ha ON h.id = ha.hospitalId " +
//            "WHERE h.status <> 'INACTIVE'")
//    List<HospitalJoinedDetails> findHospitalsWithAddresses();
//
//    @Query("SELECT hd FROM HospitalDocument hd WHERE hd.hospital.id = :hospitalId")
//    List<HospitalDocument> findDocumentsByHospitalId(@Param("hospitalId") Long hospitalId);



}