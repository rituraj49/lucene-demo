package cl.lcd.dto.pricing;

import cl.lcd.dto.search.FlightAvailabilityResponse;
import com.amadeus.resources.FlightOfferSearch;
import com.google.gson.Gson;
import lombok.Data;

@Data
public class FlightPricingConfirmResponse {
    private FlightAvailabilityResponse flightOffer;
    private String bookingAdditionalInfo;

    public void setBookingAdditionalInfo(FlightOfferSearch offer) {
        this.bookingAdditionalInfo = new Gson().toJson(offer);
    }
}