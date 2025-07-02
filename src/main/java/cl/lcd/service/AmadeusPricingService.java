package cl.lcd.service;

import cl.lcd.dto.pricing.FlightPricingConfirmRequest;
import cl.lcd.dto.pricing.FlightPricingConfirmResponse;
import cl.lcd.mappers.flight.FlightSearchResponseMapper;
import com.amadeus.Amadeus;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOfferSearch;
import com.amadeus.resources.FlightPrice;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@Slf4j
public class AmadeusPricingService {
    @Autowired
    private Amadeus amadeusClient;

    @Autowired
    private ObjectMapper objectMapper;

    private final Gson gson = new Gson();

    /**
     * Searches for flight offers and returns the price for the given flight request.
     *
     * @param flightRequest An array of FlightOfferSearch objects representing the flight request.
     * @return A FlightPrice object containing the price details of the flight offers.
     * @throws ResponseException If an error occurs while searching for flight offers or pricing.
     */
    public FlightPricingConfirmResponse searchFlightOffersPrice(FlightPricingConfirmRequest flightRequest) throws ResponseException {
        FlightOfferSearch offer = gson.fromJson(flightRequest.getFlightOffer(), FlightOfferSearch.class);

        FlightPrice price = amadeusClient.shopping.flightOffersSearch.pricing.post(offer);

        FlightPricingConfirmResponse response = new FlightPricingConfirmResponse();

        FlightOfferSearch flightOfferObject = price.getFlightOffers()[0];

        response.setFlightOffer(FlightSearchResponseMapper.createResponse(price.getFlightOffers()[0]));
        response.getFlightOffer().setPricingAdditionalInfo(null);
        response.setBookingAdditionalInfo(flightOfferObject);
        return response;
    }
}
