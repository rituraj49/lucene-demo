package cl.lcd.controller;

import cl.lcd.service.AmadeusPricingService;
import com.amadeus.resources.FlightOfferSearch;
import com.amadeus.resources.FlightPrice;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import io.swagger.v3.oas.annotations.Operation;
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

import java.util.List;
import java.util.Map;

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
    @ApiResponse(responseCode = "200", description = " return all available flight price   [View Amadeus API Docs](https://developers.amadeus.com/self-service/category/flights/api-doc/flight-offers-price/api-reference)")
    public ResponseEntity<?> searchFlightOfferPrice(@RequestBody List<Map<String, Object>> flightRequest) {
        try {
            String jsonBody = new ObjectMapper().writeValueAsString(flightRequest);

            FlightOfferSearch[] offers = gson.fromJson(jsonBody, FlightOfferSearch[].class);

            FlightPrice result = amadeusPricingService.searchFlightOffersPrice(offers);

            String jsonOutput = gson.toJson(result);

            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(jsonOutput);

        } catch (Exception e) {
            log.error("An Error occurred while processing pricing flight offer search offer API "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
}

