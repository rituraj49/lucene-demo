package cl.lcd.service;


import cl.lcd.config.AmadeusConfigTest;
import cl.lcd.model.LocationResponseWrapper;
import cl.lcd.service.locations.AmadeusLocationSearchService;
import com.amadeus.exceptions.ResponseException;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

//@Import(AmadeusConfigTest.class)
@WireMockTest(httpPort = 9999)
@SpringBootTest(classes = {AmadeusConfigTest.class, AmadeusLocationSearchService.class})
public class AmadeusLocationSearchServiceTest3 {

    @Autowired
    private AmadeusLocationSearchService amadeusLocationSeArchService;

    @BeforeEach
    public void setup() {
        stubFor(post(urlEqualTo("/v1/security/oauth2/token"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {
                                    "access_token":"test_access_token",
                                    "expires_in":3600,
                                    "token_type":"Bearer",
                                    "type":"amadeusOAuth2Token",
                                }
                                """)));
    }

    @Test
    void tokenStubCheck() throws IOException, InterruptedException {
        HttpClient http = HttpClient.newHttpClient();
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:9999/v1/security/oauth2/token"))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
        System.out.println("res :" + res.statusCode());
        Assertions.assertEquals(200, res.statusCode());
    }

    @Test
    void testSearch() throws ResponseException {
        amadeusLocationSeArchService.searchLocations(Map.of("subType", "CITY,AIRPORT", "keyword", "delhi"));

        // Print requests seen by WireMock
        WireMock.findAll(RequestPatternBuilder.allRequests()).forEach(System.out::println);
    }

    @Test
    void testSearchLocations() {

        stubFor(get(urlPathEqualTo("/v1/reference-data/locations"))
                .withQueryParam("keyword", equalTo("delhi"))
                .withQueryParam("subType", equalTo("CITY,AIRPORT"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("locations-response.json")));

        try {
            LocationResponseWrapper result = amadeusLocationSeArchService
                    .searchLocations(Map.of("subType", "CITY,AIRPORT", "keyword", "delhi"));
            assertEquals(1, result.getLocationResponses().size());
            assertEquals("DEL", result.getLocationResponses().get(0).getCityCode());
//            assertEquals("DEL", result.get(0).getGroupData().get(0).getIata());
        } catch (ResponseException e) {
//            throw new RuntimeException(e);
            System.out.println("ResponseException occurred: " + e.getMessage());
            e.printStackTrace();
        }

//        WireMock.findAll(RequestPatternBuilder.allRequests()).forEach(System.out::println);
//        verify(postRequestedFor(urlEqualTo("/v1/security/oauth2/token")));
//        verify(getRequestedFor(urlPathEqualTo("/v1/reference-data/locations"))
//                .withQueryParam("subType", equalTo("CITY,AIRPORT"))
//                .withQueryParam("keyword", equalTo("Santiago")));
    }
}