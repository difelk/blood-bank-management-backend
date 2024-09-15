package com.bcn.bmc.repositories;

import com.bcn.bmc.models.EventAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventAddressRepository extends JpaRepository<EventAddress, Long> {
}

