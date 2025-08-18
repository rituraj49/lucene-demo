package cl.lcd.controller;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Map;

import cl.lcd.model.LocationResponse;
import cl.lcd.model.LocationResponseWrapper;
import cl.lcd.model.SimpleAirportWrapper;
import cl.lcd.service.locations.AmadeusLocationSearchService;
import cl.lcd.service.locations.ElasticsearchService;
import cl.lcd.util.HelperUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import cl.lcd.model.Airport;
import cl.lcd.service.locations.InMemoryLuceneService;

@WebMvcTest(LocationSearchController.class)
//@Import(TestLocationSearchController.MockServiceConfig.class)
@AutoConfigureMockMvc(addFilters = false)
public class TestLocationSearchController {

	@Autowired
	MockMvc mockMvc;

//	@Autowired
//	@InjectMocks
	@MockBean
	InMemoryLuceneService inMemoryLuceneService;
//
//	@Autowired
//	@InjectMocks
	@MockBean
	AmadeusLocationSearchService amadeusLocationSeArchService;

	@MockBean
	ElasticsearchService elasticsearchService;

//	@Autowired
	@MockitoBean
	HelperUtil helperUtil;

//	@TestConfiguration
//	static class MockServiceConfig {
//		@Bean
//		InMemoryLuceneService inMemoryLuceneService() {
//			return Mockito.mock(InMemoryLuceneService.class);
//		}
//
//		@Bean
//		AmadeusLocationSearchService amadeusService() {
//			return Mockito.mock(AmadeusLocationSearchService.class);
//		}
//
//		@Bean
//		HelperUtil helperService() {
//			return Mockito.mock(HelperUtil.class);
//		}
//	}

	@Test
	void testSearchAirports() throws Exception {
		Airport parent = new Airport();
		parent.setCityCode("DEL");
		parent.setIata("DEL");

		LocationResponse.SimpleAirport child = new LocationResponse.SimpleAirport();
		child.setCityCode("DEL");
		child.setIata("IGI");

//		List<Airport> searchResult = List.of(parent, child);
		SimpleAirportWrapper simpleAirportWrapper = new SimpleAirportWrapper();
		simpleAirportWrapper.setSimpleAirports(List.of(child));
		LocationResponse response = new LocationResponse();
		response.setIata(parent.getIata());
		response.setGroupData(simpleAirportWrapper);

		List<LocationResponse> groupedResult = List.of(response);

		LocationResponseWrapper wrapper = new LocationResponseWrapper(groupedResult);

		Mockito.when(inMemoryLuceneService.search("del")).thenReturn(wrapper);
//		Mockito.when(helperUtil.getGroupedData(searchResult)).thenReturn(groupedResult);

		mockMvc.perform(MockMvcRequestBuilders.get("/locations/search")
				.param("keyword", "del")
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.locationResponses[0].iata").value("DEL"));
//		.andExpect(jsonPath("$[0].groupData[0].iata").value("IGI"));
	}

	@Test
	void testSearchAirportsAmadeus() throws Exception {
		Airport parent = new Airport();
		parent.setCityCode("DEL");
		parent.setIata("DEL");

		LocationResponse.SimpleAirport child = new LocationResponse.SimpleAirport();
		child.setCityCode("DEL");
		child.setIata("IGI");

		SimpleAirportWrapper simpleAirportWrapper = new SimpleAirportWrapper();
		simpleAirportWrapper.setSimpleAirports(List.of(child));
//		List<Airport> searchResult = List.of(parent, child);

		LocationResponse response = new LocationResponse();
		response.setIata(parent.getIata());
		response.setGroupData(simpleAirportWrapper);

		List<LocationResponse> groupedResult = List.of(response);

		LocationResponseWrapper wrapper = new LocationResponseWrapper(groupedResult);
		Mockito.when(amadeusLocationSeArchService.searchLocations(Mockito.<Map<String, String>>any())).thenReturn(wrapper);

		mockMvc.perform(MockMvcRequestBuilders.get("/locations/amadeus-search")
				.param("keyword", "del")
				.param("subType", "CITY,AIRPORT")
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$[0].iata").value("DEL"));
//		.andExpect(jsonPath("$[0].groupData[0].iata").value("IGI"));
	}
}
