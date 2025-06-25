package cl.lcd.model;

import cl.lcd.enums.LocationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Airport {
	 private LocationType subType;
     private String iata;
     private String name;
     private double latitude;
     private double longitude;
     private String time_zone_offset;
     private String city_code;
     private String country_code;
     private String city;
}

