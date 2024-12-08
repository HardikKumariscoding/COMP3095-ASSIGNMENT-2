package ca.gbc.eventservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.UUID;

@Data
@Builder
@Document(collection = "events")
@AllArgsConstructor
@NoArgsConstructor
public class Event {

    @Id
    private String id;
    private String eventName;
    private String organizerId;
    private String roomId;
    private String eventType;
    private int expectedAttendees;
    @Builder.Default
    private String status = "processing";
    // Constructor to initialize with bookingId, roomId, userId
    public Event(String bookingId, String roomId, String userId) {
        this.id = bookingId;  // bookingId as the id
        this.eventName = UUID.randomUUID().toString(); // Random UUID for eventName
        this.organizerId = userId;  // userId as the organizerId
        this.roomId = roomId;  // roomId from the booking event
        this.eventType = "workshop"; // Default to "workshop"
        this.expectedAttendees = 50; // Default to 50
    }
}
