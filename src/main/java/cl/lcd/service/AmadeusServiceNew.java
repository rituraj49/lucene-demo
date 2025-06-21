package cl.lcd.service;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOfferSearch;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class AmadeusServiceNew {

  //  Amadeus amadeus=Amadeus.builder(System.getenv()).build();


    @Value("${amadeus.api.key}")
    private String clientId;

    @Value("${amadeus.api.secret}")
    private String clientSecret;


    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();


    private Amadeus amadeus;//=Amadeus.builder(clientId, clientSecret).build();

    @PostConstruct
    public void init(){
        amadeus=Amadeus.builder(clientId, clientSecret).build();
    }


/*
        public FlightOfferSearch[] flightOfferSearches() throws ResponseException{
            FlightOfferSearch[] offerSearches=amadeus.shopping.flightOffersSearch.get(Params.with("originLocationCode","SYD")
                    .and("destinationLocationCode","NYC")
                    .and("departureDate","2025-06-12").and("maxPrice",140000)
                    .and("adults",2).and("currencyCode","INR"));
            System.out.println(offerSearches.length);
            return offerSearches;
        }
*/


    public FlightOfferSearch[] flightOfferSearches(Map<String, String> paramsMap) throws ResponseException {
        Params params = null;

        for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
            if (params == null) {
                params = Params.with(entry.getKey(), entry.getValue());
            } else {
                params.and(entry.getKey(), entry.getValue());
            }
        }

        return amadeus.shopping.flightOffersSearch.get(params);
    }





    public String getAccessToken() throws Exception {
        String url = "https://test.api.amadeus.com/v1/security/oauth2/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = "grant_type=client_credentials" +
                "&client_id=" + clientId +
                "&client_secret=" + clientSecret;

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody().get("access_token").toString();
        } else {
            throw new RuntimeException("Failed to get access token");
        }
    }




    public String searchMultiCityFlightOffers(Map<String, Object> flightRequest) throws Exception {
        String url = "https://test.api.amadeus.com/v2/shopping/flight-offers";

       String token = getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(flightRequest), headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        return response.getBody();
    }





/*
    public FlightPrice confirm(FlightOfferSearch offer) throws ResponseException {
        return amadeus.shopping.flightOffersSearch.pricing.post(offer);
    }
*/

// https://test.api.amadeus.com/v1/shopping/flight-offers/pricing


    public String searchFlightOffersPrice(Map<String, Object> flightRequest) throws Exception {
        String url = "https://test.api.amadeus.com/v1/shopping/flight-offers/pricing";

        String token = getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(flightRequest), headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        return response.getBody();
    }












}
