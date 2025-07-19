package cl.lcd.model;

import cl.lcd.enums.LocationType;
import cl.lcd.enums.LocationTypeConverter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Airport {
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

//     private GeoPoint location;

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
//
//     @PostConstruct
//     public void  initLocation() {
//            this.location = new GeoPoint(latitude, longitude);
//     }
}

