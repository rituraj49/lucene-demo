package cl.lcd.service.flights;

import cl.lcd.dto.pricing.FlightPricingConfirmRequest;
import cl.lcd.dto.pricing.FlightPricingConfirmResponse;
import cl.lcd.dto.search.FlightAvailabilityRequest;
import cl.lcd.dto.search.FlightAvailabilityResponse;
import com.amadeus.exceptions.ResponseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;


@Service
@Profile("offline")
public class FlightServiceOffline implements FlightSearchInterface {
    private final ObjectMapper mapper = new ObjectMapper();

    private String readFile(String fileName) throws Exception {
        File file = new ClassPathResource(fileName).getFile();
        return Files.readString(file.toPath());
    }


    @Override
    public List<FlightAvailabilityResponse> flightSearch(Map<String, String> paramsMap) throws ResponseException
    {
        try {
            String json = readFile("flight_search_response.text");
      //      System.out.println(json);
            List<FlightAvailabilityResponse> result=mapper.readValue(json, new TypeReference<List<FlightAvailabilityResponse>>() {});
            System.out.println(result.toString());
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Error reading offline flight search", e);
        }
    }

    @Override
    public List<FlightAvailabilityResponse> flightMultiCitySearch(FlightAvailabilityRequest flightRequestDto)
            throws ResponseException, JsonProcessingException
    {
        try {
            String json = readFile("multicity_flight_search_response.text");
            List<FlightAvailabilityResponse> result=mapper.readValue(json, new TypeReference<List<FlightAvailabilityResponse>>() {});
            System.out.println(result.toString());
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Error reading offline multicity search", e);
        }
    }

    @Override
    public FlightPricingConfirmResponse confirmFlightPrice(FlightPricingConfirmRequest flightRequest)
            throws ResponseException
    {
        try {
            String json=readFile("flight_pricing_response.text");
            return mapper.readValue(json, new TypeReference<>() {});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
