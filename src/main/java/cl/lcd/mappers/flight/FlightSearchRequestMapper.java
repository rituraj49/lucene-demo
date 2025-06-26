package cl.lcd.mappers.flight;

import cl.lcd.dto.search.FlightOfferSearchDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlightSearchRequestMapper {
    public static Map<String, Object> mapDtoToFlightSearchRequest(FlightOfferSearchDto flightOfferSearchDto) {
        Map<String, Object> flightOfferMap = new HashMap<>();
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

        searchParams.put("maxFlightOffers", flightOfferSearchDto.getMaxCount());
        searchParams.put("addOneWayOffers", flightOfferSearchDto.isOneWay());
        searchParams.put("flightFilters", flightFilters);

        flightOfferMap.put("currencyCode", flightOfferSearchDto.getCurrencyCode());
        flightOfferMap.put("sources", List.of("GDS"));
        flightOfferMap.put("originDestinations", ordList);
        flightOfferMap.put("travelers", travelerList);
        flightOfferMap.put("searchCriteria", searchParams);

        for(FlightOfferSearchDto.OriginDestinationsDto ord : flightOfferSearchDto.getTripDetails()) {
            Map<String, Object> ordMap = new HashMap<>();
            ordMap.put("id", ord.getId());
            ordMap.put("originLocationCode", ord.getFrom());
            ordMap.put("destinationLocationCode", ord.getTo());
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
            travelerMap.put("id", trv.getId());
            travelerMap.put("travelerType", travelerType);
            if("HELD_INFANT".equals(travelerType)) {
                travelerMap.put("associateAdultId", trv.getAssociateAdultId());
            }
            travelerList.add(travelerMap);
        }
        return flightOfferMap;
    }
}
