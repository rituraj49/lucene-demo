package cl.lcd.dto.booking;

import cl.lcd.dto.search.FlightAvailabilityResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class FlightBookingResponse {
    @Schema(description = "Unique identifier for the flight booking order")
    private String orderId;
    private List<TravelerResponseDto> travelers;
    private FlightAvailabilityResponse flightOffer;
}
