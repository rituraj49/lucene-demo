package cl.lcd.dto.search;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class FlightAvailabilityResponse {
    private boolean oneWay;
    private int seatsAvailable;
//    private int stops;
//    private Duration layoverDuration;
    private FlightAvailabilityRequest.Cabin cabin;
    private String currencyCode;
    private String basePrice;
    private String totalPrice;

    private List<Trip> trips;

    @Data
    public static class Trip {
        private String from;
        private String to;
        private int stops;
        private Duration totalDuration;
        private Duration layoverTotalDuration;
        private List<Leg> legs;
    }

    public static class Duration {
        private String hh;
        private String mm;
    }

    @Data
    public static class Leg {
        private String flightNumber;
        private String operatingCarrierCode;
        private String aircraftCode;
        private String departureAirport;
        private String departureTerminal;
        private Date departureDateTime;
        private String arrivalAirport;
        private String arrivalTerminal;
        private Date arrivalDateTime;
        private FlightAvailabilityRequest.Cabin cabin;
        private Duration duration;
        private int includedCheckedBags;
        private BagWeight includedCabinBagsWeight;
        private Amenities amenities;
    }

    public static class Amenities {

    }

    @Data
    public  static class BagWeight {
        private int weight;
        private String unit;
    }
}
