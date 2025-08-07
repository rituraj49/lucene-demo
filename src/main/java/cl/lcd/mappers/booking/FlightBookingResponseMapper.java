package cl.lcd.mappers.booking;

import cl.lcd.dto.booking.FlightBookingResponse;
import cl.lcd.dto.booking.TravelerRequestDto;
import cl.lcd.dto.booking.TravelerResponseDto;
import cl.lcd.dto.search.FlightAvailabilityResponse;
import cl.lcd.mappers.flight.FlightSearchResponseMapper;
import com.amadeus.resources.FlightOrder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class FlightBookingResponseMapper {

    /**
     * Map FlightOrder from  amadeus to FLightBookingResponse custom dto
     * @param order = FlightOrder
     * @return FlightBookingResponse
     */
    public static FlightBookingResponse flightBookingResponse(FlightOrder order, List<TravelerRequestDto> travelerRequestDtos) {
        FlightBookingResponse response = new FlightBookingResponse();
//        FlightAvailabilityResponse flightAvailabilityResponse = new FlightAvailabilityResponse();
        FlightAvailabilityResponse flightAvailabilityResponse = FlightSearchResponseMapper.createResponse(order.getFlightOffers()[0]);
        List<TravelerResponseDto> travelers = createTravelerResponse(order.getTravelers(), travelerRequestDtos);
        flightAvailabilityResponse.setPricingAdditionalInfo(null);
        //        flightAvailabilityResponse.setOneWay(order.getFlightOffers()[0].isOneWay());
        response.setOrderId(order.getId());
        response.setFlightOffer(flightAvailabilityResponse);
        response.setTravelers(travelers);
        return response;
    }

    public static FlightBookingResponse flightBookingResponse(FlightOrder order) {
        FlightBookingResponse response = new FlightBookingResponse();
//        FlightAvailabilityResponse flightAvailabilityResponse = new FlightAvailabilityResponse();
        FlightAvailabilityResponse flightAvailabilityResponse = FlightSearchResponseMapper.createResponse(order.getFlightOffers()[0]);
        List<TravelerResponseDto> travelers = createTravelerResponse(order.getTravelers());
        flightAvailabilityResponse.setPricingAdditionalInfo(null);
        //        flightAvailabilityResponse.setOneWay(order.getFlightOffers()[0].isOneWay());
        response.setOrderId(order.getId());
        response.setFlightOffer(flightAvailabilityResponse);
        response.setTravelers(travelers);
        return response;
    }

    /**
     * Creates a list of TravelerResponseDto objects from an array of FlightOrder.Traveler[] objects.
     * @param travelers = FlightOrder.Traveler[] travelers
     * @return List<TravelerResponseDto>
     */
    public static List<TravelerResponseDto> createTravelerResponse(FlightOrder.Traveler[] travelers, List<TravelerRequestDto> travelerRequestDtos) {
        List<TravelerResponseDto> travelersList = new ArrayList<>();
//        for(FlightOrder.Traveler traveler: travelers) {
        for(int i = 0; i < travelers.length; i++) {
            TravelerResponseDto dto = new TravelerResponseDto();
            FlightOrder.Traveler traveler = travelers[i];
            List<TravelerResponseDto.Phone> phones = Arrays.stream(traveler.getContact()
                    .getPhones())
                    .map(ph -> {
                        TravelerResponseDto.Phone phone = new TravelerResponseDto.Phone();
                        phone.setNumber(ph.getNumber());
                        phone.setDeviceType(TravelerResponseDto.DeviceType.valueOf(ph.getDeviceType().toString()));
                        phone.setCountryCallingCode(ph.getCountryCallingCode());
                        return phone;
                    }).toList();

            List<TravelerResponseDto.IdentityDocumentResponse> docs = Arrays.stream(traveler.getDocuments())
                .map(doc -> {
                    TravelerResponseDto.IdentityDocumentResponse idoc = new TravelerResponseDto.IdentityDocumentResponse();
                    idoc.setDocumentType(TravelerResponseDto.DocumentType.valueOf(doc.getDocumentType().toString()));
                    idoc.setNumber(doc.getNumber());
                    idoc.setExpiryDate(doc.getExpiryDate());
                    idoc.setIssuanceCountry(doc.getIssuanceCountry());
                    idoc.setNationality(doc.getNationality());
                    idoc.setHolder(doc.isHolder());

                    return idoc;
            }).toList();
            dto.setId(traveler.getId());
            dto.setGender(TravelerResponseDto.Gender.valueOf(traveler.getGender()));
            dto.setFirstName(traveler.getName().getFirstName());
            dto.setLastName(traveler.getName().getLastName());
            dto.setPhones(phones);
            dto.setEmail(travelerRequestDtos.get(i).getEmail());
            dto.setDateOfBirth(traveler.getDateOfBirth());
            dto.setDocuments(docs);

            travelersList.add(dto);
        }
        return travelersList;
    }

    public static List<TravelerResponseDto> createTravelerResponse(FlightOrder.Traveler[] travelers) {
        List<TravelerResponseDto> travelersList = new ArrayList<>();
//        for(FlightOrder.Traveler traveler: travelers) {
        for(int i = 0; i < travelers.length; i++) {
            TravelerResponseDto dto = new TravelerResponseDto();
            FlightOrder.Traveler traveler = travelers[i];
            List<TravelerResponseDto.Phone> phones = Arrays.stream(traveler.getContact()
                    .getPhones())
                    .map(ph -> {
                        TravelerResponseDto.Phone phone = new TravelerResponseDto.Phone();
                        phone.setNumber(ph.getNumber());
                        phone.setDeviceType(TravelerResponseDto.DeviceType.valueOf(ph.getDeviceType().toString()));
                        phone.setCountryCallingCode(ph.getCountryCallingCode());
                        return phone;
                    }).toList();

            List<TravelerResponseDto.IdentityDocumentResponse> docs = Arrays.stream(traveler.getDocuments())
                .map(doc -> {
                    TravelerResponseDto.IdentityDocumentResponse idoc = new TravelerResponseDto.IdentityDocumentResponse();
                    idoc.setDocumentType(TravelerResponseDto.DocumentType.valueOf(doc.getDocumentType().toString()));
                    idoc.setNumber(doc.getNumber());
                    idoc.setExpiryDate(doc.getExpiryDate());
                    idoc.setIssuanceCountry(doc.getIssuanceCountry());
                    idoc.setNationality(doc.getNationality());
                    idoc.setHolder(doc.isHolder());

                    return idoc;
            }).toList();
            dto.setId(traveler.getId());
            dto.setGender(TravelerResponseDto.Gender.valueOf(traveler.getGender()));
            dto.setFirstName(traveler.getName().getFirstName());
            dto.setLastName(traveler.getName().getLastName());
            dto.setPhones(phones);
//            dto.setEmail(travelerRequestDtos.get(i).getEmail());
            dto.setDateOfBirth(traveler.getDateOfBirth());
            dto.setDocuments(docs);

            travelersList.add(dto);
        }
        return travelersList;
    }

    /**
     * Creates an array of FlightOrder.Traveler objects from an array of TravelerRequestDto objects.
     * @param travelerRequestDtoList = List<TravelerRequestDto>
     * @return FlightOrder.Traveler[]
     */
    public static FlightOrder.Traveler[] createTravelersFromDto(List<TravelerRequestDto> travelerRequestDtoList) {
//        List<TravelerRequestDto> travelerRequestDtoList = List.of(travelerDtos);

        return travelerRequestDtoList.stream().map(dto -> {
            FlightOrder.Traveler traveler = new FlightOrder.Traveler();
            traveler.setId(dto.getId());
            traveler.setDateOfBirth(dto.getDateOfBirth());

            traveler.setGender(dto.getGender().toString());
            FlightOrder.Name name = new FlightOrder.Name();
            name.setFirstName(dto.getFirstName());
            name.setLastName(dto.getLastName());
            traveler.setName(name);

            FlightOrder.Phone[] phones = dto.getPhones().stream().map(ph -> {
                FlightOrder.Phone phone = new FlightOrder.Phone();
                FlightOrder.Phone.DeviceType type = FlightOrder.Phone.DeviceType
                        .valueOf(ph.getDeviceType().toString().toUpperCase());
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
                FlightOrder.Document.DocumentType docType =
                        FlightOrder.Document.DocumentType.valueOf(
                                d.getDocumentType().toString().toUpperCase());
                doc.setDocumentType(docType);
                doc.setNumber(d.getNumber());
                doc.setExpiryDate(d.getExpiryDate());
                doc.setIssuanceCountry(d.getIssuanceCountry());
                doc.setNationality(d.getNationality());
                doc.setHolder(d.isHolder());

                return doc;
            }).toArray(FlightOrder.Document[]::new);
            traveler.setDocuments(docs);

            return traveler;
        }).toArray(FlightOrder.Traveler[]::new);
    }
}
