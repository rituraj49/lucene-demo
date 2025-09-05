package cl.lcd.service.booking;

import cl.lcd.dto.booking.FlightBookingRequest;
import cl.lcd.dto.booking.FlightBookingResponse;
import com.amadeus.Response;
import com.amadeus.exceptions.ResponseException;

public interface BookingServiceInterface {

    public FlightBookingResponse bookFlight(FlightBookingRequest flightOrderRequest) throws ResponseException ;

//    public FlightBookingResponse getFlightBooking(String orderId) throws ResponseException ;

//    public Response cancelFlightBooking(String orderId) throws ResponseException ;


}
