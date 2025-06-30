package cl.lcd.dto.booking;

import cl.lcd.dto.search.FlightAvailabilityResponse;
import lombok.Data;

import java.util.List;

@Data
public class FlightBookingResponse {
    private String orderId;
    private List<TravelerResponseDto> travelers;
    private FlightAvailabilityResponse flightOffer;
}
