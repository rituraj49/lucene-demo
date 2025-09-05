package cl.lcd.dto.hotel;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter
public class HotelOfferResponse {

    private String type;
    private Hotel hotel;
    private boolean available;
    private List<Offer> offers;
    private String self;

    @Data
    public static class Hotel {
        private String type;
        private String hotelId;
        private String chainCode;
        private String dupeId;
        private String name;
        private String cityCode;
        private double latitude;
        private double longitude;
    }

    @Data
    public static class Offer {
        private String id;
        private String checkInDate;
        private String checkOutDate;
        private String rateCode;
        private RateFamilyEstimated rateFamilyEstimated;
        private Room room;
        private Guests guests;
        private Price price;
        private Policies policies;
        private String self;
    }

    @Data
    public static class RateFamilyEstimated {
        private String code;
        private String type;
    }

    @Data
    public static class Room {
        private String type;
        private TypeEstimated typeEstimated;
        private Description description;
    }

    @Data
    public static class TypeEstimated {
        private String category;
        private int beds;
        private String bedType;
    }

    @Data
    public static class Description {
        private String text;
        private String lang;
    }

    @Data
    public static class Guests {
        private int adults;
    }

    @Data
    public static class Price {
        private String currency;
        private String base;
        private String total;
        private Variations variations;
    }

    @Data
    public static class Variations {
        private Average average;
        private List<Change> changes;
    }

    @Data
    public static class Average {
        private String base;
    }

    @Data
    public static class Change {
        private String startDate;
        private String endDate;
        private String total;
    }

    @Data
    public static class Policies {
        private String paymentType;
        private CancellationPolicy cancellation;
    }

    @Data
    public static class CancellationPolicy {
        private Description description;
        private String type;
    }
}

