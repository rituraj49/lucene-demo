package cl.lcd.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Airport {
     private String iata;
     private String icao;
     private String name;
     private String latitude;
     private String longitude;
     private int elevation;
     private String url;
     private String time_zone;
     private String city_code;
     private String country_code;
     private String city;
     private String state;
     private String county;
     private String type;
}
