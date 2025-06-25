package cl.lcd.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Map;

import cl.lcd.service.AmadeusService;
import cl.lcd.util.HelperUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import cl.lcd.model.Airport;
import cl.lcd.model.AirportResponse;
import cl.lcd.service.InMemoryLuceneService;

@WebMvcTest(LocationController.class)
@Import(TestLocationController.MockServiceConfig.class)
public class TestLocationController {
	
	@Autowired
	MockMvc mockMvc;

	@Autowired
//	@InjectMocks
	InMemoryLuceneService inMemoryLuceneService;
//
//	@Autowired
//	@InjectMocks
	AmadeusService amadeusService;

	@Autowired
	HelperUtil helperUtil;
	
	@TestConfiguration
	static class MockServiceConfig {
		@Bean
		InMemoryLuceneService inMemoryLuceneService() {
			return Mockito.mock(InMemoryLuceneService.class);
		}

		@Bean
		AmadeusService amadeusService() {
			return Mockito.mock(AmadeusService.class);
		}

		@Bean
		HelperUtil helperService() {
			return Mockito.mock(HelperUtil.class);
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
		
		Mockito.when(inMemoryLuceneService.search("del")).thenReturn(groupedResult);
//		Mockito.when(helperUtil.getGroupedData(searchResult)).thenReturn(groupedResult);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/search")
				.param("q", "del")
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$[0].parent.iata").value("DEL"))
		.andExpect(jsonPath("$[0].groupData[0].iata").value("IGI"));
	}

	@Test
	void testSearchAirportsAmadeus() throws Exception {
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

		Mockito.when(amadeusService.searchLocations(Mockito.<Map<String, String>>any())).thenReturn(groupedResult);
//		Mockito.when(helperUtil.getGroupedData(searchResult)).thenReturn(groupedResult);

		mockMvc.perform(MockMvcRequestBuilders.get("/amadeus-search")
				.param("keyword", "del")
				.param("subType", "AIRPORT")
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$[0].parent.iata").value("DEL"))
		.andExpect(jsonPath("$[0].groupData[0].iata").value("IGI"));

	}
}
