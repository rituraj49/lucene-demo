package cl.lcd.dto.search;

import cl.lcd.enums.TripType;
import com.amadeus.resources.FlightOfferSearch;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.google.gson.Gson;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class FlightAvailabilityResponse {
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

    private List<Fees> fees;

    @Schema(example = "1")
    private int totalTravelers;

    private List<Trip> trips;

    @Schema(description = "flight offer search json object as is")
    private String pricingAdditionalInfo;

    @JsonIgnore
    public void setPricingAdditionalInfo(Object pricingAdditionalInfo) {
        if (pricingAdditionalInfo == null) {
            this.pricingAdditionalInfo = null;
        } else if (pricingAdditionalInfo instanceof String s) {
            this.pricingAdditionalInfo = s;
        } else if (pricingAdditionalInfo instanceof FlightOfferSearch offer) {
            this.pricingAdditionalInfo = new Gson().toJson(offer);
        } else {
            throw new IllegalArgumentException("Unsupported type for pricingAdditionalInfo: " + pricingAdditionalInfo);
        }
        //this.pricingAdditionalInfo = new Gson().toJson(pricingAdditionalInfo);
    }


    /*//@JsonIgnore
    @JsonSetter("pricingAdditionalInfo")
    public void setPricingAdditionalInfo(String pricingAdditionalInfo) {
        this.pricingAdditionalInfo = pricingAdditionalInfo;
    }*/


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Fees {
        private String amount;

        private String type;
    }

    @Data
    public static class Trip {

        @Schema(example = "1")
        private int tripNo;

        @Schema(example = "ONE_WAY")
        private TripType tripType;

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
        @Schema(example = "1")
        private String legNo;

        @Schema(example = "AI203")
        private String flightNumber;

        @Schema(example = "AI")
        private String carrierCode;

        @Schema(example = "Air India")
        private String carrierName;

        @Schema(example = "AI")
        private String operatingCarrierCode;

        @Schema(example = "Air India")
        private String operatingCarrierName;

        @Schema(example = "788")
        private String aircraftCode;

        @Schema(example = "DEL")
        private String departureAirport;

        @Schema(example = "T3")
        private String departureTerminal;

        @Schema(example = "2025-07-10T06:45:00")
        private String departureDateTime;

        @Schema(example = "BOM")
        private String arrivalAirport;

        @Schema(example = "T2")
        private String arrivalTerminal;

        @Schema(example = "2025-07-10T09:00:00")
        private String arrivalDateTime;

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
