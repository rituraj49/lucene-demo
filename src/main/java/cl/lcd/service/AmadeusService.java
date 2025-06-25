package cl.lcd.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cl.lcd.dto.TravelerDto;
import cl.lcd.util.HelperUtil;
import com.amadeus.resources.FlightOfferSearch;
import com.amadeus.resources.FlightOrder;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.Location;

import cl.lcd.model.Airport;
import cl.lcd.model.AirportResponse;
import cl.lcd.enums.LocationType;

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

	/**
	 * Creates a flight order using the provided json object of flight order request.
	 * @param JsonObject flightOrderRequest
	 * @return FlightOrder
	 * @throws ResponseException
	 */
	public FlightOrder createFlightOrder(JsonObject flightOrderRequest) throws ResponseException {
		log.info("Creating flight order with request: {}", flightOrderRequest);
		Gson gson = new Gson();
		JsonObject jsonData = flightOrderRequest.getAsJsonObject("data");

		JsonArray flightOffersJson = jsonData.getAsJsonArray("flightOffers");
		FlightOfferSearch[] flightOffers =  gson.fromJson(flightOffersJson, FlightOfferSearch[].class);

		JsonArray travelersJson = jsonData.getAsJsonArray("travelers");
		TravelerDto[] travelers = gson.fromJson(travelersJson, TravelerDto[].class);
		FlightOrder.Traveler[] flightTravelers = createTravelersFromDto(travelers);

        return amadeusClient.booking.flightOrders.post(flightOffers, flightTravelers);
	}

	/**
	 * Creates an array of FlightOrder.Traveler objects from an array of TravelerDto objects.
	 * @param TravelerDto[]
	 * @return FlightOrder.Traveler[]
	 */
	public FlightOrder.Traveler[] createTravelersFromDto(TravelerDto[] travelerDtos) {
		List<TravelerDto> travelerDtoList = List.of(travelerDtos);

		return travelerDtoList.stream().map(dto -> {
			FlightOrder.Traveler traveler = new FlightOrder.Traveler();
			traveler.setId(dto.getId());
			traveler.setDateOfBirth(dto.getDateOfBirth());

			traveler.setGender(dto.getGender().toString());
			FlightOrder.Name name = new FlightOrder.Name();
			name.setFirstName(dto.getName().getFirstName());
			name.setLastName(dto.getName().getLastName());
			traveler.setName(name);

			FlightOrder.Phone[] phones = dto.getContact().getPhones().stream().map(ph -> {
				FlightOrder.Phone phone = new FlightOrder.Phone();
				FlightOrder.Phone.DeviceType type = FlightOrder.Phone.DeviceType.valueOf(ph.getDeviceType().toString().toUpperCase());
				phone.setDeviceType(type);
				phone.setCountryCallingCode(ph.getCountryCallingCode());
				phone.setNumber(ph.getNumber());
				return phone;
			}).toArray(FlightOrder.Phone[]::new);

			FlightOrder.Contact contact = new FlightOrder.Contact();
			contact.setPhones(phones);
			traveler.setContact(contact);

			FlightOrder.Document[] docs = dto.getDocuments().stream().map(d -> {
				FlightOrder.Document doc = new FlightOrder.Document();
				FlightOrder.Document.DocumentType docType = FlightOrder.Document.DocumentType.valueOf(
						d.getDocumentType().toString().toUpperCase());
				doc.setDocumentType(docType);
				doc.setNumber(d.getNumber());
				doc.setExpiryDate(d.getExpiryDate());
				doc.setIssuanceCountry(d.getIssuanceCountry());
				doc.setNationality(d.getNationality());
				doc.setHolder(d.isHolder());

				return doc;
			}).toArray(FlightOrder.Document[]::new);


			return traveler;
		}).toArray(FlightOrder.Traveler[]::new);
	}
}
