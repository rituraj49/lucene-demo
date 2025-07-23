package cl.lcd.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CityGroup {
    @JsonProperty("city_code")
    private String cityCode;

    @JsonProperty("airport_group")
    private List<LocationResponse.SimpleAirport> airportGroup;
}
