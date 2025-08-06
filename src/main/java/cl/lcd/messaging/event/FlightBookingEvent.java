package cl.lcd.messaging.event;

import cl.lcd.dto.booking.FlightBookingResponse;
import cl.lcd.dto.booking.TravelerResponseDto;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class FlightBookingEvent {
//    private FlightBookingResponse bookingResponse;
    private String orderId;
    private List<TravelerResponseDto> travelers;
    private KafkaFlightAvailabilityResponse flightOffer;
    private Instant createdAt;
}