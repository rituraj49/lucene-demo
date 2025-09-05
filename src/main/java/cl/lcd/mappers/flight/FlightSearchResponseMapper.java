package cl.lcd.mappers.flight;

import cl.lcd.dto.search.FlightAvailabilityResponse;
import cl.lcd.enums.TripType;
import com.amadeus.resources.FlightOfferSearch;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class FlightSearchResponseMapper {
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
            response.setTotalPrice(offer.getPrice().getGrandTotal());
            response.setTotalTravelers(offer.getTravelerPricings().length);
            response.setTrips(createdTrips);
            response.setPricingAdditionalInfo(offer);

            List<FlightAvailabilityResponse.Fees> feesList = Arrays.stream(offer.getPrice().getFees())
                    .map(f -> new FlightAvailabilityResponse.Fees(f.getAmount(), f.getType())).toList();
            response.setFees(feesList);

        return response;
    }

    public static FlightAvailabilityResponse createResponse(FlightOfferSearch offer, JsonObject dictionaries) {
        if (offer == null) {
            return null;
        }

        JsonObject carriers = dictionaries.getAsJsonObject("carriers");
        FlightAvailabilityResponse response = new FlightAvailabilityResponse();

        List<FlightAvailabilityResponse.Trip> createdTrips = createTrips(offer.getItineraries(), carriers);
        response.setOneWay(offer.isOneWay());
        response.setSeatsAvailable(offer.getNumberOfBookableSeats());
        response.setCurrencyCode(offer.getPrice().getCurrency());
        response.setBasePrice(offer.getPrice().getBase());
        response.setTotalPrice(offer.getPrice().getGrandTotal());
        response.setTotalTravelers(offer.getTravelerPricings().length);
        response.setTrips(createdTrips);
        response.setPricingAdditionalInfo(offer);

        List<FlightAvailabilityResponse.Fees> feesList = Arrays.stream(offer.getPrice().getFees())
                .map(f -> new FlightAvailabilityResponse.Fees(f.getAmount(), f.getType())).toList();
        response.setFees(feesList);

        return response;
    }

    private static List<FlightAvailabilityResponse.Trip> createTrips(FlightOfferSearch.Itinerary[] itineraries) {
        List<FlightAvailabilityResponse.Trip> trips = new ArrayList<>();
//        for(FlightOfferSearch.Itinerary itinerary : itineraries) {
        for(int i = 0; i < itineraries.length; i++) {
            FlightOfferSearch.Itinerary itinerary = itineraries[i];
            FlightAvailabilityResponse.Trip trip = new FlightAvailabilityResponse.Trip();
            List<FlightAvailabilityResponse.Leg> legs = new ArrayList<>();
            Duration totalLayover = Duration.ZERO;
            Duration totalDuration = Duration.ZERO;

            FlightOfferSearch.SearchSegment[] segments = itinerary.getSegments();
            String from = segments[0].getDeparture().getIataCode();
            String to = segments[segments.length - 1].getArrival().getIataCode();
            int stops = segments.length - 1;

//            for(FlightOfferSearch.SearchSegment segment: itinerary.getSegments()) {

            for(int j = 0; j < segments.length; j++) {
                FlightOfferSearch.SearchSegment segment = segments[j];
                FlightAvailabilityResponse.Leg leg = new FlightAvailabilityResponse.Leg();

                leg.setLegNo(segment.getId());
                leg.setFlightNumber(segment.getNumber());
                if(segment.getOperating() != null) {
                    leg.setOperatingCarrierCode(segment.getOperating().getCarrierCode());
                }
                leg.setCarrierCode(segment.getCarrierCode());
                leg.setAircraftCode(segment.getAircraft().getCode());
                leg.setDepartureAirport(segment.getDeparture().getIataCode());
                leg.setDepartureTerminal(segment.getDeparture().getTerminal());
                leg.setDepartureDateTime(segment.getDeparture().getAt());
                leg.setArrivalAirport(segment.getArrival().getIataCode());
                leg.setArrivalTerminal(segment.getArrival().getTerminal());
                leg.setArrivalDateTime(segment.getArrival().getAt());
                leg.setDuration(getDurationString(segment.getDuration()));
                totalDuration = totalDuration.plus(Duration.parse(segment.getDuration()));

                if (j < segments.length - 1) {
                    LocalDateTime arrivalTime = LocalDateTime.parse(segment.getArrival().getAt());
                    LocalDateTime nextDepartureTime = LocalDateTime.parse(segments[j + 1].getDeparture().getAt());
                    Duration layover = Duration.between(arrivalTime, nextDepartureTime);
                    totalLayover = totalLayover.plus(layover);
                    totalDuration = totalDuration.plus(layover);
                    leg.setLayoverAfter(getDurationString(layover.toString()));
                }
                legs.add(leg);
            }

            trip.setLegs(legs);
            trip.setFrom(from);
            trip.setTo(to);
            trip.setStops(stops);
//            trip.setTotalDuration(getDurationString(itinerary.getDuration()));
            trip.setTotalFlightDuration(getDurationString(totalDuration.toString()));
            trip.setTotalLayoverDuration(getDurationString(totalLayover.toString()));

            trip.setTripNo(i + 1);
            if (itineraries.length == 1) {
                trip.setTripType(TripType.ONE_WAY);
            } else if (itineraries.length == 2){
                trip.setTripType(TripType.RETURN);
            } else {
                trip.setTripType(TripType.MULTI_CITY);
            }

            trips.add(trip);
        }
        return trips;
    }

    private static List<FlightAvailabilityResponse.Trip> createTrips(
            FlightOfferSearch.Itinerary[] itineraries, JsonObject carriers) {
        List<FlightAvailabilityResponse.Trip> trips = new ArrayList<>();
//        for(FlightOfferSearch.Itinerary itinerary : itineraries) {
        for(int i = 0; i < itineraries.length; i++) {
            FlightOfferSearch.Itinerary itinerary = itineraries[i];
            FlightAvailabilityResponse.Trip trip = new FlightAvailabilityResponse.Trip();
            List<FlightAvailabilityResponse.Leg> legs = new ArrayList<>();
            Duration totalLayover = Duration.ZERO;
            Duration totalDuration = Duration.ZERO;

            FlightOfferSearch.SearchSegment[] segments = itinerary.getSegments();
            String from = segments[0].getDeparture().getIataCode();
            String to = segments[segments.length - 1].getArrival().getIataCode();
            int stops = segments.length - 1;

//            for(FlightOfferSearch.SearchSegment segment: itinerary.getSegments()) {

            for(int j = 0; j < segments.length; j++) {
                FlightOfferSearch.SearchSegment segment = segments[j];
                FlightAvailabilityResponse.Leg leg = new FlightAvailabilityResponse.Leg();

//                JsonElement jsonOCName = carriers.get(segment.getOperating().getCarrierCode());
//                String operatingCarrierName = jsonOCName;
//                JsonElement jsonCName = carriers.get(segment.getCarrierCode());
//                String carrierName = jsonCName;
                String operatingCarrierName = Optional.ofNullable(segment.getOperating())
                                .map(o -> o.getCarrierCode())
                                .filter(carriers::has)
                                .map(code -> carriers.get(code).getAsString())
                                .orElse("AIRLINE");

                String carrierName = Optional.ofNullable(segment.getOperating())
                                .map(o -> o.getCarrierCode())
                                .filter(carriers::has)
                                .map(code -> carriers.get(code).getAsString())
                                .orElse("AIRLINE");

                leg.setLegNo(segment.getId());
                leg.setFlightNumber(segment.getNumber());
                if(segment.getOperating() != null) {
                    leg.setOperatingCarrierCode(segment.getOperating().getCarrierCode());
                }
                leg.setOperatingCarrierName(operatingCarrierName);
                leg.setCarrierCode(segment.getCarrierCode());

                leg.setCarrierName(carrierName);
                leg.setAircraftCode(segment.getAircraft().getCode());
                leg.setDepartureAirport(segment.getDeparture().getIataCode());
                leg.setDepartureTerminal(segment.getDeparture().getTerminal());
                leg.setDepartureDateTime(segment.getDeparture().getAt());
                leg.setArrivalAirport(segment.getArrival().getIataCode());
                leg.setArrivalTerminal(segment.getArrival().getTerminal());
                leg.setArrivalDateTime(segment.getArrival().getAt());
                leg.setDuration(getDurationString(segment.getDuration()));
                totalDuration = totalDuration.plus(Duration.parse(segment.getDuration()));

                if (j < segments.length - 1) {
                    LocalDateTime arrivalTime = LocalDateTime.parse(segment.getArrival().getAt());
                    LocalDateTime nextDepartureTime = LocalDateTime.parse(segments[j + 1].getDeparture().getAt());
                    Duration layover = Duration.between(arrivalTime, nextDepartureTime);
                    totalLayover = totalLayover.plus(layover);
                    totalDuration = totalDuration.plus(layover);
                    leg.setLayoverAfter(getDurationString(layover.toString()));
                }

                legs.add(leg);
            }

            trip.setLegs(legs);
            trip.setFrom(from);
            trip.setTo(to);
            trip.setStops(stops);
//            trip.setTotalDuration(getDurationString(itinerary.getDuration()));
            trip.setTotalFlightDuration(getDurationString(totalDuration.toString()));
            trip.setTotalLayoverDuration(getDurationString(totalLayover.toString()));

            trip.setTripNo(i + 1);
            if (itineraries.length == 1) {
                trip.setTripType(TripType.ONE_WAY);
            } else if (itineraries.length == 2){
                trip.setTripType(TripType.RETURN);
            } else {
                trip.setTripType(TripType.MULTI_CITY);
            }
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
