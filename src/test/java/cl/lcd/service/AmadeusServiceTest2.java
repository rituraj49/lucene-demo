//package cl.lcd.service;
//
//
//import cl.lcd.model.Airport;
//import com.amadeus.Amadeus;
//import com.amadeus.ReferenceData;
//import com.amadeus.exceptions.ResponseException;
//import com.amadeus.referencedata.Locations;
//import com.amadeus.resources.Location;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//
//import java.lang.reflect.Field;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//public class AmadeusServiceTest2 {
//    private AmadeusService amadeusService;
//    private Amadeus amadeusClient;
//    private ReferenceData referenceData;
//    private Locations locations;
//
//    @BeforeEach
//    public void setup() throws Exception {
//        amadeusService = new AmadeusService();
//
//        amadeusClient = mock(Amadeus.class);
//        referenceData = mock(ReferenceData.class);
//        locations = mock(Locations.class);
//
//        Field field = AmadeusService.class.getDeclaredField("amadeusClient");
//        field.setAccessible(true);
//        field.set(amadeusService, amadeusClient);
//
//        amadeusClient.referenceData = referenceData;
//        referenceData.locations = locations;
//    }
//
//    @Test
//    public void testSearchLocationsService() throws Exception {
//        System.out.println("Testing Amadeus search");
//        Map<String, String> queryParams = new HashMap<>();
//        queryParams.put("keyword", "delhi");
//        queryParams.put("subType", "AIRPORT");
//
//        Location location = Mockito.mock(Location.class);
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
