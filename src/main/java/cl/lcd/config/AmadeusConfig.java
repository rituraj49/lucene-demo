package cl.lcd.config;

import com.amadeus.Amadeus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmadeusConfig {

	    @Value("${amadeus.api.key}")
	    private String apiKey;

	    @Value("${amadeus.api.secret}")
	    private String apiSecret;

	    @Bean
	    public Amadeus amadeusClient() {
	        return Amadeus
	                .builder(apiKey, apiSecret)
	                .build();
	    }
}
