package cl.lcd.service.flights;

import cl.lcd.dto.search.FlightAvailabilityRequest;
import cl.lcd.dto.search.FlightAvailabilityResponse;
import cl.lcd.mappers.flight.FlightSearchResponseMapper;
import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.Response;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOfferSearch;
import com.amadeus.resources.Resource;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;

import static cl.lcd.mappers.flight.FlightSearchRequestMapper.mapDtoToFlightSearchRequest;

@Service
@Slf4j
public class AmadeusFlightSearchService {

        @Autowired
        private Amadeus amadeusClient;

        @Autowired
        private ObjectMapper objectMapper;

    /**
     * searches for flight offers based on the provided parameters.
     * @param paramsMap = Map<String, String>
     * @return FlightOfferSearch[]
     * @throws ResponseException
     */
    public List<FlightAvailabilityResponse> flightOfferSearch(Map<String, String> paramsMap) throws ResponseException {
        Params params = null;

        for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
            if (params == null) {
                params = Params.with(entry.getKey(), entry.getValue());
            } else {
                params.and(entry.getKey(), entry.getValue());
            }
        }
        Response rawResponse = amadeusClient.get("/v2/shopping/flight-offers", params);
        JsonObject json = rawResponse.getResult().getAsJsonObject();
//        JsonObject root = json.getAsJsonObject();
        JsonObject dictionaries = json.getAsJsonObject("dictionaries");

        FlightOfferSearch[] flightOffers = (FlightOfferSearch[])
                Resource.fromArray(rawResponse, FlightOfferSearch[].class);
//        System.out.println(dictionaries.getAsJsonObject("carriers"));
//       var res = amadeusClient.shopping.flightOffersSearch.get(params);
//       var res = amadeusClient.shopping.flightOffersSearch.get(params);
//       log.info(Arrays.toString(res));
        List<FlightAvailabilityResponse> flightResponseList = Arrays.stream(flightOffers)
//                .map(FlightSearchResponseMapper::createResponse)
                .map(f -> FlightSearchResponseMapper.createResponse(f, dictionaries))
                .toList();

        return flightResponseList;
    }

    /**
     * Searches for multi-city flight offers based on the provided flight request.
     * @param flightOfferSearchRequestDto A map containing the flight request details.
     * @return An array of FlightOfferSearch objects representing the flight offers.
     * @throws ResponseException If an error occurs while searching for flight offers.
     * @throws JsonProcessingException If an error occurs while processing  the json body.
     */
//    @Cacheable(cacheNames = "flightOffers")
    public List<FlightAvailabilityResponse> searchMultiCityFlightOffers
            (FlightAvailabilityRequest flightOfferSearchRequestDto) throws ResponseException, JsonProcessingException {
        Map<String, Object> dtoMap = mapDtoToFlightSearchRequest(flightOfferSearchRequestDto);

        String body = objectMapper.writeValueAsString(dtoMap);
        log.info("Flight search request body sent to amadeus: {}", body);
        FlightOfferSearch[] offers = amadeusClient.shopping.flightOffersSearch.post(body);

        //List<FlightOfferSearch> offerList = List.of(offers);

//        if (offers != null) {
//            List<FlightOfferSearch> offerList = List.of(offers);
//            // Do something with offerList if needed
//        } else {
//            log.warn("No flight offers returned from Amadeus");
//        }

//        return offers;
        return Arrays.stream(offers)
                    .map(FlightSearchResponseMapper::createResponse)
                    .toList();
    }

// https://test.api.amadeus.com/v1/shopping/flight-offers/pricing
}
