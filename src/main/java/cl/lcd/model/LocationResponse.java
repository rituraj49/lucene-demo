package cl.lcd.model;

import cl.lcd.enums.LocationType;
import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationResponse {
    private LocationType subType;
    private String iata;
    private String name;
    private double latitude;
    private double longitude;
    private String timeZoneOffset;
    private String cityCode;
    private String countryCode;
    private String city;
    private List<Airport> groupData;
}