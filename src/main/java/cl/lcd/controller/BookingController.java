package cl.lcd.controller;

import cl.lcd.dto.booking.FlightBookingRequest;
import cl.lcd.dto.booking.FlightBookingResponse;
import cl.lcd.service.AmadeusBookingService;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("booking")
@Tag(name = "Booking controller class ")
@Slf4j
public class BookingController {

    @Autowired
    AmadeusBookingService amadeusBookingService;

    @PostMapping("flight-order")
    @Operation(summary = "Book flight and create flight booking order using Amadeus API",
            description = """
					Create a flight booking order using the Amadeus API. 
					The request body should contain the create flight order details i.e. 
					FLightOffer object in an array and Travelers details in the travelers array in JSON format.
					""")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Flight order created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = FlightBookingResponse.class))
            ),
            @ApiResponse(responseCode = "500", description = "Internal server error while creating flight order"),
    })
    public ResponseEntity<?> createFlightOrder(@RequestBody FlightBookingRequest orderRequest) {
        try {
            log.info("flight booking request received: {}", orderRequest.toString());
            FlightBookingResponse createdOrder = amadeusBookingService.createFlightOrder(orderRequest);

            return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
        } catch (ResponseException e) {
            log.error("Error occurred while creating flight order: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("something went wrong");
        }
    }
}
