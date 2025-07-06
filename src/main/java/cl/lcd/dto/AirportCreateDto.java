package cl.lcd.dto;

import lombok.Data;

@Data
public class AirportCreateDto {
    private String iata;

    private String icao;

    private String name;

    private Double latitude;

    private Double longitude;

    private int elevation;

    private String url;

    private String time_zone;

    private String city_code;

    private String country_code;

    private String city;

    private String state;

    private String county;

    private String type;
}
