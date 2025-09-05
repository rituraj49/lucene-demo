package cl.lcd.controller;

import cl.lcd.dto.pricing.FlightPricingConfirmRequest;
import cl.lcd.dto.pricing.FlightPricingConfirmResponse;
import cl.lcd.service.flights.AmadeusPricingService;
import cl.lcd.service.flights.FlightSearchInterface;
import cl.lcd.service.flights.FlightService;
import com.amadeus.exceptions.ResponseException;
import com.google.gson.Gson;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("pricing")
@Slf4j
@Tag(name = "Pricing controller class ")
public class PricingController {

    @Autowired
    AmadeusPricingService amadeusPricingService;

 //   @Autowired
//    FlightService flightService;

    @Autowired
    private FlightSearchInterface flightSearchInterface;

    private final Gson gson = new Gson();

    @PostMapping("/flights/confirm")
    @Operation(
            summary = "Find the price of a flight offer search",
            description = "Step 1: First, search for flights.\n" +
                    "Step 2: Copy the 'pricingAdditionalInfo' value from the flight offer search API response.\n" +
                    "Step 3: Paste it as the 'flightOffer' value in this API."
    )
    @ApiResponse(responseCode = "200", description = " return all available flight",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = FlightPricingConfirmResponse.class)))

    //  @ApiResponse(responseCode = "200", description = " return all available flight price   [View Amadeus API Docs](https://developers.amadeus.com/self-service/category/flights/api-doc/flight-offers-price/api-reference)")
//    public ResponseEntity<?> searchFlightOfferPrice(@RequestBody List<Map<String, Object>> flightRequest) {
    public ResponseEntity<?> searchFlightOfferPrice(@RequestBody FlightPricingConfirmRequest flightRequest) {
        try {
            log.info("flight offer pricing confirmation request received");

//            FlightPricingConfirmResponse response = amadeusPricingService.searchFlightOffersPrice(flightRequest);
            FlightPricingConfirmResponse response = flightSearchInterface.confirmFlightPrice(flightRequest);

            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
        }/*catch (ResponseException e){
            System.out.println("Amadeus Pricing API down, serving offline response...");

            try {
                // Read fallback text file
                Path filePath = new ClassPathResource("flight_pricing_response.text").getFile().toPath();
                String text = Files.readString(filePath);

                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(text);

            } catch (Exception ex) {
                return ResponseEntity.status(500).body("Error loading fallback response");
            }
        }*/
        catch (Exception e) {
            log.error("An Error occurred while processing pricing flight offer search offer API: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
}

