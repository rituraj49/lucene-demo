package cl.lcd.service;

import cl.lcd.dto.booking.FlightBookingRequest;
import cl.lcd.dto.booking.FlightBookingResponse;
import cl.lcd.dto.booking.TravelerRequestDto;
import cl.lcd.mappers.booking.FlightBookingResponseMapper;
import com.amadeus.Amadeus;
import com.amadeus.Booking;
import com.amadeus.booking.FlightOrders;
import com.amadeus.resources.FlightOfferSearch;
import com.amadeus.resources.FlightOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class TestAmadeusBookingService {
    @InjectMocks
    AmadeusBookingService amadeusBookingService;

    @Mock
    Amadeus amadeusClient;

    @Mock
    Booking booking;

    @Mock
    FlightOrders flightOrders;

    @Mock
    com.amadeus.booking.FlightOrder flightOrder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        booking.flightOrders = flightOrders;

        booking.flightOrder = flightOrder;

        amadeusClient.booking = booking;

        ReflectionTestUtils.setField(amadeusBookingService, "amadeusClient", amadeusClient);
    }

    @Test
    void testCreateFlightOrder() throws Exception {
        FlightBookingRequest request = mock(FlightBookingRequest.class);
        FlightOfferSearch offer = mock(FlightOfferSearch.class);

        FlightOrder.Traveler traveler1 = new FlightOrder.Traveler();
        traveler1.setId("1");
        traveler1.setDateOfBirth("1990-01-01");
        traveler1.setGender("MALE");

        FlightOrder.Traveler traveler2 = new FlightOrder.Traveler();
        traveler2.setId("2");
        traveler2.setDateOfBirth("1992-02-02");
        traveler2.setGender("FEMALE");
        FlightOrder.Traveler[] travelers = {traveler1, traveler2};
        FlightOrder order = mock(FlightOrder.class);

        List<TravelerRequestDto> travelerRequestDtos = List.of(
                new TravelerRequestDto(),
                new TravelerRequestDto());

        try (MockedStatic<FlightBookingResponseMapper> mapper = mockStatic(FlightBookingResponseMapper.class)) {
            when(request.getFlightOffer()).thenReturn("{}");
            when(request.getTravelers()).thenReturn(travelerRequestDtos);
            mapper.when(() -> FlightBookingResponseMapper.createTravelersFromDto(travelerRequestDtos)).thenReturn(travelers);
            when(flightOrders.post(any(FlightOfferSearch.class), any(FlightOrder.Traveler[].class))).thenReturn(order);
            FlightBookingResponse response = new FlightBookingResponse();
            mapper.when(() -> FlightBookingResponseMapper.flightBookingResponse(order)).thenReturn(response);

            FlightBookingResponse result = amadeusBookingService.createFlightOrder(request);
            assertEquals(response, result);
        }
    }

    @Test
    void testGetFlightOrder() throws Exception {
        String orderId = "ORDER123";
        FlightOrder order = mock(FlightOrder.class);
        FlightBookingResponse response = mock(FlightBookingResponse.class);

        try (MockedStatic<FlightBookingResponseMapper> mapper = mockStatic(FlightBookingResponseMapper.class)) {
            when(booking.flightOrder(orderId)).thenReturn(flightOrder);
            when(flightOrder.get()).thenReturn(order);
            mapper.when(() -> FlightBookingResponseMapper.flightBookingResponse(order)).thenReturn(response);

            FlightBookingResponse result = amadeusBookingService.getFlightOrder(orderId);
            assertEquals(response, result);
        }
    }
}
