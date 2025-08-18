package cl.lcd.model;

import cl.lcd.dto.search.FlightAvailabilityResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightResponseWrapper {
    List<FlightAvailabilityResponse> flightsAvailable;
}
