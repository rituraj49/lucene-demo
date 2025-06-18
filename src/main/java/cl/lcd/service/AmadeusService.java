package cl.lcd.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.Location;

import cl.lcd.model.Airport;
import cl.lcd.model.AirportResponse;
import cl.lcd.model.LocationType;

@Service
public class AmadeusService {

	public AmadeusService() {}

	@Autowired
	private Amadeus amadeusClient;

	public AmadeusService(Amadeus amadeusClient) {
        this.amadeusClient = amadeusClient;
    }

    public List<Airport> searchLocations(Map<String, String> queryParams) throws ResponseException {

    	Params params = Params.with(
                queryParams.entrySet().iterator().next().getKey(),
                queryParams.entrySet().iterator().next().getValue()
        );

        queryParams.entrySet()
                .stream()
                .skip(1)
                .forEach(q -> params.and(q.getKey(), q.getValue()));

        Location[] locations = amadeusClient.referenceData.locations.get(params);
        
        List<Location> locList = new ArrayList<>(Arrays.asList(locations));

        List<Airport> airports = locList.stream().map(l -> {
        	LocationType type = LocationType.valueOf(l.getSubType());
        	return new Airport(
        		type,
    			l.getIataCode(),
    			l.getName(),
    			l.getGeoCode().getLatitude(),
    			l.getGeoCode().getLongitude(),
    			l.getTimeZoneOffset(),
    			l.getAddress().getCityCode(),
    			l.getAddress().getCountryCode(),
    			l.getAddress().getCityName()
        			);
        }).toList();

        return airports;
    }
    
    public List<AirportResponse> getGroupedData(List<Airport> data) {
		Map<String, List<Airport>> groupedData = data.stream().collect(Collectors.groupingBy(Airport::getCity_code));
		
		List<AirportResponse> result = new ArrayList<>();
		
		for(Map.Entry<String, List<Airport>> entry : groupedData.entrySet()) {
			System.out.println("map entry: " + entry.toString());
			List<Airport> group = entry.getValue();
			
			Optional<Airport> match = group.stream().filter(p -> LocationType.CITY.equals(p.getSubType())).findFirst();
			
//			Airport airport = group.get(0);
			Airport airportCity = match.orElse(null);
			System.out.println(airportCity.toString());
//			if(match.isPresent()) {
//				airportCity = match.get();
//			}
			
//			List<Airport> children = group.subList(1, group.size());
			List<Airport> children = group.
					stream()
					.filter(p -> 
//						!p.getIata().equals(airportCity.getIata()))
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
