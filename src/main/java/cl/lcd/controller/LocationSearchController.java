package cl.lcd.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Map;

import cl.lcd.model.LocationResponse;
import cl.lcd.model.LocationResponseWrapper;
import cl.lcd.service.locations.AmadeusLocationSearchService;
import cl.lcd.service.locations.ElasticsearchService;
import cl.lcd.util.HelperUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
import org.springframework.web.multipart.MultipartFile;
import com.amadeus.exceptions.ResponseException;

import cl.lcd.model.Airport;
import cl.lcd.service.locations.InMemoryLuceneService;

@RestController
@Slf4j
@Tag(name = "Locations search controller class ")
@RequestMapping("locations")
public class LocationSearchController {

	@Autowired
	private InMemoryLuceneService inMemoryLuceneService;

	@Autowired
	private AmadeusLocationSearchService amadeusLocationSeArchService;

	@Autowired
	private ElasticsearchService elasticsearchService;


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
			try(Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
				List<Airport> airportsList = HelperUtil.convertCsv(reader, Airport.class);
			} catch (IOException e) {
				log.error("Error reading CSV file: {}", e.getMessage());
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error reading CSV file: " + e.getMessage());
			}
        return ResponseEntity.status(HttpStatus.OK).body("Data uploaded successfully");
    }

	@GetMapping("search")
	@Operation(summary = "Search for airports using in-memory Lucene index",
			description = """
					Search for airports using an in-memory Lucene index. The query parameter 'keyword' should be provided. payload: new york
					""")
	@ApiResponse(responseCode = "200", description = "Search for airports using in-memory Lucene index")
	@Parameter(name = "keyword", description = "Query string for searching airports", required = true)
	public ResponseEntity<?> searchAirports(@RequestParam String keyword) throws Exception {
//		List<LocationResponse> airportResponses = inMemoryLuceneService.search(keyword);
		LocationResponseWrapper airportResponses = inMemoryLuceneService.search(keyword);

        return ResponseEntity.status(HttpStatus.OK).body(airportResponses);
	}

 	@GetMapping("amadeus-search")
	@Operation(
			summary = "Find airport or city location by keyword",
			description = "Example payloads:\n" +
					"```json\n" +
					"{\n" +
					"  \"keyword\": \"BOM\",\n" +
					"  \"subType\": \"CITY,AIRPORT\"\n" +
					"}\n" +
					"\n" +
					"{\n" +
					"  \"keyword\": \"JFK\",\n" +
					"  \"subType\": \"AIRPORT\"\n" +
					"}\n" +
					"\n" +
					"{\n" +
					"  \"keyword\": \"HYD\",\n" +
					"  \"subType\": \"CITY\"\n" +
					"}\n" +
					"```" +" you can use any one "
	)
	@ApiResponse(responseCode = "200", description = "Search for locations using Amadeus API")
	@Parameter(name = "params", description = "Query parameters in the form of keyword=\"nyc\" and subType=\"CITY,AIRPORT\" pairs for searching locations", required = true)
	public ResponseEntity<?> searchForLocations(@RequestParam Map<String, String> params) {
		try {
			log.info("params received in searchForLocations: {}", params);
//			List<LocationResponse> response = amadeusLocationSeArchService.searchLocations(params);
			LocationResponseWrapper response = amadeusLocationSeArchService.searchLocations(params);

			return ResponseEntity.status(HttpStatus.OK).body(response);

		} catch (ResponseException e) {
			log.error("Error occurred while searching for locations: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("something went wrong: " + e.getMessage());
		}
	}

	@Operation(
			summary = "Search airports",
			description = """
            Performs a text search on the `airports` index.
            The query must be of the form `field:value`, e.g. `name:Heathrow`.
            """
	)
	@Parameters({
			@Parameter(name = "query",
					description = "Search expression in the format `field:value`",
					example = "country:India",
					required = true)
	})
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Search results",
					content = @Content(array = @ArraySchema(schema = @Schema(implementation = Airport.class)))),
			@ApiResponse(responseCode = "400", description = "Invalid query",
					content = @Content(schema = @Schema(implementation = String.class)))
	})
	@GetMapping("elastic-search-field")
	public ResponseEntity<?> searchAirports(
			@RequestParam String query,
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size
	) {
		try {
			List<Airport> result = elasticsearchService.searchByText(query, page, size);
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to fetch data");
		}
	}

	@GetMapping("elastic-search")
	public ResponseEntity<?> searchLocations(
			@RequestParam String keyword,
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "20") int size
	) {
		try {
			log.info("Received keyword for search: {}", keyword);
//			List<LocationResponse> result = elasticsearchService.searchByKeyword(keyword, page, size);
			LocationResponseWrapper result = elasticsearchService.searchByKeyword(keyword, page, size);

			return ResponseEntity.status(HttpStatus.OK).body(result);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error occurred while searching locations: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch data");
		}
	}
}
