package ca.gbc.bookingservice.service;

import ca.gbc.bookingservice.dto.BookingRequest;
import ca.gbc.bookingservice.dto.BookingResponse;
import ca.gbc.bookingservice.repository.BookingRepository;
import lombok.AllArgsConstructor;
import main.java.ca.gbc.bookingservice.model.Booking;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final MongoTemplate mongoTemplate;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String bookingTopic = "booking-events";
    private final Logger LOG =  LoggerFactory.getLogger(BookingServiceImpl.class);

//    public BookingServiceImpl(KafkaTemplate<String, String> kafkaTemplate) {
//        this.kafkaTemplate = kafkaTemplate;
//    }

    @Override
    public BookingResponse createBooking(BookingRequest bookingRequest) {
        LOG.debug("Creating a new booking for room ID: {}", bookingRequest.roomId());

        // Rename 'booking' to 'newBooking' to avoid conflict
        Booking newBooking = Booking.builder()
                .roomId(bookingRequest.roomId())
                .userId(bookingRequest.userId())
                .startTime(bookingRequest.startTime())
                .endTime(bookingRequest.endTime())
                .build();

        // Persist the booking to the MongoDB database
        bookingRepository.save(newBooking);
        LOG.info("Booking {} is saved", newBooking.getId());

        // Create a booking event
        String bookingEvent = String.format(
                "{ \"bookingId\": \"%s\", \"roomId\": \"%s\", \"userId\": \"%s\", \"startTime\": \"%s\", \"endTime\": \"%s\" }",
                newBooking.getId(),
                newBooking.getRoomId(),
                newBooking.getUserId(),
                newBooking.getStartTime(),
                newBooking.getEndTime()
        );

        // Publish to Kafka
        kafkaTemplate.send(bookingTopic, newBooking.getId(), bookingEvent);
        System.out.println("Published booking event to Kafka: " + bookingEvent);
        // Return a response containing booking details
        return new BookingResponse(
                newBooking.getId(),
                newBooking.getRoomId(),
                newBooking.getUserId(),
                newBooking.getStartTime(),
                newBooking.getEndTime()
        );
    }

    @Override
    public Optional<Booking> getBookingById(String id) {
        LOG.debug("Retrieving booking with id: {}", id);

        // Use MongoTemplate to find the booking by ID
        Query query = new Query(Criteria.where("id").is(id));
        Booking foundBooking = mongoTemplate.findOne(query, Booking.class);

        if (foundBooking != null) {
            LOG.info("Booking {} found", id);
            return Optional.of(foundBooking);
        } else {
            LOG.warn("Booking with id {} not found", id);
            return Optional.empty();
        }
    }

    @Override
    public List<BookingResponse> getAllBookings() {
        LOG.debug("Returning a list of all bookings");

        // Retrieve all bookings using the repository
        List<Booking> bookings = bookingRepository.findAll();
        return bookings.stream()
                .map(this::mapToBookingResponse)
                .toList();
    }

    private BookingResponse mapToBookingResponse(Booking bookingEntity) {
        // Mapping method to convert booking to BookingResponse
        return new BookingResponse(
                bookingEntity.getId(),
                bookingEntity.getRoomId(),
                bookingEntity.getUserId(),
                bookingEntity.getStartTime(),
                bookingEntity.getEndTime()
        );
    }

    @Override
    public String updateBooking(String id, BookingRequest bookingRequest) {
        LOG.debug("Updating booking with id {}", id);

        // Use MongoTemplate to find the booking by ID
        Query query = new Query(Criteria.where("id").is(id));
        Booking foundBooking = mongoTemplate.findOne(query, Booking.class);

        if (foundBooking != null) {
            // Update the booking details
            foundBooking.setRoomId(bookingRequest.roomId());
            foundBooking.setUserId(bookingRequest.userId());
            foundBooking.setStartTime(bookingRequest.startTime());
            foundBooking.setEndTime(bookingRequest.endTime());

            // Save the updated booking
            bookingRepository.save(foundBooking);
            LOG.info("Booking {} updated", id);
            return foundBooking.getId();
        }

        LOG.info("Booking with id {} not found", id);
        return null;
    }

    @Override
    public void deleteBooking(String id) {
        LOG.debug("Deleting booking with id {}", id);

        // Use MongoTemplate to find the booking by ID
        Query query = new Query(Criteria.where("id").is(id));
        Booking foundBooking = mongoTemplate.findOne(query, Booking.class);

        if (foundBooking != null) {
            // Delete the booking from the repository
            bookingRepository.delete(foundBooking);
            LOG.info("Booking {} deleted", id);
        } else {
            LOG.warn("Booking with id {} not found", id);
        }
    }
}
