package cl.lcd.dto.search;
import lombok.Data;


@Data
public class FlightDateResponse {
    private String origin;
    private String destination;
    private String departureDate;
    private String returnDate;
    private String totalPrice;

}

