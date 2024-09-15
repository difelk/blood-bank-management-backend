package com.bcn.bmc.controllers;

import com.bcn.bmc.enums.EventStatus;
import com.bcn.bmc.helper.TokenData;
import com.bcn.bmc.models.*;
import com.bcn.bmc.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/event")
public class EventController {

    private final EventService eventService;

    @Autowired
    private TokenData tokenHelper;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/create")
    public ResponseEntity<EventResponse> createEvent(@RequestHeader("Authorization") String tokenHeader,
                                                     @RequestBody Event event) {
        UserAuthorize userAuthorize = tokenHelper.parseToken(tokenHeader);
        return ResponseEntity.ok(eventService.createEvent(userAuthorize, event));
    }

    @PutMapping("/update")
    public ResponseEntity<EventResponse> updateEvent(@RequestHeader("Authorization") String tokenHeader,
                                                     @RequestBody Event updatedEvent) {
        UserAuthorize userAuthorize = tokenHelper.parseToken(tokenHeader);
        return ResponseEntity.ok(eventService.updateEvent(userAuthorize, updatedEvent));
    }

    @PutMapping("/status/{eventId}")
    public ResponseEntity<EventResponse> updateEventStatus(@RequestHeader("Authorization") String tokenHeader,
                                                           @PathVariable Long eventId,
                                                           @RequestParam EventStatus status) {
        UserAuthorize userAuthorize = tokenHelper.parseToken(tokenHeader);
        return ResponseEntity.ok(eventService.updateEventStatus(userAuthorize, eventId, status));
    }

    @PostMapping("/address/{eventId}")
    public ResponseEntity<EventAddressResponse> createEventAddress(@RequestHeader("Authorization") String tokenHeader,
                                                                   @PathVariable Long eventId,
                                                                   @RequestBody EventAddress eventAddress) {
        UserAuthorize userAuthorize = tokenHelper.parseToken(tokenHeader);
        return ResponseEntity.ok(eventService.createEventAddress(userAuthorize, eventId, eventAddress));
    }

//    @PostMapping("/documents/{eventId}")
//    public ResponseEntity<EventDocumentResponse> createEventDocument(
//            @RequestHeader("Authorization") String tokenHeader,
//            @PathVariable Long eventId,
//            @RequestPart("eventDocument") EventDocument eventDocument,
//            @RequestParam("file") MultipartFile file) {
//
//        UserAuthorize userAuthorize = tokenHelper.parseToken(tokenHeader);
//        try {
//            EventDocumentResponse response = eventService.createEventDocument(userAuthorize, eventId, eventDocument, file);
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new EventDocumentResponse("Error", "Failed to create event document: " + e.getMessage()));
//        }
//    }

    @PostMapping("/documents/upload")
    public ResponseEntity<EventDocument> uploadFile(@RequestParam("file") MultipartFile file,
                                                       @RequestParam("eventId") Long eventId) {
        try {
            EventDocument savedDocument = eventService.storeFile(file, eventId);
            return new ResponseEntity<>(savedDocument, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/address/{eventId}")
    public ResponseEntity<EventAddressResponse> updateEventAddress(
            @RequestHeader("Authorization") String tokenHeader,
            @PathVariable Long eventId,
            @RequestBody EventAddress eventAddress) {
        try {
            UserAuthorize userAuthorize = tokenHelper.parseToken(tokenHeader);

            EventAddress updatedAddress = eventService.updateEventAddress(userAuthorize, eventId, eventAddress);

            EventAddressResponse response = new EventAddressResponse("Success", "Event address updated successfully.");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            EventAddressResponse response = new EventAddressResponse("Error", "Failed to update event address: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @PutMapping("/documents/{eventId}")
    public ResponseEntity<EventDocumentResponse> updateEventDocument(@RequestHeader("Authorization") String tokenHeader,
                                                                     @PathVariable Long eventId,
                                                                     @RequestBody EventDocument eventDocument) {

        try {
            UserAuthorize userAuthorize = tokenHelper.parseToken(tokenHeader);
           eventService.updateEventDocument(userAuthorize, eventId, eventDocument);
            EventDocumentResponse response = new EventDocumentResponse("Success", "Event document updated successfully.");
            return ResponseEntity.ok(response);
        }catch (Exception e) {
            EventDocumentResponse response = new EventDocumentResponse("Error", "Failed to update event document: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/documents/{documentId}")
    public ResponseEntity<EventDocumentResponse> deleteEventDocumentById(@PathVariable Long documentId) {
        return eventService.deleteEventDocumentById(documentId);
    }

    @GetMapping("/addresses")
    public ResponseEntity<List<EventAddress>> getAllAddresses() {
        return ResponseEntity.ok(eventService.getAllAddresses());
    }

    @GetMapping("/documents/{eventId}")
    public ResponseEntity<List<EventDocument>> getAllDocumentsById(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.getAllDocumentsById(eventId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Event>> getAllEvents(@RequestHeader("Authorization") String tokenHeader) {
        UserAuthorize userAuthorize = tokenHelper.parseToken(tokenHeader);
        return ResponseEntity.ok(eventService.getAllEvents(userAuthorize));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<Event> getEventById(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.getEventById(eventId));
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<EventResponse> deleteEventById(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.deleteEventById(eventId));
    }

    @GetMapping("/address/{eventId}")
    public EventAddress getAddressByHospital(@PathVariable Long eventId) {
        return eventService.getAddressByEventId(eventId);
    }

    @GetMapping("/name/{name}")
    public Event getEventByName(@PathVariable String name) {
        System.out.println("Fetching event by name: " + name);
        return eventService.getEventByName(name);
    }

}

