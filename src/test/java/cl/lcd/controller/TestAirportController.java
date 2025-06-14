package cl.lcd.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester.MockMvcRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import cl.lcd.model.Airport;
import cl.lcd.model.AirportResponse;
import cl.lcd.service.InMemoryLuceneService;

@WebMvcTest(AirportController.class)
@Import(TestAirportController.MockServiceConfig.class)
public class TestAirportController {
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	InMemoryLuceneService inMemoryLuceneService;
	
	@TestConfiguration
	static class MockServiceConfig {
		@Bean
		InMemoryLuceneService inMemoryLuceneService() {
			return Mockito.mock(InMemoryLuceneService.class);
		}
	}
	
	@Test
	void testSearchAirports() throws Exception {
		Airport parent = new Airport();
		parent.setCity_code("DEL");
		parent.setIata("DEL");
		
		Airport child = new Airport();
		child.setCity_code("DEL");
		child.setIata("IGI");
		
		List<Airport> searchResult = List.of(parent, child);
		
		AirportResponse response = new AirportResponse();
		response.setParent(parent);
		response.setGroupData(List.of(child));
		
		List<AirportResponse> groupedResult = List.of(response);
		
		Mockito.when(inMemoryLuceneService.search("del")).thenReturn(searchResult);
		Mockito.when(inMemoryLuceneService.getGroupedData(searchResult)).thenReturn(groupedResult);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/search")
				.param("q", "del")
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$[0].parent.iata").value("DEL"))
		.andExpect(jsonPath("$[0].groupData[0].iata").value("IGI"));
	}
}
