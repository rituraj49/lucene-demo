package cl.lcd.service;

import com.amadeus.Amadeus;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOfferSearch;
import com.amadeus.resources.FlightPrice;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AmadeusPricingService {
    @Autowired
    private Amadeus amadeusClient;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Searches for flight offers and returns the price for the given flight request.
     *
     * @param flightRequest An array of FlightOfferSearch objects representing the flight request.
     * @return A FlightPrice object containing the price details of the flight offers.
     * @throws ResponseException If an error occurs while searching for flight offers or pricing.
     */
    public FlightPrice searchFlightOffersPrice(FlightOfferSearch[] flightRequest) throws ResponseException {
        return amadeusClient.shopping.flightOffersSearch.pricing.post(flightRequest);
    }
}
