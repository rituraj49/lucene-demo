package cl.lcd.dto.search;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class FlightAvailabilityRequest {
    @Schema(example = "INR")
    private String currencyCode; // TODO enum

    private List<TripDetailsDto> tripDetails;

    @Schema(example = "1")
    private int adults;

    @Schema(example = "1")
    private int children; // < 12 yr

    @Schema(example = "1")
    private int infants; // < 2 yr
//    private boolean isOneWay;

    @Schema(example = "1")
    private int maxCount;

    @Schema(example = "ECONOMY")
    private Cabin cabin;
//    private boolean checkedBags; // TODO: this is not used in the mapper, but it is in the API
//    private boolean refundableFare;

    @Data
    public static class TripDetailsDto {

        @Schema(example = "1")
        private String id;

        @Schema(example = "DEL")
        private String from; // IATA code

        @Schema(example = "DEL")
        private String to; // IATA code

        @Schema(example = "2025-01-01")
        private LocalDate departureDate;

        @Schema(example = "10:00:00")
        private LocalTime departureTime;
    }

//    @Data
//    public static class TravelerInfoDto {
//        private String id;
//        private TravelerType travelerType;
//        private String associateAdultId; // required if travelerType is HELD_INFANT
//    }

    public static enum TravelerType {
        ADULT,
        CHILD, // < 12 yr
        SENIOR,
        YOUNG,
        HELD_INFANT, // < 2 yr
        SEATED_INFANT, // < 2yr
        STUDENT
    }

    public static enum Cabin {
        ECONOMY,
        PREMIUM_ECONOMY,
        BUSINESS,
        FIRST
    }
}
