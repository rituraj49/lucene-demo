package cl.lcd.service;

import cl.lcd.service.locations.AmadeusLocationSearchService;
import com.amadeus.Amadeus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AmadeusLocationSearchserviceTest {
    @Mock
    private Amadeus amadeusClient;

    @InjectMocks
    private AmadeusLocationSearchService amadeusLocationSearchService;

    @Test
    void testAmadeusLocationSearch() {
        when()
    }

}
