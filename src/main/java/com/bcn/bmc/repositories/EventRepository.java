package com.bcn.bmc.repositories;

import com.bcn.bmc.enums.ActiveStatus;
import com.bcn.bmc.enums.EventStatus;
import com.bcn.bmc.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findByEventName(String eventName);
    List<Event> findByStatus(ActiveStatus status);

    boolean existsByEventName(String eventName);
    boolean existsByEventNameAndIdNot(String eventName, Long id);
}

