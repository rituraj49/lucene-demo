package cl.lcd.service;

import cl.lcd.dto.booking.FlightBookingRequest;
import cl.lcd.dto.booking.FlightBookingResponse;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOrder;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService {
    @Autowired
    AmadeusBookingService amadeusBookingService;

    /**
     * Creates a booking with the given flight offer ID.
     *
     * @param flightOfferRequest The request dto.
     * @return A string containing the booking confirmation details.
     */
    public String createBooking(FlightBookingRequest flightOfferRequest) throws ResponseException {
        FlightBookingResponse order = amadeusBookingService.createFlightOrder(flightOfferRequest);
        return order.toString();
    }
}
