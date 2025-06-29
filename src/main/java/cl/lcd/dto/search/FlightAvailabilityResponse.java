package cl.lcd.dto.search;

import com.amadeus.resources.FlightOfferSearch;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.Gson;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class FlightAvailabilityResponse {
    private boolean oneWay;
    private int seatsAvailable;
    private String currencyCode;
    private String basePrice;
    private String totalPrice;

    private List<Trip> trips;
//    @JsonIgnore
    private String additionalInfo;

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = new Gson().toJson(additionalInfo);
    }

    @Data
    public static class Trip {
        private String from;
        private String to;
        private int stops;
        private String totalDuration;
        private String layoverTotalDuration;
        private List<Leg> legs;
    }

    @Data
    public static class Leg {
        private String legNo;
        private String flightNumber;
        private String operatingCarrierCode;
        private String aircraftCode;
        private String departureAirport;
        private String departureTerminal;
        private LocalDateTime departureDateTime;
        private String arrivalAirport;
        private String arrivalTerminal;
        private LocalDateTime arrivalDateTime;
        private String duration;
//        private int includedCheckedBags;
//        private BagWeight includedCabinBagsWeight;
        private String layoverAfter;
    }

    @Data
    public  static class BagWeight {
        private int weight;
        private String unit;
    }
}
