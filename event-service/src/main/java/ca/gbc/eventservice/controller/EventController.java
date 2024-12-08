package ca.gbc.eventservice.controller;

import ca.gbc.eventservice.client.RoomClient;
import ca.gbc.eventservice.client.UserClient;
import ca.gbc.eventservice.model.Event;
import ca.gbc.eventservice.service.EventService;
import lombok.AllArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/events")
@AllArgsConstructor

public class EventController {
    private final EventService eventService;
    private final UserClient userClient;
    private final RoomClient roomClient;

    @Operation(
            summary = "Create/update an Event by Event Response Body",
            description = "Create or Update an Event with Event Body"
    )
    @PostMapping
    public ResponseEntity<Event> createOrUpdateEvent(@RequestBody Event event) {

        if(!userClient.isValid(event.getOrganizerId())) {
            throw new RuntimeException("User id is invalid");
        }

        if(!roomClient.isAvailable(event.getRoomId())) {
            throw new RuntimeException("Room no " + event.getRoomId() + " is not available");
        }

        Event savedEvent = eventService.createOrUpdateEvent(event);
        return new ResponseEntity<>(savedEvent, HttpStatus.CREATED);
    }


    @GetMapping
    @Operation(
            summary = "Get all events",
            description = "Retrieves a list of all available events."
    )
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @GetMapping("/search")
    @Operation(
            summary = "Search events by name",
            description = "Retrieves events that match the specified name."
    )
    public ResponseEntity<List<Event>> getEventByName(@RequestParam("name") String eventName) {
        List<Event> events = eventService.getEventByName(eventName);
        if (events.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @Operation(
            summary = "Delete an event by ID",
            description = "Deletes an event specified by its unique ID. Returns no content if the deletion is successful."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable("id") String eventId) {
        eventService.deleteEvent(eventId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/isEventValid")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Check if an event is valid",
            description = "Checks if an event exists by its unique ID and returns a boolean value."
    )
    public ResponseEntity<Boolean> isEventValid(@RequestParam String eventId) {
        Optional<Event> eventOp = eventService.getEventById(eventId);
        return new ResponseEntity<>(eventOp.isPresent(), HttpStatus.OK);
    }

    @PostMapping("/updateEventStatus")
    @Operation(
            summary = "Update event status",
            description = "Updates the status of an event by its unique ID and the provided status value."
    )
    public ResponseEntity<Void> updateEventStatus(@RequestParam String eventId, @RequestParam String status) {
        Optional<Event> eventOp = eventService.getEventById(eventId);
        Event event = eventOp.get();
        event.setStatus(status);
        eventService.createOrUpdateEvent(event);
        return new ResponseEntity<>(HttpStatus.OK);
    }}