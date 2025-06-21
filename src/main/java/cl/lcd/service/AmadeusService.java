package cl.lcd.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import cl.lcd.util.HelperUtil;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class AmadeusService {

	public AmadeusService() {}

	@Autowired
	private Amadeus amadeusClient;

//	public AmadeusService(Amadeus amadeusClient) {
//        this.amadeusClient = amadeusClient;
//    }

	/**
	 *
	 * @param queryParams = Map<String, String>
	 * @return List<AirportResponse>
	 * @throws ResponseException
	 * queryParams should contain at least two key-value pairs. Examlpe - [{subType: CITY,AIRPORT} {keyword: delhi}]
	 */

    public List<AirportResponse> searchLocations(Map<String, String> queryParams) throws ResponseException {
//		boolean first = true;
		Params qParams = null;
//		if(first) {
//		for(Map.Entry<String, String> entry: queryParams.entrySet()) {
//			if(qParams == null) {
//				qParams = Params.with(
//						"subType",
//						queryParams.get("subType")
//				);
//			}
//		}
		qParams = Params.with("subType", queryParams.get("subType"));
//			first = false;
//		}
		for(Map.Entry<String, String> entry: queryParams.entrySet()) {
			if(!entry.getKey().equals("subType")) {
				qParams.and(entry.getKey(), entry.getValue());
			}
		}
//			queryParams.entrySet().stream()
//					.filter(f -> !f.getKey().equals("subType"))
//					.forEach(p -> qParams.and(p.getKey(), p.getValue()));

//    	Params params = Params.with(
////				queryParams.get("subType")
//                queryParams.entrySet().iterator().next().getKey(),
//                queryParams.entrySet().iterator().next().getValue()
//        );

//        queryParams.entrySet()
//                .stream()
//                .skip(1)
//                .forEach(q -> params.and(q.getKey(), q.getValue()));
//		System.out.println("query params: " + qParams.toString());
        Location[] locations = amadeusClient.referenceData.locations.get(qParams);
        
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

        return HelperUtil.getGroupedData(airports);
    }
}
