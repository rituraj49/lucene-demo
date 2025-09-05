package cl.lcd.service.flights;

import cl.lcd.dto.pricing.FlightPricingConfirmRequest;
import cl.lcd.dto.pricing.FlightPricingConfirmResponse;
import cl.lcd.dto.search.FlightAvailabilityRequest;
import cl.lcd.dto.search.FlightAvailabilityResponse;
import com.amadeus.exceptions.ResponseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Profile("dev")
@Service
public class FlightService implements FlightSearchInterface {
    @Autowired
    private AmadeusFlightSearchService amadeusFlightSearchService;

    @Autowired
    AmadeusPricingService amadeusPricingService;

    public List<FlightAvailabilityResponse> flightSearch(Map<String, String> paramsMap) throws ResponseException {
        return amadeusFlightSearchService.flightOfferSearch(paramsMap);
    }

    public List<FlightAvailabilityResponse> flightMultiCitySearch
            (FlightAvailabilityRequest flightRequestDto) throws ResponseException, JsonProcessingException {
        return amadeusFlightSearchService.searchMultiCityFlightOffers(flightRequestDto);
    }

    public FlightPricingConfirmResponse confirmFlightPrice(FlightPricingConfirmRequest flightRequest) throws ResponseException {
        return amadeusPricingService.searchFlightOffersPrice(flightRequest);
    }
}
