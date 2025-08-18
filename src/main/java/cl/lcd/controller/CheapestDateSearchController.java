package cl.lcd.controller;

import cl.lcd.dto.search.FlightDateResponse;
import cl.lcd.service.CheapestDateSearchService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/flight-dates")
public class CheapestDateSearchController {

    @Autowired
    private CheapestDateSearchService cheapestDateSearchService;


    @Operation(summary = "for find cheapest price date",description = "Give Request PlayLoad is {\n" +
            "  \"origin\": \"MAD\",\n" +
            "  \"destination\": \"LON\"}+ and you can also add i.e departureDate: 2026-01-01, oneWay:false, nonStop:false, maxPrice, viewBy:WEEK")
    @GetMapping("/cheapest")
    public List<FlightDateResponse> getCheapestFlightDates(@RequestParam Map<String, Object> params) throws Exception {
        System.out.println(params);
        log.info("CheapestDateSearchController method params: {}", params);
        return cheapestDateSearchService.getCheapestFlightDates(params);
    }
}
