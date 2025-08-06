package cl.lcd.util;

import cl.lcd.model.*;
import cl.lcd.enums.LocationType;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class HelperUtil {
//    public static List<AirportResponse> getGroupedData(List<Airport> data) {
//        Map<String, List<Airport>> groupedData = data.stream().collect(Collectors.groupingBy(Airport::getCityCode));
//
//        List<AirportResponse> result = new ArrayList<>();
//
//        for(Map.Entry<String, List<Airport>> entry : groupedData.entrySet()) {
//            List<Airport> group = entry.getValue();
//
//            Optional<Airport> match = group.stream().filter(p -> LocationType.CITY.equals(p.getSubType())).findFirst();
//
//            Airport airportCity = match.orElse(null);
//
//            List<Airport> children = group.
//                    stream()
//                    .filter(p ->
//                            !p.getSubType().equals(LocationType.CITY))
//                    .toList();
//
//            AirportResponse parent = new AirportResponse();
//            if (airportCity == null) {
//                airportCity = group.get(0);
//            }
//
//            parent.setParent(airportCity);
//            parent.setGroupData(children);
//
//            result.add(parent);
//        }
//        return result;
//    }

    public static List<LocationResponse> getGroupedLocationData(List<Airport> data) {
        Map<String, List<Airport>> groupedData = data.stream().collect(Collectors.groupingBy(Airport::getCityCode));

        List<LocationResponse> result = new ArrayList<>();

        for(Map.Entry<String, List<Airport>> entry : groupedData.entrySet()) {
            List<Airport> group = entry.getValue();

            Optional<Airport> match = group.stream().filter(p -> LocationType.CITY.equals(p.getSubType())).findFirst();

            Airport airportCity = match.orElse(null);
//            if(airportCity != null) airportCity.setName("All airports within " + airportCity.getName());

            if (airportCity == null) {
                airportCity = group.get(0);
            } else {
                airportCity.setName("All airports within " + airportCity.getName());
            }

            List<LocationResponse.SimpleAirport> subAirports = group.
                    stream()
//                    .filter(p ->
//                            !p.getSubType().equals(LocationType.CITY)
//                    )
                    .skip(1)
                    .map(c -> {
                        LocationResponse.SimpleAirport simpleAirport = new LocationResponse.SimpleAirport();
                        simpleAirport.setSubType(c.getSubType());
                        simpleAirport.setIata(c.getIata());
                        simpleAirport.setName(c.getName());
                        simpleAirport.setCityCode(c.getCityCode());
                        simpleAirport.setCity(c.getCity());
                        return simpleAirport;
                    })
                    .toList();

            SimpleAirportWrapper children = new SimpleAirportWrapper();
            children.setSimpleAirports(subAirports);

            LocationResponse locationResponse = getLocationResponse(airportCity, children);
//            parent.setParent(airportCity);
//            parent.setGroupData(children);

            result.add(locationResponse);
        }
        return result;
    }

    private static LocationResponse getLocationResponse(Airport airportCity, SimpleAirportWrapper children) {
        LocationResponse locationResponse = new LocationResponse();
        locationResponse.setSubType(airportCity.getSubType());
        locationResponse.setIata(airportCity.getIata());
        locationResponse.setName(airportCity.getName());
        locationResponse.setLatitude(airportCity.getLatitude());
        locationResponse.setLongitude(airportCity.getLongitude());
        locationResponse.setTimeZoneOffset(airportCity.getTimeZoneOffset());
        locationResponse.setCityCode(airportCity.getCityCode());
        locationResponse.setCountryCode(airportCity.getCountryCode());
        locationResponse.setCity(airportCity.getCity());
        locationResponse.setGroupData(children);
        return locationResponse;
    }

    public static List<AirportResponse> getGroupedDataLucene(List<Airport> data) {
        Map<String, List<Airport>> groupedData = data.stream().collect(Collectors.groupingBy(Airport::getCityCode));

        List<AirportResponse> result = new ArrayList<>();

        for(Map.Entry<String, List<Airport>> entry : groupedData.entrySet()) {
            List<Airport> group = entry.getValue();

            Airport airportCity = group.get(0);

            List<Airport> children = group.stream().filter(p -> !p.getCity().isEmpty()).toList();

            AirportResponse parent = new AirportResponse();
            parent.setParent(airportCity);
            parent.setGroupData(children);

            result.add(parent);
        }
        return result;
    }

    public static <T> List<T> convertCsv(Reader reader, Class<T> tClass) throws IOException {
                CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
                        .withType(tClass)
//                        .withSeparator('^')
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();
        return csvToBean.parse();
    }

    public static List<LocationResponse> getGroupedCityData(List<Airport> airports, List<CityGroup> cityGroupList) {
        Map<String, List<Airport>> airportsByCity = airports.stream()
                .collect(Collectors.groupingBy(Airport::getCityCode));

        Map<String, CityGroup> cityGroupMap = cityGroupList.stream()
                .collect(Collectors.toMap(CityGroup::getCityCode, cg -> cg));

        return airportsByCity.entrySet().stream()
                .map(entry -> {
                    String cityCode = entry.getKey();
                    Airport airportRep = entry.getValue().get(0);

                    LocationResponse response = new LocationResponse();
                        response.setIata(airportRep.getIata());
                        response.setCity(airportRep.getCity());
                        response.setCityCode(airportRep.getCityCode());
                        response.setLatitude(airportRep.getLatitude());
                        response.setLongitude(airportRep.getLongitude());
                        response.setSubType(airportRep.getSubType());
                        response.setName(airportRep.getName());
                        response.setCountryCode(airportRep.getCountryCode());
                        response.setCountryCode(airportRep.getCountryCode());
                        response.setTimeZoneOffset(airportRep.getTimeZoneOffset());

                    List<LocationResponse.SimpleAirport> subAirports = Optional.ofNullable(cityGroupMap.get(cityCode))
                            .map(cg -> cg.getAirportGroup().stream()
                                    .filter(sa -> !sa.getIata().equals(airportRep.getIata()))
                                    .sorted(Comparator.comparing((LocationResponse.SimpleAirport sa) ->
                                            "CITY".equalsIgnoreCase(String.valueOf(sa.getSubType())) ? 0 : 1))
                                    .toList())
                            .orElse(List.of());
//                    List<LocationResponse.SimpleAirport> finalSubAirports = new ArrayList<>(subAirports);
                    SimpleAirportWrapper finalSubAirports = new SimpleAirportWrapper();
                    finalSubAirports.setSimpleAirports(subAirports);
                    response.setGroupData(finalSubAirports);

                    return response;
                }).toList();
    }

    public static String getEmailBody(TemplateEngine templateEngine, String templateName, Context context) {
        return templateEngine.process(templateName, context);
    }

}
