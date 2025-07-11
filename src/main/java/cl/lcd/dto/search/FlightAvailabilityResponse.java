package cl.lcd.dto.search;

import com.amadeus.resources.FlightOfferSearch;
import com.google.gson.Gson;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class FlightAvailabilityResponse {
//    private boolean oneWay;
//    private int seatsAvailable;
//    private String currencyCode;
//    private String basePrice;
//    private String totalPrice;
    @Schema(example = "false")
    private boolean oneWay;

    @Schema(example = "3")
    private int seatsAvailable;

    @Schema(example = "INR")
    private String currencyCode;

    @Schema(example = "5000.00")
    private String basePrice;

    @Schema(example = "5750.00")
    private String totalPrice;

    @Schema(example = "1")
    private int totalTravelers;

    private List<Trip> trips;
//    @JsonIgnore
    @Schema(description = "flight offer search json object as is")
    private String pricingAdditionalInfo;

    public void setPricingAdditionalInfo(FlightOfferSearch pricingAdditionalInfo) {
        this.pricingAdditionalInfo = new Gson().toJson(pricingAdditionalInfo);
    }

    @Data
    public static class Trip {
//        private String from;
//        private String to;
//        private int stops;
//        private String totalFlightDuration;
//        private String totalLayoverDuration;
        @Schema(example = "DEL")
        private String from;

        @Schema(example = "BOM")
        private String to;

        @Schema(example = "1")
        private int stops;

        @Schema(example = "10h 3m")
        private String totalFlightDuration;

        @Schema(example = "2h 15m")
        private String totalLayoverDuration;
        private List<Leg> legs;
    }

    @Data
    public static class Leg {
//        private String legNo;
//        private String flightNumber;
//        private String operatingCarrierCode;
//        private String aircraftCode;
//        private String departureAirport;
//        private String departureTerminal;
//        private LocalDateTime departureDateTime;
//        private String arrivalAirport;
//        private String arrivalTerminal;
//        private LocalDateTime arrivalDateTime;
//        private String duration;
        @Schema(example = "1")
        private String legNo;

        @Schema(example = "AI203")
        private String flightNumber;

        @Schema(example = "AI")
        private String operatingCarrierCode;

        @Schema(example = "788")
        private String aircraftCode;

        @Schema(example = "DEL")
        private String departureAirport;

        @Schema(example = "T3")
        private String departureTerminal;

        @Schema(example = "2025-07-10T06:45:00")
        private LocalDateTime departureDateTime;

        @Schema(example = "BOM")
        private String arrivalAirport;

        @Schema(example = "T2")
        private String arrivalTerminal;

        @Schema(example = "2025-07-10T09:00:00")
        private LocalDateTime arrivalDateTime;

        @Schema(example = "2h 15m")
        private String duration;
//        private int includedCheckedBags;
//        private BagWeight includedCabinBagsWeight;
        @Schema(example = "10h 25m")
        private String layoverAfter;
    }

    @Data
    public  static class BagWeight {
        private int weight;
        private String unit;
    }
}
