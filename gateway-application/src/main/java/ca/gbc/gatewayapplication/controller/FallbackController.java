package ca.gbc.gatewayapplication.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

    @GetMapping("/fallback/users")
    public ResponseEntity<String> fallbackUsers() {
        return ResponseEntity.ok("User service is currently unavailable. Please try again later.");
    }

    @GetMapping("/fallback/approvals")
    public ResponseEntity<String> fallbackApprovals() {
        return ResponseEntity.ok("Approval service is currently unavailable. Please try again later.");
    }

    @GetMapping("/fallback/room")
    public ResponseEntity<String> fallbackRoom() {
        return ResponseEntity.ok("Room service is currently unavailable. Please try again later.");
    }

    @GetMapping("/fallback/events")
    public ResponseEntity<String> fallbackEvents() {
        return ResponseEntity.ok("Event service is currently unavailable. Please try again later.");
    }

    @GetMapping("/fallback/bookings")
    public ResponseEntity<String> fallbackBookings() {
        return ResponseEntity.ok("Booking service is currently unavailable. Please try again later.");
    }
}
