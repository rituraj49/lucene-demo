package cl.lcd.controller;

import cl.lcd.dto.search.FlightAvailabilityRequest;
import cl.lcd.dto.search.FlightAvailabilityResponse;
import cl.lcd.mappers.flight.FlightSearchResponseMapper;
import cl.lcd.service.flights.AmadeusFlightSearchService;
import cl.lcd.service.flights.FlightService;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOfferSearch;
//import com.amadeus.service.AmadeusLocationSearchService;
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
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/flights/")
@Tag(name = "Amadeus flight search controller class ")
@Slf4j
public class FlightSearchController {

    @Autowired
    private AmadeusFlightSearchService amadeusFlightSearchService;

    @Autowired
    private FlightService flightService;

    private final Gson gson = new Gson();

    //@Operation(summary = "find flight offer search ")
    @ApiResponse(responseCode = "200", description = " return all available flight  ")
    @Operation(
            summary = "find flight offer search",
            description = " Example Payload:\n" +
                    "```json\n" +
                    "{\n" +
                    "  \"originLocationCode\": \"SYD\",\n" +
                    "  \"destinationLocationCode\": \"NYC\",\n" +
                    "  \"departureDate\": \"2025-12-31\",\n" +
                    "  \"returnDate\": \"2026-01-01\",\n" +
                    "  \"maxPrice\": 140000,\n" +
                    "  \"adults\": 1,\n" +
                    "  \"children\":0, \n"+
                    "  \"infants\":0, \n"+
                    "  \"travelClass\": \"ECONOMY\", \n"+
                    "  \"nonStop\": \"false\", \n"+
                    "  \"currencyCode\": \"INR\" ,\n" +
                    "  \"max\": 5\n" +
                    "}\n" +
                    "```"+" max-> show only 5 result \n  if you want to Excluded any Airline than use \"excludedAirlineCodes\":\"AI\" \n or if you want to Included Airlines than use \"includedAirlineCodes\":\"AI\" \n     "
    )
    @GetMapping("/search")
    public ResponseEntity<?> flightOfferSearch(@RequestParam Map<String, String> queryParams)
            throws ResponseException {
        log.info("flight offer search params received: {}", queryParams.toString());

        FlightOfferSearch[] flightOffers = flightService.flightSearch(queryParams);

        List<FlightAvailabilityResponse> flightResponseList = Arrays.stream(flightOffers)
                .map(FlightSearchResponseMapper::createResponse)
                .toList();
        log.info("{} flight offers found", flightResponseList.size());
//        String jsonOutput = gson.toJson(flightOffers);

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(flightResponseList);
    }

    @PostMapping("/search")
    //@Operation(summary = "find multi city flight offer search ")
    @Operation(
            summary = "Find multi-city flight offer search",
            description = "Example Payload:\n" +
                    "```json\n" +
                    "{\n" +
                    "  \"currencyCode\": \"INR\",\n" +
                    "  \"tripDetails\": [\n" +
                    "    {\n" +
                    "      \"id\": \"1\",\n" +
                    "      \"from\": \"BKK\",\n" +
                    "      \"to\": \"BLR\",\n" +
                    "      \"departureDate\": \"2025-12-28\",\n" +
                    "      \"departureTime\": \"10:00:00\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": \"2\",\n" +
                    "      \"from\": \"BLR\",\n" +
                    "      \"to\": \"BOM\",\n" +
                    "      \"departureDate\": \"2025-12-30\",\n" +
                    "      \"departureTime\": \"10:00:00\"\n" +
                    "    }\n" +
                    "  ],\n" +
                    "  \"adults\": 1,\n" +
                    "  \"children\": 0,\n" +
                    "  \"infants\": 0,\n" +
                    "  \"maxCount\": 2,\n" +
                    "  \"cabin\": \"ECONOMY\"\n" +
                    "}\n" +
                    "```"
    )
    @ApiResponse(responseCode = "200", description = " return all available flight",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = FlightAvailabilityResponse.class)))
    public ResponseEntity<?> searchStructuredFlights(@RequestBody FlightAvailabilityRequest flightRequestDto) {
        try {
            log.info("multicity search flight offer request received: {}", flightRequestDto.toString());
            FlightOfferSearch[] flightOffers = flightService.flightMultiCitySearch(flightRequestDto);
//            String jsonOutput = gson.toJson(flightOffers);
//            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(jsonOutput);
            List<FlightAvailabilityResponse> flightResponseList = Arrays.stream(flightOffers)
                    .map(FlightSearchResponseMapper::createResponse)
                    .toList();

            log.info("{} flight offers found", flightResponseList.size());

            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(flightResponseList);
        } catch (Exception e) {
            log.error("An Error occurred while processing multi city search offer API: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
}