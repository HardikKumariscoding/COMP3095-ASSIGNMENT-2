package ca.gbc.eventservice.service;

import ca.gbc.eventservice.model.Booking;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BookingParser {

    // ObjectMapper instance to parse JSON
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Method to parse the JSON message and extract fields
    public Booking parseBooking(String message) {
        try {
            // Deserialize the JSON string into the Booking object
            return objectMapper.readValue(message, Booking.class);
        } catch (Exception e) {
            System.err.println("Error parsing the message: " + e.getMessage());
            return null;
        }
    }
}

