package cl.lcd.service;

import cl.lcd.dto.booking.TravelerDto;
import com.amadeus.Amadeus;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOfferSearch;
import com.amadeus.resources.FlightOrder;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AmadeusBookingService {

    @Autowired
    Amadeus amadeusClient;

    /**
     * Creates a flight order using the provided json object of flight order request.
     * @param flightOrderRequest = JsonObject
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
     * @param travelerDtos = TravelerDto[]
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
