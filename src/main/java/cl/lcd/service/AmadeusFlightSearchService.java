package cl.lcd.service;

import cl.lcd.dto.search.FlightOfferSearchDto;
import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOfferSearch;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
    public FlightOfferSearch[] flightOfferSearches(Map<String, String> paramsMap) throws ResponseException {
        Params params = null;

        for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
            if (params == null) {
                params = Params.with(entry.getKey(), entry.getValue());
            } else {
                params.and(entry.getKey(), entry.getValue());
            }
        }
        return amadeusClient.shopping.flightOffersSearch.get(params);
    }

    /**
     * Searches for multi-city flight offers based on the provided flight request.
     * @param flightOfferSearchDto A map containing the flight request details.
     * @return An array of FlightOfferSearch objects representing the flight offers.
     * @throws ResponseException If an error occurs while searching for flight offers.
     * @throws JsonProcessingException If an error occurs while processing  the json body.
     */
    public FlightOfferSearch[] searchMultiCityFlightOffers
            (FlightOfferSearchDto flightOfferSearchDto) throws ResponseException, JsonProcessingException {
//            (Map<String, Object> flightOfferSearchDto) throws ResponseException, JsonProcessingException {
        Map<String, Object> dtoMap = mapDtoToFlightSearchRequest(flightOfferSearchDto);

        String body = objectMapper.writeValueAsString(dtoMap);
        log.info("Flight search request body sent to amadeus: {}", body);
        FlightOfferSearch[] offers = amadeusClient.shopping.flightOffersSearch.post(body);

        List<FlightOfferSearch> offerList = List.of(offers);

//        offerList.forEach(o -> System.out.println("src: " + o.toString()));
//        System.out.println("Flight offers: " + offerList.toString());
//        return offerList;
//        String jsonOutput = objectMapper.writeValueAsString(offers);
//
//        JsonNode jsonObject = objectMapper.readTree(jsonOutput);
        return offers;
    }

// https://test.api.amadeus.com/v1/shopping/flight-offers/pricing

    public Map<String, Object> mapDtoToFlightSearchRequest(FlightOfferSearchDto flightOfferSearchDto) {
        Map<String, Object> f = new HashMap<>();
        List<Map<String, Object>> ordList = new ArrayList<>();
        List<Map<String, String>> travelerList = new ArrayList<>();
        Map<String, Object> searchParams = new HashMap<>();
        Map<String, Object> flightFilters = new HashMap<>();

        Map<String, Object> cabinRestrictions = new HashMap<>();
        cabinRestrictions.put("cabin", flightOfferSearchDto.getCabin() != null ? flightOfferSearchDto.getCabin().toString() : "ECONOMY");
        cabinRestrictions.put("originDestinationIds", flightOfferSearchDto.getTripDetails().stream()
                .map(FlightOfferSearchDto.OriginDestinationsDto::getId).toList());

        flightFilters.put("cabinRestrictions", List.of(cabinRestrictions));
        flightFilters.put("returnToDepartureAirport", !flightOfferSearchDto.isOneWay());

//        cabinRestrictions.put("cabinClass", flightOfferSearchDto.getCabinClass() != null ? flightOfferSearchDto.getCabinClass().toString() : "ECONOMY");

        searchParams.put("maxFlightOffers", flightOfferSearchDto.getMaxCount());
        searchParams.put("addOneWayOffers", flightOfferSearchDto.isOneWay());
        searchParams.put("flightFilters", flightFilters);

        f.put("currencyCode", flightOfferSearchDto.getCurrencyCode());
        f.put("sources", List.of("GDS"));
        f.put("originDestinations", ordList);
        f.put("travelers", travelerList);
        f.put("searchCriteria", searchParams);

        for(FlightOfferSearchDto.OriginDestinationsDto ord : flightOfferSearchDto.getTripDetails()) {
            Map<String, Object> ordMap = new HashMap<>();
            ordMap.put("id", ord.getId());
            ordMap.put("originLocationCode", ord.getFrom());
            ordMap.put("destinationLocationCode", ord.getTo());
//            ordMap.put("departureDateTime", ord.getDepartureDateTime());
            ordMap.put("departureDateTimeRange", Map.of(
               "date", ord.getDepartureDate(),
                "time",  ord.getDepartureTime()
            ));
//            ordMap.put("arrivalDateTimeRange", Map.of(
//               "date", ord.getDepartureDate(),
//                "time",  ord.getDepartureTime()
//            ));
            ordList.add(ordMap);
        }

        for(FlightOfferSearchDto.TravelerInfoDto trv: flightOfferSearchDto.getTravelers()) {
            Map<String, String> travelerMap = new HashMap<>();
            String travelerType = trv.getTravelerType() != null ? trv.getTravelerType().toString() : "ADULT";
//            FlightOfferSearchDto.TravelerType.valueOf(String.valueOf(trv.getTravelerType());
            travelerMap.put("id", trv.getId());
            travelerMap.put("travelerType", travelerType);
            if("HELD_INFANT".equals(travelerType)) {
                travelerMap.put("associateAdultId", trv.getAssociateAdultId());
            }
            travelerList.add(travelerMap);
        }


        return f;
    }
}
