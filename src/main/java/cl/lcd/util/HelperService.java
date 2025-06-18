package cl.lcd.util;

import cl.lcd.model.Airport;
import cl.lcd.model.AirportResponse;
import cl.lcd.model.LocationType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class HelperService {
    public List<AirportResponse> getGroupedData(List<Airport> data) {
        Map<String, List<Airport>> groupedData = data.stream().collect(Collectors.groupingBy(Airport::getCity_code));

        List<AirportResponse> result = new ArrayList<>();

        for(Map.Entry<String, List<Airport>> entry : groupedData.entrySet()) {
//            System.out.println("map entry: " + entry.toString());
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
}
