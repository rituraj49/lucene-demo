package cl.lcd.dto.search;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class FlightAvailabilityRequest { // FlightAvailability
    private String currencyCode; // TODO enum
    private List<TripDetailsDto> tripDetails;
//    private List<TravelerInfoDto> travelers;
    private int adults;
    private int children; // < 12 yr
    private int infants; // < 2 yr
//    private boolean isOneWay;
    private int maxCount;
    private Cabin cabin;
    private boolean checkedBags; // TODO: this is not used in the mapper, but it is in the API
    private boolean refundableFare;

    @Data
    public static class TripDetailsDto {
        private String id;
        private String from; // IATA code
        private String to; // IATA code
        private LocalDate departureDate;
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
