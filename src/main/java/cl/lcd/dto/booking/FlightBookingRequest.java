package cl.lcd.dto.booking;

import com.amadeus.shopping.FlightOffersSearch;
import lombok.Data;

import java.util.List;

@Data
public class FlightBookingRequest {
    private String flightOffer;

    private List<TravelerDto> travelers;
}
