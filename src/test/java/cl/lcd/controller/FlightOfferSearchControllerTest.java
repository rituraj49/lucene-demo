package cl.lcd.controller;

//import cl.lcd.dto.search.FlightOfferSearchDto;
import cl.lcd.dto.search.FlightAvailabilityRequest;
import cl.lcd.dto.search.FlightAvailabilityResponse;
import cl.lcd.mappers.flight.FlightSearchResponseMapper;
import cl.lcd.model.FlightResponseWrapper;
import cl.lcd.service.flights.AmadeusPricingService;
import cl.lcd.service.flights.FlightService;
import com.amadeus.resources.FlightOfferSearch;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

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

//    @MockBean
//    private AmadeusFlightSearchService amadeusFlightSearchService;

    @MockBean
    private FlightService flightService;

    @MockBean
    private AmadeusPricingService amadeusPricingService;

    @Test
    public void testFlightOfferSearch() throws Exception {
        FlightOfferSearch mockOffer = mock(FlightOfferSearch.class);
//        FlightOfferSearch[] mockOffers = new FlightOfferSearch[]{mockOffer};
        try (MockedStatic<FlightSearchResponseMapper> mockMapper = Mockito.mockStatic(FlightSearchResponseMapper.class)) {

//        List<FlightAvailabilityResponse> mockResponse = List.of(mockOffers);

        FlightAvailabilityResponse mockResponse = new FlightAvailabilityResponse();
//            when(amadeusFlightSearchService.flightOfferSearch(anyMap()))
//                .thenReturn(mockResponse);

//        try (MockedStatic<FlightSearchResponseMapper> mockMapper = Mockito.mockStatic(FlightSearchResponseMapper.class)) {
            mockMapper.when(() -> FlightSearchResponseMapper.createResponse(mockOffer))
                    .thenReturn(mockResponse);
//            when(flightService.flightOfferSearch((anyMap()))).thenReturn(List.of(mockResponse));
            when(flightService.flightSearch((anyMap()))).thenReturn(List.of(mockResponse));

            mockMvc.perform(get("/flights/search")
                            .param("originLocationCode", "DEL")
                            .param("destinationLocationCode", "DXB")
                            .param("departureDate", "2025-07-01")
                            .param("adults", "2"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().json(
                            objectMapper.writeValueAsString(new FlightResponseWrapper
                                (List.of(mockResponse)
                        )
                    )
                )
            );
        }
    }

    @Test
    void testSearchMultiFlights_ReturnsFlightOffers() throws Exception {
        FlightOfferSearch mockOffer = mock(FlightOfferSearch.class);
        FlightOfferSearch[] mockOffers = new FlightOfferSearch[]{mockOffer};

        try (MockedStatic<FlightSearchResponseMapper> mockMapper = Mockito.mockStatic(FlightSearchResponseMapper.class)) {

//        List<FlightAvailabilityResponse> mockResponse = List.of(mockOffers);

            FlightAvailabilityResponse mockResponse = new FlightAvailabilityResponse();
//            mockMapper.when(() -> FlightSearchResponseMapper.createResponse(mockOffer))
//                    .thenReturn(mockResponse);
//        when(flightService.flightOfferSearch(anyMap()))
            FlightAvailabilityRequest flightReq = new FlightAvailabilityRequest();

            mockMapper.when(() -> FlightSearchResponseMapper.createResponse(mockOffer))
                    .thenReturn(mockResponse);

            when(flightService.flightMultiCitySearch(flightReq))
                .thenReturn(List.of(mockResponse));

//        FlightAvailabilityResponse mockResponse = new FlightAvailabilityResponse();
//        try (MockedStatic<FlightSearchResponseMapper> mockMapper = Mockito.mockStatic(FlightSearchResponseMapper.class)) {
//            mockMapper.when(() -> FlightSearchResponseMapper.createResponse(mockOffer))
//                    .thenReturn(mockResponse);

            mockMvc.perform(get("/flights/search")
                            .param("originLocationCode", "DEL")
                            .param("destinationLocationCode", "DXB")
                            .param("departureDate", "2025-07-01")
                            .param("adults", "2"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
//                    .andExpect(content().json(
//                        objectMapper.writeValueAsString
//                            (new FlightResponseWrapper(List.of(mockResponse)
//                        )
//                    )
//                )
//            );
        }
    }
}