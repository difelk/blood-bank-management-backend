package com.bcn.bmc.repositories;

import com.bcn.bmc.models.EventAddress;
import com.bcn.bmc.models.HospitalAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventAddressRepository extends JpaRepository<EventAddress, Long> {

    List<EventAddress> findByEventId(Long eventId);



}

