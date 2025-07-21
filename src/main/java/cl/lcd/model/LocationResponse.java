package cl.lcd.model;

import cl.lcd.enums.LocationType;
import cl.lcd.enums.LocationTypeConverter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationResponse {
//    private LocationType subType;
//    private String iata;
//    private String name;
//    private double latitude;
//    private double longitude;
//    private String timeZoneOffset;
//    private String cityCode;
//    private String countryCode;
//    private String city;
//    private List<Airport> groupData;

    @CsvCustomBindByName(column = "type", converter = LocationTypeConverter.class)
    private LocationType subType;

    @CsvBindByName( column = "iata")
    private String iata;

    @CsvBindByName(column = "name")
    private String name;

    @CsvBindByName(column = "latitude")
    private Double latitude;

    @CsvBindByName(column = "longitude")
    private Double longitude;

    @CsvBindByName(column = "time_zone")
    @JsonProperty("time_zone")
    private String timeZoneOffset;

    @CsvBindByName(column = "city_code")
    @JsonProperty("city_code")
    private String cityCode;

    @CsvBindByName(column = "country_code")
    @JsonProperty("country_code")
    private String countryCode;

    @CsvBindByName(column = "city")
    private String city;

    @JsonProperty("group_data")
    private List<SimpleAirport> groupData;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SimpleAirport {
        private LocationType subType;
        private String iata;
        private String name;
        private String city;
        private String cityCode;
    }
}