package cl.lcd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityResponse {
    private String id;
    private String type;
    private String name;
    private String shortDescription;
    private String description;
    private GeoCode geoCode;
    private String rating;
    private List<String> pictures;
    private String bookingLink;
    private Price price;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GeoCode {
        private String latitude;
        private String longitude;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Price {
        private String currencyCode;
        private String amount;
    }
}
