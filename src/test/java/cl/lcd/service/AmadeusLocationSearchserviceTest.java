package cl.lcd.service;

import cl.lcd.service.locations.AmadeusLocationSearchService;
import com.amadeus.Amadeus;
import com.amadeus.Location;
import com.amadeus.ReferenceData;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.referencedata.Locations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AmadeusLocationSearchserviceTest {
    @Mock
    private Amadeus amadeusClient;

//    @InjectMocks
    private AmadeusLocationSearchService amadeusLocationSearchService;

    @Mock
    private ReferenceData referenceData;

    @Mock
    private Locations locations;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        when(amadeusClient.referenceData).thenReturn(referenceData);
        when(referenceData.locations).thenReturn(locations);
    }
//    @Test
//    void testAmadeusLocationSearch() throws ResponseException {
//        Location location = mock(Location.class);
//        when(locations.get(any())).thenReturn(new Location[]{location});
//    }

}
