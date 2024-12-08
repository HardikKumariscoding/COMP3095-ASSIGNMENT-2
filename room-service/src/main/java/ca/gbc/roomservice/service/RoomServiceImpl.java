package ca.gbc.roomservice.service;

import ca.gbc.roomservice.dto.RoomRequest;
import ca.gbc.roomservice.dto.RoomResponse;
import ca.gbc.roomservice.model.Room;
import ca.gbc.roomservice.repository.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RoomServiceImpl implements RoomService  {
    private  final RoomRepository roomRepository;
    private final Logger LOG =  LoggerFactory.getLogger(RoomServiceImpl.class);
    @Override
    public RoomResponse createRoom(RoomRequest roomRequest){
        Room room = Room.builder()
                .roomName(roomRequest.roomName())
                .capacity(roomRequest.capacity())
                .features(roomRequest.features())
                .availability(roomRequest.availability())
                .build();
        roomRepository.save(room);
        LOG.info("room {} is saved",room.getRoomID());
        return new RoomResponse(room.getRoomID(),room.getRoomName(),room.getCapacity(),room.getFeatures(),room.isAvailability());
    }
    @Override
    public List<RoomResponse> getallRooms() {
        List<Room> rooms = roomRepository.findAll();
        LOG.info("All rooms are {}",rooms);
        return rooms.stream()
                .map(this::mapToRoomResponse)
                .collect(Collectors.toList());
    }
    private  RoomResponse mapToRoomResponse(Room room){
        return new RoomResponse(room.getRoomID(),room.getRoomName(),room.getCapacity(),room.getFeatures(),room.isAvailability());
    }
    @Override
    public RoomResponse getRoomById(Long roomId) {
        LOG.info("Room ID is {},",roomId);
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        return mapToRoomResponse(room);
    }

    @Override
    public String updateRoom(Long roomId, RoomRequest roomRequest) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        LOG.info("Updation of room {},",roomId);
        room.setAvailability(roomRequest.availability());
        roomRepository.save(room);
        return "Room availability updated successfully";
    }
    @Override
    public void deleteRoom(Long roomId) {
        LOG.info("Deletion of roo with id {},",roomId);
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        roomRepository.delete(room);
    }

}
