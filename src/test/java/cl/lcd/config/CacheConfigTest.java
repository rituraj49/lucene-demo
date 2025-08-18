package cl.lcd.config;

import cl.lcd.enums.LocationType;
import cl.lcd.model.LocationResponse;
import cl.lcd.model.LocationResponseWrapper;
import cl.lcd.model.SimpleAirportWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.AutoConfigureDataRedis;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureDataRedis
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CacheConfigTest {
    @Autowired
    private CacheManager cacheManager;

    private static final String CACHE_NAME = "locations";

    @BeforeEach
    void clearCache() {
        cacheManager.getCache(CACHE_NAME).clear();
    }

    @Test
    void testSerializationAndDeserializationWithWrapper() {
        LocationResponse.SimpleAirport simpleAirport = new LocationResponse.SimpleAirport(
                LocationType.AIRPORT, "JFK", "John F. Kennedy Intl", "New York", "NYC", "US"
        );

        List<LocationResponse.SimpleAirport> simpleAirportsList = new ArrayList<>();
        simpleAirportsList.add(simpleAirport);
        SimpleAirportWrapper airport = new SimpleAirportWrapper(simpleAirportsList);

        LocationResponse response = new LocationResponse();
        response.setSubType(LocationType.CITY);
        response.setIata("NYC");
        response.setName("New York Metropolitan Area");
        response.setLatitude(40.7128);
        response.setLongitude(-74.0060);
        response.setCity("New York");
        response.setTimeZoneOffset("America/New_York");
        response.setCityCode("NYC");
        response.setCountryCode("US");
        response.setGroupData(airport);

        LocationResponseWrapper wrapper = new LocationResponseWrapper(List.of(response));

        // Write to cache
        cacheManager.getCache(CACHE_NAME).put("testKey", wrapper);

        // Read from cache
        LocationResponseWrapper cached = Objects.requireNonNull(cacheManager.getCache(CACHE_NAME))
                .get("testKey", LocationResponseWrapper.class);

        assertNotNull(cached);
        assertEquals(1, cached.getLocationResponses().size());
        assertEquals("NYC", cached.getLocationResponses().get(0).getIata());
//        assertEquals("JFK", cached.getLocationResponses().get(0).getGroupData().get(0).getIata());
    }
}
