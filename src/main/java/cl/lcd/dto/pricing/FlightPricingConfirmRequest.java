package cl.lcd.dto.pricing;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class FlightPricingConfirmRequest {
    @Schema(description = "pricingAdditionalInfo field from FlightAvailabilityResponse")
    private String flightOffer;
}
