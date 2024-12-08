package ca.gbc.bookingservice.config;

import ca.gbc.bookingservice.client.RoomClient;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class RoomClientFallback implements FallbackFactory<RoomClient> {



    @Override
    public RoomClient create(Throwable cause) {
        return new RoomClient() {
            @Override
            public boolean isAvailable(String roomId) {
                System.out.println("Fallback: Room Client unavailable" + roomId);
                return false;
            }

            @Override
            public void updateAvailability(String roomId, Boolean availability) {
                System.out.println("Fallback: Unable to update room availability for roomId " + roomId);
            }
        };
    }
}
