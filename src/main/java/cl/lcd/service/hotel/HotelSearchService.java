package cl.lcd.service.hotel;


import cl.lcd.dto.hotel.HotelSearchResponse;
import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.Hotel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class HotelSearchService {

    private static final Logger log = LoggerFactory.getLogger(HotelSearchService.class);

    @Autowired
    private Amadeus amadeusClient;

    public List<HotelSearchResponse> getHotels(String cityCode,
                                               Integer radius,
                                               String radiusUnit,
                                               List<String> amenities,
                                               List<String> ratings) throws ResponseException {

        Params params = Params.with("cityCode", cityCode);

        if (radius != null) params.and("radius", radius);
        if (radiusUnit != null) params.and("radiusUnit", radiusUnit);
        if (amenities != null && !amenities.isEmpty()) params.and("amenities", String.join(",", amenities));
        if (ratings != null && !ratings.isEmpty()) params.and("ratings", String.join(",", ratings));

        Hotel[] hotels = amadeusClient.referenceData.locations.hotels.byCity.get(params);

        return Arrays.stream(hotels)
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }


    private HotelSearchResponse mapToDto(Hotel hotel) {
        HotelSearchResponse dto = new HotelSearchResponse();
        dto.setChainCode(hotel.getChainCode());
        dto.setIataCode(hotel.getIataCode());
      //  dto.setDupeId(hotel.getDupeId());
        dto.setName(hotel.getName());
        dto.setHotelId(hotel.getHotelId());

        if (hotel.getGeoCode() != null) {
            dto.setGeoCode(new HotelSearchResponse.GeoCode(
                    hotel.getGeoCode().getLatitude(),
                    hotel.getGeoCode().getLongitude()
            ));
        }

        if (hotel.getAddress() != null) {
            dto.setAddress(new HotelSearchResponse.Address(
                    hotel.getAddress().getCountryCode(),
                    hotel.getAddress().getPostalCode(),
                    hotel.getAddress().getCityName(),
                    Arrays.asList(hotel.getAddress().getLines())
            ));
        }

        if (hotel.getDistance() != null) {
            dto.setDistance(new HotelSearchResponse.Distance(
                    hotel.getDistance().getUnit(),
                    hotel.getDistance().getValue()
            ));
        }

//        dto.setLastUpdate(hotel.getLastUpdate());
        return dto;
    }


}
