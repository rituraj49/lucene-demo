package cl.lcd.controller;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import cl.lcd.model.Airport;
import cl.lcd.model.AirportResponse;
import cl.lcd.service.InMemoryLuceneService;
import cl.lcd.service.LuceneService;

@RestController
public class AirportController {

//	private LuceneService luceneService;
	private InMemoryLuceneService inMemoryLuceneService;
	
	public AirportController(InMemoryLuceneService inMemoryLuceneService) {
//		this.luceneService = luceneService;
		this.inMemoryLuceneService = inMemoryLuceneService;
	}
	
	@PostMapping("bulk-upload")
	public ResponseEntity<?> bulkUploadAirports(
            @RequestParam("file") MultipartFile file) throws IOException {
        try(Reader reader = new InputStreamReader(file.getInputStream())) {
            CsvToBean<Airport> csvToBean = new CsvToBeanBuilder<Airport>(reader)
                    .withType(Airport.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            List<Airport> airportsList = csvToBean.parse();
//            inMemoryLuceneService.indexData(airportsList);
        } catch (Exception e) {
            System.out.println("caught exception");
            throw new RuntimeException(e);
//            e.printStackTrace();
//            return ResponseEntity.status(500).body("Something went wrong...");
        }

        return ResponseEntity.status(HttpStatus.OK).body("Data uploaded successfully");
    }
	
	@GetMapping("search")
	public ResponseEntity<List<AirportResponse>> searchAirports(@RequestParam String q) throws Exception {
		List<Airport> airports = inMemoryLuceneService.search(q);
        
        List<AirportResponse> airportData = inMemoryLuceneService.getGroupedData(airports);
        return ResponseEntity.status(HttpStatus.OK).body(airportData);
	}
}
