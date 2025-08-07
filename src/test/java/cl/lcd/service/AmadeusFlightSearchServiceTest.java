package cl.lcd.service;
/*

import cl.lcd.dto.search.FlightAvailabilityRequest;
import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.Shopping;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOfferSearch;
import com.amadeus.shopping.FlightOffersSearch;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

//@SpringBootTest
public class AmadeusFlightSearchServiceTest {
*/
/*


    @Mock
    private Amadeus amadeusClient;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private Shopping shopping;

    @InjectMocks
    private AmadeusFlightSearchService flightSearchService;

    @Mock
    private FlightOffersSearch flightOffersSearch;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // initialize mocks

        // Mock the nested shopping object
        when(amadeusClient.shopping).thenReturn(shopping);

        // Mock the nested flightOffersSearch
        when(shopping.flightOffersSearch).thenReturn(flightOffersSearch);
    }

    @Test
    public void testFlightOfferSearches() throws ResponseException {
        // Arrange
        Map<String, String> params = new HashMap<>();
        params.put("originLocationCode", "DEL");
        params.put("destinationLocationCode", "BOM");

        // Mocking the amadeusClient response
        FlightOfferSearch[] mockResponse = new FlightOfferSearch[1];
        when(amadeusClient.shopping.flightOffersSearch.get(any())).thenReturn(mockResponse);

        // Act
        FlightOfferSearch[] result = flightSearchService.flightOfferSearches(params);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.length);
        verify(amadeusClient.shopping.flightOffersSearch, times(1)).get(any());
    }
*//*


    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private Amadeus amadeusClient;

    @Mock
    private FlightOffersSearch flightOffersSearch;

    @InjectMocks
    private AmadeusFlightSearchService flightSearchService;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        // Inject the mock flightOffersSearch into amadeusClient.shopping using reflection
        // because 'shopping' is a public field, not a method

        // First, create a Shopping mock
        com.amadeus.Shopping shoppingMock = mock(com.amadeus.Shopping.class);

        // Inject shoppingMock into amadeusClient
        Field shoppingField = Amadeus.class.getField("shopping");
        shoppingField.set(amadeusClient, shoppingMock);

        // Inject flightOffersSearch into shoppingMock
        Field fosField = com.amadeus.Shopping.class.getField("flightOffersSearch");
        fosField.set(shoppingMock, flightOffersSearch);
    }

    @Test
    public void testFlightOfferSearches() throws ResponseException {
        // Arrange
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("originLocationCode", "DEL");
        paramsMap.put("destinationLocationCode", "BOM");

        // Mock the FlightOfferSearch response
        FlightOfferSearch[] mockResponse = new FlightOfferSearch[1];
        when(flightOffersSearch.get(any(Params.class))).thenReturn(mockResponse);

        // Act
        FlightOfferSearch[] result = flightSearchService.flightOfferSearches(paramsMap);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.length, "Result length should be 1");

        // Verify that flightOffersSearch.get() was called once with any Params
        verify(flightOffersSearch, times(1)).get(any(Params.class));
    }

    @Test
    public void testSearchMultiCityFlightOffers() throws ResponseException, JsonProcessingException {
        // request DTO
        FlightAvailabilityRequest requestDto = new FlightAvailabilityRequest();
        requestDto.setCurrencyCode("INR");
        requestDto.setAdults(1);
        requestDto.setChildren(1);
        requestDto.setInfants(1);
        requestDto.setMaxCount(5);
        requestDto.setCabin(FlightAvailabilityRequest.Cabin.ECONOMY);

        FlightAvailabilityRequest.TripDetailsDto trip = new FlightAvailabilityRequest.TripDetailsDto();
        trip.setId("1");
        trip.setFrom("DEL");
        trip.setTo("BOM");
        trip.setDepartureDate(LocalDate.of(2025, 1, 1));
        trip.setDepartureTime(LocalTime.of(10, 0));
        requestDto.setTripDetails(List.of(trip));

        // Mock objectMapper.writeValueAsString()
        when(objectMapper.writeValueAsString(any())).thenReturn("{\"mocked\":\"body\"}");

        // Mock FlightOfferSearch response with a mock object (cannot instantiate directly)
        FlightOfferSearch mockOffer = mock(FlightOfferSearch.class);
        FlightOfferSearch[] mockResponse = new FlightOfferSearch[]{mockOffer};
        when(flightOffersSearch.post(anyString())).thenReturn(mockResponse);

        // Act
        FlightOfferSearch[] result = flightSearchService.searchMultiCityFlightOffers(requestDto);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.length, "Result length should be 1");

        // Verify mocks
        verify(objectMapper, times(1)).writeValueAsString(any());
        verify(flightOffersSearch, times(1)).post(anyString());
    }

    @Test
    public void testSearchMultiCityFlightOffers_whenNullResponse() throws ResponseException, JsonProcessingException {
        // Arrange
        FlightAvailabilityRequest requestDto = new FlightAvailabilityRequest();
        requestDto.setCurrencyCode("INR");
        requestDto.setAdults(1);
        requestDto.setMaxCount(5);
        FlightAvailabilityRequest.TripDetailsDto trip = new FlightAvailabilityRequest.TripDetailsDto();
        trip.setId("1");
        trip.setFrom("DEL");
        trip.setTo("BOM");
        trip.setDepartureDate(LocalDate.of(2025, 1, 1));
        trip.setDepartureTime(LocalTime.of(10, 0));
        requestDto.setTripDetails(List.of(trip));

        when(objectMapper.writeValueAsString(any())).thenReturn("{\"mocked\":\"body\"}");
        when(flightOffersSearch.post(anyString())).thenReturn(null);

        // Act
        FlightOfferSearch[] result = flightSearchService.searchMultiCityFlightOffers(requestDto);

        // Assert
        assertNull(result, "Result should be null if Amadeus returns null");

        // Verify mocks
        verify(objectMapper, times(1)).writeValueAsString(any());
        verify(flightOffersSearch, times(1)).post(anyString());
    }
}
*/
