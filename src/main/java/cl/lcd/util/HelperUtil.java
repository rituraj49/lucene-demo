package cl.lcd.util;

import cl.lcd.model.Airport;
import cl.lcd.model.AirportResponse;
import cl.lcd.enums.LocationType;
import cl.lcd.model.LocationResponse;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class HelperUtil {
    public static List<AirportResponse> getGroupedData(List<Airport> data) {
        Map<String, List<Airport>> groupedData = data.stream().collect(Collectors.groupingBy(Airport::getCityCode));

        List<AirportResponse> result = new ArrayList<>();

        for(Map.Entry<String, List<Airport>> entry : groupedData.entrySet()) {
            List<Airport> group = entry.getValue();

            Optional<Airport> match = group.stream().filter(p -> LocationType.CITY.equals(p.getSubType())).findFirst();

            Airport airportCity = match.orElse(null);

            List<Airport> children = group.
                    stream()
                    .filter(p ->
                            !p.getSubType().equals(LocationType.CITY))
                    .toList();

            AirportResponse parent = new AirportResponse();
            if (airportCity == null) {
                airportCity = group.get(0);
            }

            parent.setParent(airportCity);
            parent.setGroupData(children);

            result.add(parent);
        }
        return result;
    }

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

            List<Airport> children = group.
                    stream()
//                    .filter(p ->
//                            !p.getSubType().equals(LocationType.CITY)
//                    )
                    .skip(1)
                    .toList();

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
//            parent.setParent(airportCity);
//            parent.setGroupData(children);

            result.add(locationResponse);
        }
        return result;
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

//    public static <T> List<T> convertCsv(MultipartFile file, Class<T> tClass) throws IOException { // generic method
    public static <T> List<T> convertCsv(Reader reader, Class<T> tClass) throws IOException { // generic method

//            try(Reader reader = new InputStreamReader(file.getInputStream())) {
                CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
                        .withType(tClass)
//                        .withSeparator('^')
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();
                //            inMemoryLuceneService.indexData(airportsList);

        List<T> parsedData = csvToBean.parse();
        return parsedData;
//            } catch (Exception e) {
//                System.out.println("caught exception");
//                throw new RuntimeException(e);
////            e.printStackTrace();
////            return ResponseEntity.status(500).body("Something went wrong...");
//            }
//            return new ArrayList<>();
    }
}
