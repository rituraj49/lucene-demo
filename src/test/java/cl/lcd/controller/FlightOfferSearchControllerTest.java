package cl.lcd.controller;

//import cl.lcd.dto.search.FlightOfferSearchDto;
import cl.lcd.dto.pricing.FlightPricingConfirmResponse;
import cl.lcd.dto.search.FlightAvailabilityRequest;
import cl.lcd.service.AmadeusFlightSearchService;
import cl.lcd.service.AmadeusPricingService;
import com.amadeus.resources.FlightOfferSearch;
import com.amadeus.resources.FlightPrice;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
@WebMvcTest({FlightSearchController.class,PricingController.class})
@AutoConfigureMockMvc(addFilters = false)
public class FlightOfferSearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AmadeusFlightSearchService amadeusFlightSearchService;

    @MockBean
    private AmadeusPricingService amadeusPricingService;

    @Test
    public void testFlightOfferSearch() throws Exception {
        // 1. Load JSON file from resources
        String jsonResponse = new String(
                Files.readAllBytes(Paths.get("src/test/resources/flightOffers.json"))
        );

        // 2. Parse JSON into FlightOfferSearch[] (using Jackson)
        ObjectMapper mapper = new ObjectMapper();
        FlightOfferSearch[] mockFlights = mapper.readValue(jsonResponse, FlightOfferSearch[].class);

        // 3. Mock the service to return parsed data
        when(amadeusFlightSearchService.flightOfferSearches(anyMap()))
                .thenReturn(mockFlights);

        // 4. Test the endpoint (compare with the raw JSON)
        mockMvc.perform(get("/flights/search")
                        .param("originLocationCode", "DEL")
                        .param("destinationLocationCode", "DXB")
                        .param("departureDate", "2025-07-01")
                        .param("adults", "2"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonResponse)); // Directly compare JSON
    }


    @Test
    public void testPriceFlightOfferSearch() throws Exception {
        // 1. Load mock request data (flightOffers.json)
        String inputJson = Files.readString(Paths.get("src/test/resources/flightOfferPricingRequest.json"));

        // 2. Deserialize to actual model
        Gson gson = new Gson();
        FlightOfferSearch[] inputOffers = gson.fromJson(inputJson, FlightOfferSearch[].class);

        // 3. Create mock response object
        FlightPricingConfirmResponse mockPrice = mock(FlightPricingConfirmResponse.class); // or mock(FlightPrice.class);
       // mockPrice.setResponse(null); // set mock fields if needed

        // 4. Stub the service
        when(amadeusPricingService.searchFlightOffersPrice(any()))
                .thenReturn(mockPrice);

        // 5. Perform the POST request
        mockMvc.perform(post("/pricing/flights/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputJson))
                .andExpect(status().isOk());
    }


/*
    @Test
    public void testMultiCityFlightSearch() throws Exception {
        // 1. Prepare input DTO
        FlightOfferSearchDto dto = new FlightOfferSearchDto();
        dto.setCurrencyCode("INR");
        dto.setOneWay(false);
        dto.setMaxCount(10);
        dto.setCabin(FlightOfferSearchDto.Cabin.ECONOMY);

        // Origin/Destination Legs
        FlightOfferSearchDto.OriginDestinationsDto leg1 = new FlightOfferSearchDto.OriginDestinationsDto();
        leg1.setId("1");
        leg1.setFrom("DEL");
        leg1.setTo("BOM");
        leg1.setDepartureDate(LocalDate.of(2025, 7, 10));
        leg1.setDepartureTime(LocalTime.of(10, 30));

        FlightOfferSearchDto.OriginDestinationsDto leg2 = new FlightOfferSearchDto.OriginDestinationsDto();
        leg2.setId("2");
        leg2.setFrom("BOM");
        leg2.setTo("DXB");
        leg2.setDepartureDate(LocalDate.of(2025, 7, 15));
        leg2.setDepartureTime(LocalTime.of(12, 0));

        dto.setTripDetails(List.of(leg1, leg2));

        // Travelers
        FlightOfferSearchDto.TravelerInfoDto traveler = new FlightOfferSearchDto.TravelerInfoDto();
        traveler.setId("T1");
        traveler.setTravelerType(FlightOfferSearchDto.TravelerType.ADULT);

        dto.setTravelers(List.of(traveler));

        // 2. Mock service response
        FlightOfferSearch[] mockResponse = new FlightOfferSearch[0]; // or use mock(FlightOfferSearch[].class)
        when(amadeusFlightSearchService.searchMultiCityFlightOffers(any()))
                .thenReturn(mockResponse);

        // 3. Perform POST request
        mockMvc.perform(post("/flights/search") // replace with actual path, e.g., /flights/search
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }
*/

    @Test
    void testSearchMultiFlights_ReturnsFlightOffers() throws Exception {
        // Given: Prepare sample request DTO
        FlightAvailabilityRequest.TripDetailsDto trip = new FlightAvailabilityRequest.TripDetailsDto();
        trip.setId("1");
        trip.setFrom("DEL");
        trip.setTo("BOM");
        trip.setDepartureDate(LocalDate.of(2025, 7, 15));
        trip.setDepartureTime(LocalTime.of(10, 0));


        FlightAvailabilityRequest request = new FlightAvailabilityRequest();
        request.setCurrencyCode("INR");
        request.setTripDetails(List.of(trip));
        request.setAdults(1);
        request.setChildren(0);
        request.setInfants(0);
        request.setMaxCount(3);
        request.setCabin(FlightAvailabilityRequest.Cabin.ECONOMY);

        // Mock service behavior
        FlightOfferSearch mockOffer = mock(FlightOfferSearch.class);
        FlightOfferSearch[] mockResult = new FlightOfferSearch[]{mockOffer};

        when(amadeusFlightSearchService.searchMultiCityFlightOffers(any(FlightAvailabilityRequest.class)))
                .thenReturn(mockResult);

        // When: Perform POST request
        mockMvc.perform(post("/flights/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }



}
