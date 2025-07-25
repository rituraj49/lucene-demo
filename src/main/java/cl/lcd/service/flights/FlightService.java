package cl.lcd.service.flights;

import cl.lcd.dto.pricing.FlightPricingConfirmRequest;
import cl.lcd.dto.pricing.FlightPricingConfirmResponse;
import cl.lcd.dto.search.FlightAvailabilityRequest;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOfferSearch;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FlightService {
    @Autowired
    private AmadeusFlightSearchService amadeusFlightSearchService;

    @Autowired
    AmadeusPricingService amadeusPricingService;

    public FlightOfferSearch[] flightSearch(Map<String, String> paramsMap) throws ResponseException {
        return amadeusFlightSearchService.flightOfferSearch(paramsMap);
    }

    public FlightOfferSearch[] flightMultiCitySearch
            (FlightAvailabilityRequest flightRequestDto) throws ResponseException, JsonProcessingException {
        return amadeusFlightSearchService.searchMultiCityFlightOffers(flightRequestDto);
    }

    public FlightPricingConfirmResponse confirmFlightPrice(FlightPricingConfirmRequest flightRequest) throws ResponseException {
        return amadeusPricingService.searchFlightOffersPrice(flightRequest);
    }
}
