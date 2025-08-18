package cl.lcd.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleAirportWrapper {
    List<LocationResponse.SimpleAirport> simpleAirports;
}
