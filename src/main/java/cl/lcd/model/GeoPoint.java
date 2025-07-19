package cl.lcd.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GeoPoint {
    private double lat;
    private double lon;
}
