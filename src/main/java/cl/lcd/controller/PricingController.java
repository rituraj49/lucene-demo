package cl.lcd.controller;

import cl.lcd.dto.pricing.FlightPricingConfirmRequest;
import cl.lcd.dto.pricing.FlightPricingConfirmResponse;
import cl.lcd.dto.search.FlightAvailabilityResponse;
import cl.lcd.service.AmadeusPricingService;
import com.amadeus.resources.FlightOfferSearch;
import com.google.gson.Gson;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("pricing")
@Slf4j
@Tag(name = "Pricing controller class ")
public class PricingController {

    @Autowired
    AmadeusPricingService amadeusPricingService;

    private final Gson gson = new Gson();

    @PostMapping("/flights/confirm")
    @Operation(summary = "find price of flight offer search ")
    @ApiResponse(responseCode = "200", description = " return all available flight",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = FlightPricingConfirmResponse.class)))

    //  @ApiResponse(responseCode = "200", description = " return all available flight price   [View Amadeus API Docs](https://developers.amadeus.com/self-service/category/flights/api-doc/flight-offers-price/api-reference)")
//    public ResponseEntity<?> searchFlightOfferPrice(@RequestBody List<Map<String, Object>> flightRequest) {
    public ResponseEntity<?> searchFlightOfferPrice(@RequestBody FlightPricingConfirmRequest flightRequest) {
        try {
            log.info("flight offer pricing confirmation request received");

            FlightPricingConfirmResponse response = amadeusPricingService.searchFlightOffersPrice(flightRequest);

            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
        } catch (Exception e) {
            log.error("An Error occurred while processing pricing flight offer search offer API: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
}

