package cl.lcd.dto.hotel;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelSearchResponse {


    private String chainCode;
    private String iataCode;
    //private Long dupeId;
    private String name;
    private String hotelId;
    private GeoCode geoCode;
    private Address address;
    private Distance distance;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GeoCode {
        private float latitude;
        private float longitude;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Address {
        private String countryCode;
        private String postalCode;
        private String cityName;
        private List<String> lines;
    }

    @Data
    @NoArgsConstructor
    //@AllArgsConstructor
    public static class Distance {

        private String unit;
        private double value;
        public Distance(String unit,double value ) {
        }
    }

}
