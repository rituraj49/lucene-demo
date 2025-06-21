package cl.lcd.config;

import com.amadeus.Amadeus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class AmadeusConfigTest {
    @Value("${amadeus.api.key}")
    private String apiKey;

    @Value("${amadeus.api.secret}")
    private String apiSecret;

    @Value("${amadeus.api.host}")
    private String apiHost;

    @Bean(name = "testAmadeusClient")
    public Amadeus amadeusClient() {
        System.setProperty("AMADEUS_HOST", "http://localhost:9999");
        return Amadeus
                .builder(apiKey, apiSecret)
//                .setHost("http://localhost:9999")
                .build();
    }
}
