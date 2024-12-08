package ca.gbc.eventservice.service;

import ca.gbc.eventservice.model.Booking;
import ca.gbc.eventservice.model.Event;
import ca.gbc.eventservice.repository.EventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class EventConsumptionService {

    private final EventRepository eventRepository;
    BookingParser parser = new BookingParser();




    @Autowired
    public EventConsumptionService(EventRepository eventRepository, ObjectMapper objectMapper) {
        this.eventRepository = eventRepository;
    }

    public void registerEvent(Event event) {
        System.out.println("Registering event for confirmed bookingId: " + event.getId());
        eventRepository.save(event);
    }

    @KafkaListener(topics = "${spring.kafka.topic.booking}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeBookingEvent(String message) {
        try {
            System.out.println("Consumed booking event: " + message);

            Booking booking = parser.parseBooking(message);
            Event event = new Event(
                    booking.getBookingId(),booking.getRoomId(),booking.getUserId()

            );

                registerEvent(event);
        } catch (Exception e) {
            System.err.println("Failed to process booking event: " + message + ". Error: " + e.getMessage());
        }
    }


}

