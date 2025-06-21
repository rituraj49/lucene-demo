package cl.lcd.controller;

import cl.lcd.service.AmadeusServiceNew;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOfferSearch;
//import com.amadeus.service.AmadeusService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v2/")
@Tag(name = "Amadeus Controller class ")
@Slf4j
public class AmadeusControllers {

    @Autowired
    private AmadeusServiceNew amadeusServiceNew;

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
    public ResponseEntity<String> flightOfferSearch(@RequestParam Map<String, String> queryParams)
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







    @PostMapping("/structured-search")
    @Operation(summary = "find multi city flight offer search ")
    @ApiResponse(responseCode = "200", description = " return all available flight    [View Amadeus API Docs](https://developers.amadeus.com/self-service/category/air/api-doc/flight-offers-search/api-reference)")
    public ResponseEntity<?> searchStructuredFlights(@RequestBody Map<String, Object> flightRequest) {
        try {

            String result = amadeusServiceNew.searchMultiCityFlightOffers(flightRequest);

            // Convert raw JSON string to a JSON object (Map)
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> jsonMap = objectMapper.readValue(result, Map.class);
           // System.out.println(jsonMap.size());


            log.info("Multi city flight offer search controller class call ");
            log.info("Total number of records : "+jsonMap.size());


            return ResponseEntity.ok(jsonMap);  // Returned as proper application/json


        } catch (Exception e) {
            log.error("An Error occurred while processing multi city search offer API "+e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }



    /*@PostMapping("/confirm")
    public FlightPrice confirm(@RequestBody(required=true) FlightOfferSearch search) throws ResponseException {
        return AmadeusConnect.INSTANCE.confirm(search);
    }*/


    @PostMapping("/confirm")
    @Operation(summary = "find price of flight offer search ")
    @ApiResponse(responseCode = "200", description = " return all available flight price   [View Amadeus API Docs](https://developers.amadeus.com/self-service/category/flights/api-doc/flight-offers-price/api-reference)")
    public ResponseEntity<?> searchFlightOfferPrice(@RequestBody Map<String, Object> flightRequest) {
        try {

            String result = amadeusServiceNew.searchFlightOffersPrice(flightRequest);

            // Convert raw JSON string to a JSON object (Map)
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> jsonMap = objectMapper.readValue(result, Map.class);
            // System.out.println(jsonMap.size());


            log.info("pricing for flight offer search controller class call ");
            log.info("Total number of records : "+jsonMap.size());


            return ResponseEntity.ok(jsonMap);  // Returned as proper application/json


        } catch (Exception e) {
            log.error("An Error occurred while processing pricing flight offer search offer API "+e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }


}