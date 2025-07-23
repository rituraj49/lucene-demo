package cl.lcd.controller;

import cl.lcd.dto.booking.FlightBookingRequest;
import cl.lcd.dto.booking.FlightBookingResponse;
import cl.lcd.service.AmadeusBookingService;
import cl.lcd.service.PostGreLogsServices;
import cl.lcd.service.UserLogService;
import com.amadeus.Response;
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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("booking")
@Tag(name = "Booking controller class ")
@Slf4j
public class BookingController {

    @Autowired
    AmadeusBookingService amadeusBookingService;

    @Autowired
    private UserLogService userLogService;


    @Autowired
    private PostGreLogsServices postGreLogsServices;



    @PostMapping("flight-order")
    @Operation(
            summary = "Book flight and create flight booking order using Amadeus API",
            description = "Create a flight booking order using the Amadeus API.\n\n" +
                    "The request body should contain:\n" +
                    "- A FlightOffer object in an array\n" +
                    "- Travelers details in the travelers array in JSON format\n\n" +
                    "Example Travelers Documents Payload:\n" +
                    "```json\n" +
                    "[\n" +
                    "  {\n" +
                    "    \"documentType\": \"PASSPORT\",\n" +
                    "    \"number\": \"M1234567\",\n" +
                    "    \"birthPlace\": \"Delhi\",\n" +
                    "    \"issuanceLocation\": \"Delhi\",\n" +
                    "    \"issuanceCountry\": \"IN\",\n" +
                    "    \"issuanceDate\": \"2016-03-10\",\n" +
                    "    \"expiryDate\": \"2026-03-10\",\n" +
                    "    \"validityCountry\": \"IN\",\n" +
                    "    \"nationality\": \"IN\",\n" +
                    "    \"holder\": true\n" +
                    "  }\n" +
                    "]\n" +
                    "```\n\n" +
                    "Steps:\n" +
                    "1. Copy `bookingAdditionalInfo` value from the pricing API.\n" +
                    "2. Paste it as the `flightOffer` value.\n" +
                    "3. Provide travelers documents as shown above."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Flight order created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = FlightBookingResponse.class))
            ),
            @ApiResponse(responseCode = "500", description = "Internal server error while creating flight order"),
    })
    public ResponseEntity<?> createFlightOrder(@RequestBody FlightBookingRequest orderRequest) {
        try {
            log.info("flight booking request received");
            FlightBookingResponse createdOrder = amadeusBookingService.createFlightOrder(orderRequest);

            userLogService.createLoges(orderRequest, createdOrder);
            postGreLogsServices.createLogesPostGreDB(orderRequest, createdOrder);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
        } catch (ResponseException e) {
            log.error("Error occurred while creating flight order: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("something went wrong");
        }
    }
    @GetMapping("flight-order/{orderId}")
    @Operation(summary = "Get flight order by ID",
            description = "Fetch a flight booking order using the Amadeus API by providing the order ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Flight order retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = FlightBookingResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Flight order not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error while fetching flight order")
    })
    public ResponseEntity<?> getFlightOrder(@PathVariable String orderId) {
        try {
            log.info("Fetching flight order with ID: {}", orderId);
            FlightBookingResponse flightOrder = amadeusBookingService.getFlightOrder(orderId);
            if (flightOrder == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Flight order not found");
            }
            log.info("Flight order with ID: {} retrieved successfully", orderId);
            return ResponseEntity.status(HttpStatus.OK).body(flightOrder);
        } catch (ResponseException e) {
            log.error("Error occurred while fetching flight order: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("something went wrong");
        }
    }

    @DeleteMapping("flight-order/{orderId}")
    @Operation(summary = "Delete flight order by ID",
            description = "Delete a flight booking order using the Amadeus API by providing the order ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Flight order deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Flight order not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error while deleting flight order")
    })
    public ResponseEntity<?> deleteFlightOrder(@PathVariable String orderId) {
        try {
            log.info("received request to delete flight order with ID: {}", orderId);
            FlightBookingResponse order = amadeusBookingService.getFlightOrder(orderId);
            if (order == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Flight order not found");
            }
            log.info("Deleting flight order with ID: {}", orderId);
            Response response = amadeusBookingService.cancelFlightOrder(orderId);
            log.info("Flight order with ID: {} deleted successfully", orderId);
            return ResponseEntity.status(response.getStatusCode()).build();
        } catch (ResponseException e) {
            log.error("Error occurred while deleting flight order: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("something went wrong");
        }
    }
}
