package cl.lcd.service.booking;

import cl.lcd.dto.booking.FlightBookingRequest;
import cl.lcd.dto.booking.FlightBookingResponse;
import com.amadeus.Response;
import com.amadeus.exceptions.ResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("dev")
@Service
public class    BookingService implements BookingServiceInterface {

    @Autowired
    AmadeusBookingService amadeusBookingService;

    /**
     * Creates a booking with the given flight offer ID.
     *
     * @param flightOrderRequest The request dto.
     * @return A string containing the booking confirmation details.
     */
    public FlightBookingResponse bookFlight(FlightBookingRequest flightOrderRequest) throws ResponseException {
        return amadeusBookingService.createFlightOrder(flightOrderRequest);
    }

/*
    public FlightBookingResponse getFlightBooking(String orderId) throws ResponseException {
        return amadeusBookingService.getFlightOrder(orderId);
    }

    public Response cancelFlightBooking(String orderId) throws ResponseException {
        return amadeusBookingService.cancelFlightOrder(orderId);
    }
*/

}
