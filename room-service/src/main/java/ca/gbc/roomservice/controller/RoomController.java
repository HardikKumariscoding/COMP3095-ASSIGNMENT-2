package ca.gbc.roomservice.controller;

import ca.gbc.roomservice.dto.RoomRequest;
import ca.gbc.roomservice.dto.RoomResponse;
import ca.gbc.roomservice.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/room")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;


    @Operation(
            summary = "Endpoint to create a room",
            description = "Endpoint to create a room"
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RoomResponse createRoom(@RequestBody RoomRequest roomRequest) {
        return roomService.createRoom(roomRequest);
    }

    @Operation(
            summary = "Endpoint to get all rooms",
            description = "Endpoint to get all rooms"
    )
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<RoomResponse> getAllRooms() {
        return roomService.getallRooms();
    }

    @Operation(
            summary = "Endpoint to get a room by ID",
            description = "Endpoint to get a room by ID"
    )
    @GetMapping("/{roomId}")
    @ResponseStatus(HttpStatus.OK)
    public RoomResponse getRoomById(@PathVariable("roomId") Long roomId) {
        return roomService.getRoomById(roomId);
    }


    @Operation(
            summary = "Endpoint to update room avilability",
            description = "Endpoint to update room avilability"
    )
    @PatchMapping("/{roomId}/availability")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> updateRoomAvailability(@PathVariable("roomId") Long roomId,
                                                    @RequestBody RoomRequest roomRequest) {
        String updateRoomId = roomService.updateRoom(roomId, roomRequest);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/rooms/" + updateRoomId);
        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }

    @Operation(
            summary = "Endpoint to delete a room",
            description = "Endpoint to delete a room"
    )
    @DeleteMapping("/{roomId}")
    public ResponseEntity<?> deleteRoom(@PathVariable("roomId") Long roomId) {
        roomService.deleteRoom(roomId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @Operation(
            summary = "Endpoint to check room availability",
            description = "Endpoint to check room availability"
    )

    @GetMapping("/checkAvailability")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Boolean> checkRoomAvailability(@RequestParam String roomId) {
        RoomResponse room = roomService.getRoomById(Long.valueOf(roomId));
        return new ResponseEntity<>(room.availability(), HttpStatus.OK);
    }
    @Operation(
            summary = "Endpoint to update room availability",
            description = "Endpoint to update room availability"
    )
    @PostMapping("/updateAvailability")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> updateRoomAvailability(@RequestParam String roomId, @RequestParam Boolean availability) {
        RoomResponse roomRes = getRoomById(Long.valueOf(roomId));
        RoomRequest roomRequest = new RoomRequest(
                roomRes.roomId(),roomRes.roomName(), roomRes.capacity(),roomRes.features(), availability
        );
        updateRoomAvailability(roomRes.roomId(), roomRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
