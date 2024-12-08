package ca.gbc.bookingservice.client;

import ca.gbc.bookingservice.config.RoomClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "room-client", url = "${room.service.url}/api/room",fallback = RoomClientFallback.class)
public interface RoomClient {
    @RequestMapping(method = RequestMethod.GET, value="/checkAvailability")
    boolean isAvailable(@RequestParam("roomId") String roomId);

    @RequestMapping(method = RequestMethod.POST, value="/updateAvailability")
    void updateAvailability(@RequestParam("roomId") String roomId, @RequestParam("availability") Boolean availability);
}