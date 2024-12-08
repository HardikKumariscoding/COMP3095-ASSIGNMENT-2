package ca.gbc.eventservice.service;


import ca.gbc.eventservice.model.Event;
import ca.gbc.eventservice.repository.EventRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final Logger LOG =  LoggerFactory.getLogger(EventService.class);

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    // Create or update event
    public Optional<Event> getEventById(String eventId) {
        LOG.info("get Event by ID");return eventRepository.findById(eventId); }
    public Event createOrUpdateEvent(Event event) {
        LOG.info("Event creation");
        return eventRepository.save(event);
    }

    // Get all events
    public List<Event> getAllEvents() {

        LOG.info("Get all events ");return eventRepository.findAll();
    }

    // Get event by name
    public List<Event> getEventByName(String eventName) {
        LOG.info("get Event by Name");
        return eventRepository.findByEventName(eventName);
    }

    // Delete event by id
    public void deleteEvent(String eventId) {
        eventRepository.deleteById(eventId);
    }
}
