package cl.lcd.dto.booking;

import lombok.Data;

import java.util.List;

@Data
public class FlightBookingRequest {
//    private FlightCreateOrderDto data;
    private String flightOffer;
    private List<TravelerRequestDto> travelers;
}
