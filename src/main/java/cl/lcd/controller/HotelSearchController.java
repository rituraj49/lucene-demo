package cl.lcd.controller;


import cl.lcd.dto.hotel.HotelSearchResponse;
import cl.lcd.service.hotel.HotelSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Hotel Search", description = "Search Hotels in a City")
@Slf4j
@RestController
@RequestMapping("/api/hotels")
public class HotelSearchController {

//    private static final Logger log = LoggerFactory.getLogger(HotelSearchController.class);

    @Autowired
    private HotelSearchService hotelSearchService;



    @GetMapping("/search")
    @Operation(summary = "Search Hotels by City",
            description = "Search hotels using cityCode (mandatory), radius, radiusUnit, amenities, and ratings.")
    public List<HotelSearchResponse> searchHotels(
            @RequestParam
            @Parameter(description = "Destination city code (IATA 3-letter)", required = true, example = "PAR")
            String cityCode,

            @RequestParam(required = false)
            @Parameter(description = "Maximum distance from city center", example = "5")
            Integer radius,

            @RequestParam(required = false)
            @Parameter(description = "Radius unit (KM or MI)", example = "KM")
            String radiusUnit,

            @RequestParam(required = false)
            @Parameter(description = "List of amenities  like SWIMMING_POOL , \n" +
                    "FITNESS_CENTER, \n" +
                    "AIR_CONDITIONING, \n" +
                    "RESTAURANT, \n" +
                    "PARKING, \n" +
                    "PETS_ALLOWED, \n" +
                    "AIRPORT_SHUTTLE, \n" +
                    "BUSINESS_CENTER, \n" +
                    "DISABLED_FACILITIES, \n" +
                    "WIFI,", example = "[\"SWIMMING_POOL\", \"SPA\"]")
            List<String> amenities,

            @RequestParam(required = false)
            @Parameter(description = "Hotel star ratings (1–5)")//, example = "[\"3\", \"4\"]")
            List<String> ratings
    ) throws Exception {
        return hotelSearchService.getHotels(cityCode, radius, radiusUnit, amenities, ratings);
    }
}
