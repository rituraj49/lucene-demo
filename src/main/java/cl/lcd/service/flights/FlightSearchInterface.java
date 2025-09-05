package cl.lcd.service.flights;

import cl.lcd.dto.pricing.FlightPricingConfirmRequest;
import cl.lcd.dto.pricing.FlightPricingConfirmResponse;
import cl.lcd.dto.search.FlightAvailabilityRequest;
import cl.lcd.dto.search.FlightAvailabilityResponse;
import com.amadeus.exceptions.ResponseException;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
import java.util.Map;

public interface FlightSearchInterface {

    public List<FlightAvailabilityResponse> flightSearch(Map<String, String> paramsMap) throws ResponseException;


    public List<FlightAvailabilityResponse> flightMultiCitySearch
            (FlightAvailabilityRequest flightRequestDto) throws ResponseException, JsonProcessingException;


    public FlightPricingConfirmResponse confirmFlightPrice(FlightPricingConfirmRequest flightRequest) throws ResponseException ;
}
