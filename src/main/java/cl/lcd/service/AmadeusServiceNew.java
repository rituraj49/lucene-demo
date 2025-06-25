package cl.lcd.service;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOfferSearch;
import com.amadeus.resources.FlightPrice;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AmadeusServiceNew {

        @Autowired
        private Amadeus amadeusClient;

//    @Value("${amadeus.api.key}")
//    private String clientId;
//
//    @Value("${amadeus.api.secret}")
//    private String clientSecret;
//
//
//    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
//
//
//    private Amadeus amadeus;//=Amadeus.builder(clientId, clientSecret).build();
//
//    @PostConstruct
//    public void init(){
//        amadeus=Amadeus.builder(clientId, clientSecret).build();
//    }


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

    /**
     * searches for flight offers based on the provided parameters.
     * @param paramsMap = Map<String, String>------------------***-
     * @return FlightOfferSearch[]
     * @throws ResponseException
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

        return amadeusClient.shopping.flightOffersSearch.get(params);
    }

//    public String getAccessToken() throws Exception {
//        String url = "https://test.api.amadeus.com/v1/security/oauth2/token";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        String body = "grant_type=client_credentials" +
//                "&client_id=" + clientId +
//                "&client_secret=" + clientSecret;
//
//        HttpEntity<String> request = new HttpEntity<>(body, headers);
//
//        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
//
//        if (response.getStatusCode() == HttpStatus.OK) {
//            return response.getBody().get("access_token").toString();
//        } else {
//            throw new RuntimeException("Failed to get access token");
//        }
//    }

    /**
     * Searches for multi-city flight offers based on the provided flight request.
     *
     * @param flightRequest A map containing the flight request details.
     * @return An array of FlightOfferSearch objects representing the flight offers.
     * @throws ResponseException If an error occurs while searching for flight offers.
     * @throws JsonProcessingException If an error occurs while processing  the json body.
     */
    public FlightOfferSearch[] searchMultiCityFlightOffers
            (Map<String, Object> flightRequest) throws ResponseException, JsonProcessingException {
        String body = objectMapper.writeValueAsString(flightRequest);

        FlightOfferSearch[] offers = amadeusClient.shopping.flightOffersSearch.post(body);

        System.out.println("Number of flight offers found: " + offers.length);
//        if (offers == null || offers.length == 0) {
//            throw new Exception("No flight offers found for the given request.");
//        }
        List<FlightOfferSearch> offerList = List.of(offers);

        offerList.forEach(o -> System.out.println("src: " + o.toString()));
//        System.out.println("Flight offers: " + offerList.toString());
//        return offerList;
//        String jsonOutput = objectMapper.writeValueAsString(offers);
//
//        JsonNode jsonObject = objectMapper.readTree(jsonOutput);
        return offers;
    }

/*
    public FlightPrice confirm(FlightOfferSearch offer) throws ResponseException {
        return amadeus.shopping.flightOffersSearch.pricing.post(offer);
    }
*/

// https://test.api.amadeus.com/v1/shopping/flight-offers/pricing


    /**
     * Searches for flight offers and returns the price for the given flight request.
     *
     * @param flightRequest An array of FlightOfferSearch objects representing the flight request.
     * @return A FlightPrice object containing the price details of the flight offers.
     * @throws ResponseException If an error occurs while searching for flight offers or pricing.
     */
    public FlightPrice searchFlightOffersPrice(FlightOfferSearch[] flightRequest) throws ResponseException {
        FlightPrice price = amadeusClient.shopping.flightOffersSearch.pricing.post(flightRequest);
        return price;
    }
}
