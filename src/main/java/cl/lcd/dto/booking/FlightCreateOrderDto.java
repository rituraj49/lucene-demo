package cl.lcd.dto.booking;

import com.amadeus.shopping.FlightOffersSearch;

import java.util.List;

public class FlightCreateOrderDto {
    private FlightOffersSearch flightOffer;

    private List<TravelerDto> travelers;
}
