package cl.lcd.dto.booking;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class FlightBookingRequest {
    @Schema(description = "bookingAdditionalInfo field from FlightPricingConfirmResponse")
    private String flightOffer;
    private List<TravelerRequestDto> travelers;
}
