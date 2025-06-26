//package cl.lcd.service;
//
//import cl.lcd.model.Airport;
//import com.amadeus.Amadeus;
//import com.amadeus.ReferenceData;
//import com.amadeus.exceptions.ResponseException;
//import com.amadeus.referencedata.Locations;
////import com.amadeus.resources.Address;
////import com.amadeus.resources.Location;
//import com.amadeus.resources.Location;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Answers;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//import java.lang.reflect.Field;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@ExtendWith(MockitoExtension.class)
//public class AmadeusServiceTest {
//
//    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
//    private Amadeus amadeusClient;
//
////    @Mock
////    private ReferenceData referenceData;
////
////    @Mock
////    private Locations locations;
//
//    private AmadeusLocationSearchService amadeusService;
//
//    @BeforeEach
//    public void setup() {
//         amadeusService = new AmadeusLocationSearchService();
//
////        Mockito.when(amadeusClient.referenceData).thenReturn(referenceData);
////        Mockito.when(referenceData.locations).thenReturn(locations);
//    }
//
//    @Test
//    public void testSearchLocationsService() throws ResponseException {
//        Map<String, String> queryParams = new HashMap<>();
//        queryParams.put("keyword", "delhi");
//        queryParams.put("subType", "AIRPORT");
//
//        System.out.println("testing amadeus search");
//        System.out.println("ref data: " + amadeusClient);
//        Location location = Mockito.mock(Location.class);
////        location.setSubType("AIRPORT");
////        location.setIataCode("DEL");
////        location.setName("Indira Gandhi International Airport");
//
//        Location.GeoCode geoCode = Mockito.mock(Location.GeoCode.class);
//        Location.Address address = Mockito.mock(Location.Address.class);
//
//        Mockito.when(location.getSubType()).thenReturn("AIRPORT");
//        Mockito.when(location.getIataCode()).thenReturn("DEL");
//        Mockito.when(location.getName()).thenReturn("Indira Gandhi International Airport");
//
//        Mockito.when(geoCode.getLatitude()).thenReturn(28.5562);
//        Mockito.when(geoCode.getLongitude()).thenReturn(77.1000);
//        Mockito.when(location.getGeoCode()).thenReturn(geoCode);
//
//        Mockito.when(address.getCityCode()).thenReturn("DEL");
//        Mockito.when(address.getCityName()).thenReturn("New Delhi");
//        Mockito.when(address.getCountryCode()).thenReturn("IN");
//        Mockito.when(location.getAddress()).thenReturn(address);
//
////        try {
////            Mockito.when(locations.get(Mockito.any())).thenReturn(new Location[]{location});
////        } catch (ResponseException e) {
////            throw new RuntimeException(e);
////        }
//
//        List<Airport> result = null;
//        try {
//            Mockito.when(amadeusClient.referenceData.locations.get(Mockito.any())).thenReturn(new Location[]{location});
//            result = amadeusService.searchLocations(queryParams);
//        } catch (ResponseException e) {
//            throw new RuntimeException(e);
//        }
//
//        assertEquals(1, result.size());
//        Airport airport = result.get(0);
//        assertEquals("DEL", airport.getIata());
//        assertEquals("Indira Gandhi International Airport", airport.getName());
//        assertEquals("New Delhi", airport.getCity());
//    }
//}
