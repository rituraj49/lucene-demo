package cl.lcd.controller;

import cl.lcd.service.AmadeusServiceNew;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOfferSearch;
//import com.amadeus.service.AmadeusService;
import com.amadeus.resources.FlightPrice;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/flights/")
@Tag(name = "Amadeus flights controller class ")
@Slf4j
public class FlightController {

    @Autowired
    private AmadeusServiceNew amadeusServiceNew;

    private Gson gson;

/*
    @GetMapping("search")
    public ResponseEntity<String> flightOfferSearch() throws ResponseException, JsonProcessingException {
       FlightOfferSearch[] offerSearches= amadeusService.flightOfferSearches();
        for(FlightOfferSearch flightOfferSearch:offerSearches){
            System.out.println(flightOfferSearch);
        }


        Gson gson = new Gson();
        String jsonOutput = gson.toJson(offerSearches);

        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonOutput);
       // return new ResponseEntity<>("Flight search executed successfully", HttpStatus.OK);
    }
*/

    @GetMapping("/search")
    @Operation(summary = "find flight offer search ")
    @ApiResponse(responseCode = "200", description = " return all available flight  ")
    @Parameter(name = "[View Amadeus API Docs] https://developers.amadeus.com/self-service/category/air/api-doc/flight-offers-search/api-reference ")
    public ResponseEntity<?> flightOfferSearch(@RequestParam Map<String, String> queryParams)
            throws ResponseException {

        FlightOfferSearch[] offers = amadeusServiceNew.flightOfferSearches(queryParams);

        // Convert response to JSON using Gson
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonOutput = gson.toJson(offers);
        log.info("flight offer search controller class call ");
        log.info("Total number of records : "+offers.length);
        //System.out.println(offers.length);
        return ResponseEntity.ok(jsonOutput);
    }

    @PostMapping("/multicity-search")
    @Operation(summary = "find multi city flight offer search ")
    @ApiResponse(responseCode = "200", description = " return all available flight    [View Amadeus API Docs](https://developers.amadeus.com/self-service/category/air/api-doc/flight-offers-search/api-reference)")
    public ResponseEntity<?> searchStructuredFlights(@RequestBody Map<String, Object> flightRequest) {
        try {
            log.info("multicity search flight offer request received: {}", flightRequest.toString());
            FlightOfferSearch[] result = amadeusServiceNew.searchMultiCityFlightOffers(flightRequest);
            String jsonOutput = gson.toJson(result);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(jsonOutput);

        } catch (Exception e) {
            log.error("An Error occurred while processing multi city search offer API "+e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
            return null;
        }
    }

    @PostMapping("/confirm-price")
    @Operation(summary = "find price of flight offer search ")
    @ApiResponse(responseCode = "200", description = " return all available flight price   [View Amadeus API Docs](https://developers.amadeus.com/self-service/category/flights/api-doc/flight-offers-price/api-reference)")
    public ResponseEntity<?> searchFlightOfferPrice(@RequestBody List<Map<String, Object>> flightRequest) {
        try {

            String jsonBody = new ObjectMapper().writeValueAsString(flightRequest);

            FlightOfferSearch[] offers = gson.fromJson(jsonBody, FlightOfferSearch[].class);

            FlightPrice result = amadeusServiceNew.searchFlightOffersPrice(offers);

            String jsonOutput = gson.toJson(result);
//            // Convert raw JSON string to a JSON object (Map)
//            ObjectMapper objectMapper = new ObjectMapper();
//            Map<String, Object> jsonMap = objectMapper.readValue(result, Map.class);
//            // System.out.println(jsonMap.size());
//
//
//            log.info("pricing for flight offer search controller class call ");
//            log.info("Total number of records : "+jsonMap.size());

            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(jsonOutput);

        } catch (Exception e) {
            log.error("An Error occurred while processing pricing flight offer search offer API "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

}