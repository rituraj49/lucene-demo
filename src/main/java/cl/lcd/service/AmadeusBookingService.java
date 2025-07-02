package cl.lcd.service;

import cl.lcd.dto.booking.FlightBookingRequest;
import cl.lcd.dto.booking.FlightBookingResponse;
import cl.lcd.mappers.booking.FlightBookingResponseMapper;
import com.amadeus.Amadeus;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOfferSearch;
import com.amadeus.resources.FlightOrder;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AmadeusBookingService {

    @Autowired
    Amadeus amadeusClient;

    /**
     * Creates a flight order using the provided json object of flight order request.
     * @param flightOrderRequest = JsonObject
     * @return FlightOrder
     * @throws ResponseException
     */
    public FlightBookingResponse createFlightOrder(FlightBookingRequest flightOrderRequest) throws ResponseException {
        log.info("Creating flight order with request: {}", flightOrderRequest);
        Gson gson = new Gson();
        FlightOfferSearch offer = gson.fromJson(flightOrderRequest.getFlightOffer(), FlightOfferSearch.class);

        FlightOrder.Traveler[] flightTravelers = FlightBookingResponseMapper.createTravelersFromDto(flightOrderRequest.getTravelers());

        FlightOrder order = amadeusClient.booking.flightOrders.post(offer, flightTravelers);
        return FlightBookingResponseMapper.flightBookingResponse(order);
    }

    public FlightOrder getFlightOrder(String orderId) throws ResponseException {
        log.info("Retrieving flight order with ID: {}", orderId);
        FlightOrder order = amadeusClient.booking.flightOrder(orderId).get();
        return order;
    }
}
