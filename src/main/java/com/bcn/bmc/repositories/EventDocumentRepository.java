package com.bcn.bmc.repositories;

import com.bcn.bmc.models.EventDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventDocumentRepository extends JpaRepository<EventDocument, Long> {
    List<EventDocument> findByEventId(Long eventId);
}

