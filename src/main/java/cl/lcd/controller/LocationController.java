package cl.lcd.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import cl.lcd.util.HelperUtil;
import com.amadeus.resources.FlightOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.amadeus.exceptions.ResponseException;

import cl.lcd.model.Airport;
import cl.lcd.model.AirportResponse;
import cl.lcd.service.AmadeusService;
import cl.lcd.service.InMemoryLuceneService;

@RestController
@Slf4j
public class LocationController {

	//	private LuceneService luceneService;
	private InMemoryLuceneService inMemoryLuceneService;
	private AmadeusService amadeusService;

//	private Gson gson;

	@Autowired
	public LocationController(InMemoryLuceneService inMemoryLuceneService, AmadeusService amadeusService) {
//		this.luceneService = luceneService;
		this.inMemoryLuceneService = inMemoryLuceneService;
		this.amadeusService = amadeusService;
	}


	@PostMapping("bulk-upload")
	@Operation(
			summary = "Bulk-upload airports CSV",
			description = """
            Accepts a CSV file, parses it into `Airport` beans and
            performs a bulk upload into the `airports` index.
            The CSV columns must map to the fields of the `Airport` class.
            """
	)
	@ApiResponse(responseCode = "400", description = "Bad CSV or parse error",
			content = @Content(schema = @Schema(implementation = String.class)))
	@Parameter(name = "file", description = "CSV file containing airport data", required = true)
	public ResponseEntity<?> bulkUploadAirports(@RequestParam("file") MultipartFile file) throws IOException {
			log.info("Received file for bulk upload: {}", file.getOriginalFilename());
            List<Airport> airportsList = HelperUtil.convertCsv(file, Airport.class);

        return ResponseEntity.status(HttpStatus.OK).body("Data uploaded successfully");
    }

	@GetMapping("search")
	@Operation(summary = "Search for airports using in-memory Lucene index",
			description = """
					Search for airports using an in-memory Lucene index. The query parameter 'q' should be provided.
					""")
	@ApiResponse(responseCode = "200", description = "Search for airports using in-memory Lucene index")
	@Parameter(name = "keyword", description = "Query string for searching airports", required = true)
	public ResponseEntity<List<AirportResponse>> searchAirports(@RequestParam String keyword) throws Exception {
		List<AirportResponse> airportResponses = inMemoryLuceneService.search(keyword);

//        List<AirportResponse> airportData = inMemoryLuceneService.getGroupedData(airports);
        return ResponseEntity.status(HttpStatus.OK).body(airportResponses);
	}

 	@GetMapping("amadeus-search")
	@Operation(summary = "Search for locations using Amadeus API",
			description = """
					Search for locations such as airports or cities using the Amadeus API. " +
					"Query parameters should include atleast 'subType' and 'keyword'.
					""")
	@ApiResponse(responseCode = "200", description = "Search for locations using Amadeus API")
	@Parameter(name = "params", description = "Query parameters in the form of key=value pairs for searching locations", required = true)
	public ResponseEntity<?> searchForLocations(@RequestParam Map<String, String> params) {
		try {
			log.info("params received in searchForLocations: {}", params);
//	        	List<Airport> airports = amadeusService.searchLocations(params);
			List<AirportResponse> response = amadeusService.searchLocations(params);

//	            List<AirportResponse> response = HelperUtil.getGroupedData(airports);

			return ResponseEntity.status(HttpStatus.OK).body(response);

		} catch (ResponseException e) {
//	            e.printStackTrace();
			log.error("Error occurred while searching for locations: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("something went wrong: " + e.getMessage());
//			throw new RuntimeException();
		}
	}

	@PostMapping("create-order")
	@Operation(summary = "Book flight and create flight booking order using Amadeus API",
			description = """
					Create a flight booking order using the Amadeus API. 
					The request body should contain the create flight order details i.e. 
					FLightOffer object in an array and Travelers details in the travelers array in JSON format.
					""")
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "Flight order created successfully"),
			@ApiResponse(responseCode = "500", description = "Internal server error while creating flight order"),
	})
	@Parameter(name = "params", description = "Query parameters in the form of key=value pairs for searching locations", required = true)
	public ResponseEntity<?> createFlightOrder(@RequestBody Map<String, Object> orderRequest) {
        try {
			Gson gson = new Gson();
//			System.out.println("ord req" + orderRequest.toString());
			String jsonString = new ObjectMapper().writeValueAsString(orderRequest);
			JsonObject gsonObject = JsonParser.parseString(jsonString).getAsJsonObject();

            FlightOrder createdOrder = amadeusService.createFlightOrder(gsonObject);
			System.out.println("createdOrder: "+createdOrder.toString());
			String result = gson.toJson(createdOrder);

			return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (ResponseException | JsonProcessingException e) {
			log.error("Error occurred while creating flight order: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("something went wrong");
//			throw new RuntimeException(e);
		}
    }
}
