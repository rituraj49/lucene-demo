package cl.lcd.controller;

import cl.lcd.dto.booking.FlightBookingRequest;
import cl.lcd.dto.booking.FlightBookingResponse;
import cl.lcd.dto.booking.TravelerRequestDto;
import cl.lcd.model.Airport;
import cl.lcd.model.AirportResponse;
import cl.lcd.service.AmadeusBookingService;
import cl.lcd.service.AmadeusLocationSearchService;
import cl.lcd.service.InMemoryLuceneService;
import cl.lcd.service.UserLogService;
import cl.lcd.util.HelperUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TestBookingController {
	
	@Autowired
	MockMvc mockMvc;

	@MockBean
	AmadeusBookingService amadeusBookingService;

	@MockBean
	private UserLogService userLogService;

	@Autowired
	ObjectMapper objectMapper;


	@Test
	void testBookingFlight() throws Exception {
		TravelerRequestDto mockTravelerRequestDto = new TravelerRequestDto();
		mockTravelerRequestDto.setId("1");
		mockTravelerRequestDto.setFirstName("John");
		mockTravelerRequestDto.setLastName("Doe");

		FlightBookingRequest mockRequest = new FlightBookingRequest();
		mockRequest.setFlightOffer( "{\"type\":\"flight-offer\",\"id\":\"1\"");
		mockRequest.setTravelers(List.of(mockTravelerRequestDto));

		FlightBookingResponse mockResponse = new FlightBookingResponse();
		mockResponse.setOrderId("ORDER123");

		Mockito.when(amadeusBookingService.createFlightOrder(Mockito.any())).thenReturn(mockResponse);

		mockMvc.perform(MockMvcRequestBuilders.post("/booking/flight-order")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(mockRequest))
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.orderId").value("ORDER123"));
	}

	@Test
	void testFetchFlightOrder() throws Exception {
		String orderId = "ORDER123";
		FlightBookingResponse mockResponse = new FlightBookingResponse();
		mockResponse.setOrderId(orderId);

		Mockito.when(amadeusBookingService.getFlightOrder(orderId)).thenReturn(mockResponse);

		mockMvc.perform(MockMvcRequestBuilders.get("/booking/flight-order/{orderId}", orderId)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.orderId").value(orderId));
	}

	@Test
	void testDeleteFlightOrder() throws Exception {
		String orderId = "ORDER123";

		FlightBookingResponse mockOrder = new FlightBookingResponse();
		mockOrder.setOrderId(orderId);

		Mockito.when(amadeusBookingService.getFlightOrder(orderId)).thenReturn(mockOrder);

//		Mockito.when(amadeusBookingService.cancelFlightOrder(orderId)).thenReturn(null);

		com.amadeus.Response mockResponse = Mockito.mock(com.amadeus.Response.class);
		Mockito.when(mockResponse.getStatusCode()).thenReturn(204);
		Mockito.when(amadeusBookingService.cancelFlightOrder(orderId)).thenReturn(mockResponse);

		mockMvc.perform(MockMvcRequestBuilders.delete("/booking/flight-order/{orderId}", orderId)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}
}
