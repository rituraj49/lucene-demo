package cl.lcd.service;

import cl.lcd.dto.ActivityResponse;
import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.Activity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ActivityService {
    @Autowired
    private Amadeus amadeusClient;

    @Cacheable("activities")
    public List<ActivityResponse> getActivities(double latitude, double longitude, Integer radius) throws ResponseException {
        System.out.println("cache missed - fetching from Amadeus API");
        Params params = Params.with("latitude", String.valueOf(latitude))
                .and("longitude", String.valueOf(longitude));

        if (radius != null) {
            params.and("radius", radius.toString());
        }

        ActivityResponse mockActivity = new ActivityResponse();
        mockActivity.setId("ACT123");
        mockActivity.setType("TOUR");
        mockActivity.setName("Guided City Tour");
        mockActivity.setShortDescription("Explore the city highlights in 3 hours");
        mockActivity.setDescription("A guided tour covering the historical landmarks, local culture, and food experiences.");
        mockActivity.setGeoCode(new ActivityResponse.GeoCode("12.9716", "77.5946")); // Bangalore coords
        mockActivity.setRating("4.7");
        mockActivity.setPictures(List.of(
                "https://example.com/pictures/tour1.jpg",
                "https://example.com/pictures/tour2.jpg"
        ));
        mockActivity.setBookingLink("https://example.com/booking/ACT123");
        mockActivity.setPrice(new ActivityResponse.Price("INR", "1499.00"));
        return List.of(mockActivity);
//        Activity[] activities = amadeusClient.shopping.activities.get(params);
//
//        return Arrays.stream(activities).map(activity -> {
//            ActivityResponse dto = new ActivityResponse();
//            dto.setId(activity.getId());
//            dto.setType(activity.getType());
//            dto.setName(activity.getName());
//            dto.setDescription(activity.getDescription());
//            dto.setShortDescription(activity.getShortDescription());
//            if (activity.getGeoCode() != null) {
//                dto.setGeoCode(new ActivityResponse.GeoCode(
//                        String.valueOf(activity.getGeoCode().getLatitude()),
//                        String.valueOf(activity.getGeoCode().getLongitude())
//                ));
//            }
//
//            // dto.setRating(activity.getRating());
//
//            if (activity.getPictures() != null) {
//                dto.setPictures(Arrays.asList(activity.getPictures()));
//            }
//
//            dto.setBookingLink(activity.getBookingLink());
//
//            if (activity.getPrice() != null) {
//                dto.setPrice(new ActivityResponse.Price(
//                        activity.getPrice().getCurrencyCode(),
//                        activity.getPrice().getAmount()
//                ));
//            }
//
//            return dto;
//        }).toList();
    }

    @Cacheable("activities")
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
