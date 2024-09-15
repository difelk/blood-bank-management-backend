package com.bcn.bmc.services;

import com.bcn.bmc.enums.ActiveStatus;
import com.bcn.bmc.enums.EventStatus;
import com.bcn.bmc.models.*;
import com.bcn.bmc.repositories.*;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.*;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final EventAddressRepository eventAddressRepository;
    private final EventDocumentRepository eventDocumentRepository;

    private final UserRepository userRepository;

    public EventService(EventRepository eventRepository, EventAddressRepository eventAddressRepository, EventDocumentRepository eventDocumentRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.eventAddressRepository = eventAddressRepository;
        this.eventDocumentRepository = eventDocumentRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public EventResponse createEvent(UserAuthorize userAuthorize, Event event) {
        try {

            if (eventRepository.existsByEventName(event.getEventName())) {
                return new EventResponse("Failure", "Event name already exists.");
            }

            User createdByUser = userRepository.findById(userAuthorize.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found."));


            event.setCreatedBy(createdByUser);
            event.setCreatedDate(LocalDate.now());
            event.setStatus(ActiveStatus.ACTIVE);


            Event newEvent = eventRepository.save(event);
            return new EventResponse("Success", "Event created successfully.");
        } catch (Exception e) {
            throw new RuntimeException("Error creating event: " + e.getMessage(), e);
        }
    }


    @Transactional
    public EventResponse updateEvent(UserAuthorize userAuthorize, Event updatedEvent) {
        try {
            if (eventRepository.existsByEventNameAndIdNot(updatedEvent.getEventName(), updatedEvent.getId())) {
                return new EventResponse("Failure", "Event name already exists.");
            }
            Event existingEvent = eventRepository.findById(updatedEvent.getId())
                    .orElseThrow(() -> new RuntimeException("Event not found for ID: " + updatedEvent.getId()));
            existingEvent.setEventName(updatedEvent.getEventName());
            existingEvent.setEventStartDate(updatedEvent.getEventStartDate());
            existingEvent.setEventEndDate(updatedEvent.getEventEndDate());
            existingEvent.setEventLocation(updatedEvent.getEventLocation());
            existingEvent.setEventStatus(updatedEvent.getEventStatus());
            existingEvent.setContactNo(updatedEvent.getContactNo());
            eventRepository.save(existingEvent);
            return new EventResponse("Success", "Event updated successfully.");
        } catch (Exception e) {
            throw new RuntimeException("Error updating event: " + e.getMessage(), e);
        }
    }

    @Transactional
    public EventResponse updateEventStatus(UserAuthorize userAuthorize, Long eventId, EventStatus status) {
        try {
            Event event = eventRepository.findById(eventId)
                    .orElseThrow(() -> new RuntimeException("Event not found for ID: " + eventId));
            event.setEventStatus(status);
            eventRepository.save(event);
            return new EventResponse("Success", "Event status updated successfully.");
        } catch (Exception e) {
            throw new RuntimeException("Error updating event status: " + e.getMessage(), e);
        }
    }
    @Transactional
    public EventAddressResponse createEventAddress(UserAuthorize userAuthorize, Long eventId, EventAddress eventAddress) {
        try {
            Event event = eventRepository.findById(eventId)
                    .orElseThrow(() -> new RuntimeException("Event not found."));

            eventAddress.setEvent(event);
            eventAddressRepository.save(eventAddress);
            return new EventAddressResponse("Success", "Event address created successfully.");
        } catch (Exception e) {
            throw new RuntimeException("Error creating event address: " + e.getMessage(), e);
        }
    }


    @Transactional
    public EventDocumentResponse createEventDocument(UserAuthorize userAuthorize, Long eventId, EventDocument eventDocument) {
        try {
            Event event = eventRepository.findById(eventId)
                    .orElseThrow(() -> new RuntimeException("Event not found."));

            eventDocument.setEvent(event);
            eventDocumentRepository.save(eventDocument);
            return new EventDocumentResponse("Success", "Event document created successfully.");
        } catch (Exception e) {
            throw new RuntimeException("Error creating event document: " + e.getMessage(), e);
        }
    }



    @Transactional
    public EventAddress updateEventAddress(UserAuthorize userAuthorize, Long eventId, EventAddress eventAddress) {
        try {
            Event event = eventRepository.findById(eventId)
                    .orElseThrow(() -> new RuntimeException("Event not found"));

            EventAddress existingAddress = eventAddressRepository.findById(eventAddress.getId())
                    .orElseThrow(() -> new RuntimeException("Event address not found"));

            existingAddress.setStreetNumber(eventAddress.getStreetNumber());
            existingAddress.setStreetName(eventAddress.getStreetName());
            existingAddress.setCity(eventAddress.getCity());
            existingAddress.setState(eventAddress.getState());
            existingAddress.setZipCode(eventAddress.getZipCode());

            existingAddress.setEvent(event);

            return eventAddressRepository.save(existingAddress);

        } catch (RuntimeException e) {
            throw new RuntimeException("Error updating event address: " + e.getMessage(), e);
        }
    }



    @Transactional
    public EventDocument updateEventDocument(UserAuthorize userAuthorize, Long eventId, EventDocument eventDocument) {
        try {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        EventDocument existingDocs = eventDocumentRepository.findById(eventDocument.getId())
                .orElseThrow(() -> new RuntimeException("Event document not found"));

        existingDocs.setFileName(eventDocument.getFileName());
        existingDocs.setFileType(eventDocument.getFileType());
        existingDocs.setFileSize(eventDocument.getFileSize());
        existingDocs.setData(eventDocument.getData());

        existingDocs.setEvent(event);

        return eventDocumentRepository.save(existingDocs);

    } catch (RuntimeException e) {
        throw new RuntimeException("Error updating event document: " + e.getMessage(), e);
    }
    }

    @Transactional
    public ResponseEntity<EventDocumentResponse> deleteEventDocumentById(Long documentId) {
        try {
            if (!eventDocumentRepository.existsById(documentId)) {
                return new ResponseEntity<>(new EventDocumentResponse("Failure", "Document not found"), HttpStatus.NOT_FOUND);
            }
            eventDocumentRepository.deleteById(documentId);
            return new ResponseEntity<>(new EventDocumentResponse("Success", "Document deleted successfully"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new EventDocumentResponse("Failure", "Error deleting document"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<EventAddress> getAllAddresses() {
        try {
            return eventAddressRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching all event addresses: " + e.getMessage(), e);
        }
    }

    public List<EventDocument> getAllDocumentsById(Long eventId) {
        try {
            return eventDocumentRepository.findByEventId(eventId);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching event documents: " + e.getMessage(), e);
        }
    }

    public List<Event> getAllEvents(UserAuthorize userAuthorize) {
        try {
            return eventRepository.findByStatus(ActiveStatus.ACTIVE);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching all events: " + e.getMessage(), e);
        }
    }

    public Event getEventById(Long eventId) {
        try {
            return eventRepository.findById(eventId)
                    .orElseThrow(() -> new RuntimeException("Event not found for ID: " + eventId));
        } catch (Exception e) {
            throw new RuntimeException("Error fetching event by ID: " + e.getMessage(), e);
        }
    }

    @Transactional
    public EventResponse deleteEventById(Long eventId) {
        try {
            Event event = eventRepository.findById(eventId)
                    .orElseThrow(() -> new RuntimeException("Event not found for ID: " + eventId));
            event.setStatus(ActiveStatus.INACTIVE);
            eventRepository.save(event);
            return new EventResponse("Success", "Event marked as inactive successfully.");
        } catch (Exception e) {
            throw new RuntimeException("Error marking event as inactive: " + e.getMessage(), e);
        }
    }
}
