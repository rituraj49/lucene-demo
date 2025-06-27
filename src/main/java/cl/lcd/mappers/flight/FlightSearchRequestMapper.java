package cl.lcd.mappers.flight;

import cl.lcd.dto.search.FlightAvailabilityRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class FlightSearchRequestMapper {
    public static Map<String, Object> mapDtoToFlightSearchRequest(FlightAvailabilityRequest flightOfferSearchRequestDto) {
        Map<String, Object> flightOfferMap = new HashMap<>();
        List<Map<String, Object>> ordList = new ArrayList<>();
        List<Map<String, String>> travelerList = new ArrayList<>();
        Map<String, Object> searchParams = new HashMap<>();
        Map<String, Object> flightFilters = new HashMap<>();

        Map<String, Object> cabinRestrictions = new HashMap<>();
        cabinRestrictions.put("cabin", flightOfferSearchRequestDto.getCabin() != null ? flightOfferSearchRequestDto.getCabin().toString() : "ECONOMY");
        cabinRestrictions.put("originDestinationIds", flightOfferSearchRequestDto.getTripDetails().stream()
                .map(FlightAvailabilityRequest.TripDetailsDto::getId).toList());

        flightFilters.put("cabinRestrictions", List.of(cabinRestrictions));
//        flightFilters.put("returnToDepartureAirport", !flightOfferSearchRequestDto.isOneWay());

        searchParams.put("maxFlightOffers", flightOfferSearchRequestDto.getMaxCount());
//        searchParams.put("addOneWayOffers", flightOfferSearchRequestDto.isOneWay());/0
        searchParams.put("flightFilters", flightFilters);

        flightOfferMap.put("currencyCode", flightOfferSearchRequestDto.getCurrencyCode());
        flightOfferMap.put("sources", List.of("GDS"));
        flightOfferMap.put("originDestinations", ordList);
        flightOfferMap.put("travelers", travelerList);
        flightOfferMap.put("searchCriteria", searchParams);

        for(FlightAvailabilityRequest.TripDetailsDto ord : flightOfferSearchRequestDto.getTripDetails()) {
            Map<String, Object> ordMap = new HashMap<>();
            ordMap.put("id", ord.getId());
            ordMap.put("originLocationCode", ord.getFrom());
            ordMap.put("destinationLocationCode", ord.getTo());
            ordMap.put("departureDateTimeRange", Map.of(
                    "date", ord.getDepartureDate(),
                    "time",  ord.getDepartureTime()
            ));
            ordList.add(ordMap);
        }

        int adults = flightOfferSearchRequestDto.getAdults();
        IntStream.rangeClosed(1, adults).forEach(i -> {
            Map<String, String> travelerMap = new HashMap<>();
            travelerMap.put("id", String.valueOf(i));
            travelerMap.put("travelerType", "ADULT");
            travelerList.add(travelerMap);
        });

        if(flightOfferSearchRequestDto.getChildren() > 0) {
            IntStream.rangeClosed(travelerList.size() + 1, travelerList.size() + flightOfferSearchRequestDto.getChildren()).forEach(i -> {
                Map<String, String> travelerMap = new HashMap<>();
                travelerMap.put("id", String.valueOf(i));
                travelerMap.put("travelerType", "CHILD");
                travelerList.add(travelerMap);
            });
        }

        if(flightOfferSearchRequestDto.getInfants() > 0) {
            IntStream.rangeClosed(travelerList.size() + 1,
                    travelerList.size() + flightOfferSearchRequestDto.getInfants()).forEach(i -> {
                Map<String, String> travelerMap = new HashMap<>();
                travelerMap.put("id", String.valueOf(i));
                travelerMap.put("travelerType", "HELD_INFANT");
                travelerMap.put("associateAdultId", String.valueOf(travelerList.stream()
                        .filter(tr -> "ADULT".equals(tr.get("travelerType")))
                        .findFirst()
                        .orElseThrow()
                        .get("id")));
                travelerList.add(travelerMap);
            });
        }

        return flightOfferMap;
    }
}
