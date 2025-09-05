package cl.lcd.controller;

import cl.lcd.service.hotel.HotelOfferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/hotels")
@Slf4j
public class HotelOfferController {

    private static final Logger log = LoggerFactory.getLogger(HotelOfferController.class);

    @Autowired
    private HotelOfferService hotelOfferService;


    @GetMapping("/offers")
    @Operation(summary = "Get Hotel Offers", description = "Search hotel offers by hotelIds, dates, guests, and filters.")
    public ResponseEntity<?> getHotelOffers(
            @RequestParam
            @Parameter(description = "Amadeus property codes (8 chars). Mandatory when searching by predefined list of hotels.", required = true, example = "PAR12345")
            List<String> hotelIds,

            @RequestParam(required = false)
            @Parameter(description = "Number of adult guests (1-9) per room", example = "2")
            Integer adults,

            @RequestParam(required = false)
            @Parameter(description = "Check-in date (YYYY-MM-DD)", example = "2025-09-01")
            String checkInDate,

            @RequestParam(required = false)
            @Parameter(description = "Check-out date (YYYY-MM-DD)", example = "2025-09-02")
            String checkOutDate,

            @RequestParam(required = false)
            @Parameter(description = "Number of rooms requested (1-9)", example = "1")
            Integer roomQuantity,

            @RequestParam(required = false)
            @Parameter(description = "Return only the cheapest offer per hotel if true", example = "true")
            Boolean bestRateOnly
    ) {
        try {
            // Build params map for service
            Map<String, String> params = new HashMap<>();
            params.put("hotelIds", String.join(",", hotelIds));
            if (adults != null) params.put("adults", adults.toString());
            if (checkInDate != null) params.put("checkInDate", checkInDate);
            if (checkOutDate != null) params.put("checkOutDate", checkOutDate);
            if (roomQuantity != null) params.put("roomQuantity", roomQuantity.toString());
            if (bestRateOnly != null) params.put("bestRateOnly", bestRateOnly.toString());

            return ResponseEntity.ok(hotelOfferService.getOffers(params));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", e.getMessage()));
        }
    }

}

