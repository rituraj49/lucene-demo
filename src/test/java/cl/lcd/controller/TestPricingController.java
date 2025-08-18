package cl.lcd.controller;

import cl.lcd.dto.pricing.FlightPricingConfirmRequest;
import cl.lcd.dto.pricing.FlightPricingConfirmResponse;
import cl.lcd.service.flights.AmadeusPricingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PricingController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TestPricingController {
	
	@Autowired
	MockMvc mockMvc;

	@MockBean
	AmadeusPricingService amadeusPricingService;

	@Autowired
	ObjectMapper objectMapper;

	@Test
	void testConfirmPricing() throws Exception {
		FlightPricingConfirmRequest mockRequest = new FlightPricingConfirmRequest();
		FlightPricingConfirmResponse mockResponse = new FlightPricingConfirmResponse();

		Mockito.when(amadeusPricingService.searchFlightOffersPrice(Mockito.any()))
				.thenReturn(mockResponse);

		mockMvc.perform(MockMvcRequestBuilders.post("/pricing/flights/confirm")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(mockRequest))
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(mockResponse)));
	}
}
