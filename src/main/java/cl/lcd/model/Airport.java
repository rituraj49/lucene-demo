package cl.lcd.model;

import cl.lcd.enums.LocationType;
import cl.lcd.enums.LocationTypeConverter;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Airport {
//     @CsvCustomBindByName(column = "location_type", converter = LocationTypeConverter.class)
	 private LocationType subType;

//     @CsvBindByName( column = "iata_code")
     private String iata;

//     @CsvBindByName(column = "name")
     private String name;

//     @CsvBindByName(column = "latitude")
     private double latitude;

//     @CsvBindByName(column = "longitude")
     private double longitude;

     @CsvBindByName(column = "time_zone_offset")
     private String timeZoneOffset;

     @CsvBindByName(column = "city_code")
     private String cityCode;

     @CsvBindByName(column = "country_code")
     private String countryCode;

     @CsvBindByName(column = "city")
     private String city;
}

