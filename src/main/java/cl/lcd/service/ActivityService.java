package cl.lcd.service;

import cl.lcd.dto.ActivityResponse;
import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.Activity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ActivityService {
    @Autowired
    private Amadeus amadeusClient;

    public List<ActivityResponse> getActivities(double latitude, double longitude, Integer radius) throws ResponseException {
        Params params = Params.with("latitude", String.valueOf(latitude))
                .and("longitude", String.valueOf(longitude));

        if (radius != null) {
            params.and("radius", radius.toString());
        }

        Activity[] activities = amadeusClient.shopping.activities.get(params);

        return Arrays.stream(activities).map(activity -> {
            ActivityResponse dto = new ActivityResponse();
            dto.setId(activity.getId());
            dto.setType(activity.getType());
            dto.setName(activity.getName());
            dto.setDescription(activity.getDescription());
            dto.setShortDescription(activity.getShortDescription());
            if (activity.getGeoCode() != null) {
                dto.setGeoCode(new ActivityResponse.GeoCode(
                        String.valueOf(activity.getGeoCode().getLatitude()),
                        String.valueOf(activity.getGeoCode().getLongitude())
                ));
            }

            // dto.setRating(activity.getRating());

            if (activity.getPictures() != null) {
                dto.setPictures(Arrays.asList(activity.getPictures()));
            }

            dto.setBookingLink(activity.getBookingLink());

            if (activity.getPrice() != null) {
                dto.setPrice(new ActivityResponse.Price(
                        activity.getPrice().getCurrencyCode(),
                        activity.getPrice().getAmount()
                ));
            }

            return dto;
        }).toList();
    }

    public List<ActivityResponse> getActivitiesBySquare(
            double north,
            double west,
            double south,
            double east
    ) throws ResponseException {

        Params params = Params.with("north", String.valueOf(north))
                .and("west", String.valueOf(west))
                .and("south", String.valueOf(south))
                .and("east", String.valueOf(east));

        Activity[] activities = amadeusClient.shopping.activities.bySquare.get(params);

        return Arrays.stream(activities).map(activity -> {
            ActivityResponse dto = new ActivityResponse();
            dto.setId(activity.getId());
            dto.setType(activity.getType());
            dto.setName(activity.getName());
            dto.setDescription(activity.getDescription());
            dto.setShortDescription(activity.getShortDescription());

            if (activity.getGeoCode() != null) {
                dto.setGeoCode(new ActivityResponse.GeoCode(
                        String.valueOf(activity.getGeoCode().getLatitude()),
                        String.valueOf(activity.getGeoCode().getLongitude())
                ));
            }

            if (activity.getPictures() != null) {
                dto.setPictures(Arrays.asList(activity.getPictures()));
            }

            dto.setBookingLink(activity.getBookingLink());

            if (activity.getPrice() != null) {
                dto.setPrice(new ActivityResponse.Price(
                        activity.getPrice().getCurrencyCode(),
                        activity.getPrice().getAmount()
                ));
            }

            return dto;
        }).toList();
    }
}
