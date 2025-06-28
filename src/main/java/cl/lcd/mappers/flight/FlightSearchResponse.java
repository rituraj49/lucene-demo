package cl.lcd.mappers.flight;

import cl.lcd.dto.search.FlightAvailabilityResponse;
import com.amadeus.resources.DatedFlight;
import com.amadeus.resources.FlightOfferSearch;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class FlightSearchResponse {
    public static FlightAvailabilityResponse createResponse(FlightOfferSearch offer) {
        if (offer == null) {
            return null;
        }

        FlightAvailabilityResponse response = new FlightAvailabilityResponse();

            List<FlightAvailabilityResponse.Trip> createdTrips = createTrips(offer.getItineraries());

            response.setOneWay(offer.isOneWay());
            response.setSeatsAvailable(offer.getNumberOfBookableSeats());
            response.setCurrencyCode(offer.getPrice().getCurrency());
            response.setBasePrice(offer.getPrice().getBase());
            response.setTotalPrice(offer.getPrice().getTotal());
            response.setTrips(createdTrips);
            response.setAdditionalInfo(offer.toString());
        return response;
    }

    private static List<FlightAvailabilityResponse.Trip> createTrips(FlightOfferSearch.Itinerary[] itineraries) {
        List<FlightAvailabilityResponse.Trip> trips = new ArrayList<>();
        for(FlightOfferSearch.Itinerary itinerary : itineraries) {
            FlightAvailabilityResponse.Trip trip = new FlightAvailabilityResponse.Trip();
            List<FlightAvailabilityResponse.Leg> legs = new ArrayList<>();
            Duration totalLayover = Duration.ZERO;

            FlightOfferSearch.SearchSegment[] segments = itinerary.getSegments();
            String from = segments[0].getDeparture().getIataCode();
            String to = segments[segments.length - 1].getArrival().getIataCode();
            int stops = segments.length - 1;

//            for(FlightOfferSearch.SearchSegment segment: itinerary.getSegments()) {
            for(int i = 0; i < segments.length; i++) {
                FlightOfferSearch.SearchSegment segment = segments[i];
                FlightAvailabilityResponse.Leg leg = new FlightAvailabilityResponse.Leg();

                leg.setLegNo(segment.getId());
                leg.setFlightNumber(segment.getNumber());
                leg.setOperatingCarrierCode(segment.getCarrierCode());
                leg.setAircraftCode(segment.getAircraft().getCode());
                leg.setDepartureAirport(segment.getDeparture().getIataCode());
                leg.setDepartureTerminal(segment.getDeparture().getTerminal());
                leg.setDepartureDateTime(LocalDateTime.parse(segment.getDeparture().getAt()));
                leg.setArrivalAirport(segment.getArrival().getIataCode());
                leg.setArrivalTerminal(segment.getArrival().getTerminal());
                leg.setArrivalDateTime(LocalDateTime.parse(segment.getArrival().getAt()));
                leg.setDuration(getDurationString(segment.getDuration()));

                if (i < segments.length - 1) {
                    LocalDateTime arrivalTime = LocalDateTime.parse(segment.getArrival().getAt());
                    LocalDateTime nextDepartureTime = LocalDateTime.parse(segments[i + 1].getDeparture().getAt());
                    Duration layover = Duration.between(arrivalTime, nextDepartureTime);
                    totalLayover = totalLayover.plus(layover);

                    leg.setLayoverAfter(getDurationString(layover.toString()));
                }
                legs.add(leg);
            }

            trip.setLegs(legs);
            trip.setFrom(from);
            trip.setTo(to);
            trip.setStops(stops);
            trip.setTotalDuration(getDurationString(itinerary.getDuration()));
            trip.setLayoverTotalDuration(getDurationString(totalLayover.toString()));

            trips.add(trip);
        }
        return trips;
    }

    public static String getDurationString(String duration) {
        if(duration == null || duration.isEmpty()) {
            return "0h 0m";
        }
        Duration dur = Duration.parse(duration);
        long hrs = dur.toHours();
        long min = dur.minusHours(hrs).toMinutes();
        return String.format("%dh %dm", hrs, min);
    }

//    public static
}
