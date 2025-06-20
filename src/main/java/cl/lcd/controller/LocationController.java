package cl.lcd.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import cl.lcd.util.HelperUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
	public ResponseEntity<?> bulkUploadAirports(
            @RequestParam("file") MultipartFile file) throws IOException {
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
	@Parameter(name = "q", description = "Query string for searching airports", required = true)
	public ResponseEntity<List<AirportResponse>> searchAirports(@RequestParam String q) throws Exception {
		List<AirportResponse> airportResponses = inMemoryLuceneService.search(q);

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
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("something went wrong: " + e.getMessage());
//			throw new RuntimeException();
		}
	}
}
