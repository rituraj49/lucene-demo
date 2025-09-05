package cl.lcd.dto.pricing;

import cl.lcd.dto.search.FlightAvailabilityResponse;
import com.amadeus.resources.FlightOfferSearch;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.google.gson.Gson;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FlightPricingConfirmResponse {
    @JsonUnwrapped
    private FlightAvailabilityResponse flightOffer;

    @Schema(description = "flight offer search json object as is from pricing confirm response")
    private String bookingAdditionalInfo;

   @JsonIgnore
    public void setBookingAdditionalInfo(FlightOfferSearch offer) {
        this.bookingAdditionalInfo = new Gson().toJson(offer);
    }
}