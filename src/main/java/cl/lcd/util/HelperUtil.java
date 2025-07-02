package cl.lcd.util;

import cl.lcd.model.Airport;
import cl.lcd.model.AirportResponse;
import cl.lcd.enums.LocationType;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
            parent.setParent(airportCity);
            parent.setGroupData(children);

            result.add(parent);
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
                        .withSeparator('^')
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();
                //            inMemoryLuceneService.indexData(airportsList);

        return csvToBean.parse();
//            } catch (Exception e) {
//                System.out.println("caught exception");
//                throw new RuntimeException(e);
////            e.printStackTrace();
////            return ResponseEntity.status(500).body("Something went wrong...");
//            }
//            return new ArrayList<>();
    }
}
