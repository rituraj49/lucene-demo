package cl.lcd.service.booking;

import cl.lcd.dto.booking.FlightBookingRequest;
import cl.lcd.dto.booking.FlightBookingResponse;
import cl.lcd.dto.booking.TravelerResponseDto;
import cl.lcd.dto.search.FlightAvailabilityResponse;
import cl.lcd.messaging.KafkaProducer;
import cl.lcd.messaging.event.FlightBookingEvent;
import cl.lcd.mappers.booking.FlightBookingResponseMapper;
import cl.lcd.messaging.event.KafkaFlightAvailabilityResponse;
import cl.lcd.service.mailing.EmailService;
import com.amadeus.Amadeus;
import com.amadeus.Response;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOfferSearch;
import com.amadeus.resources.FlightOrder;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@Slf4j
public class AmadeusBookingService {

    @Autowired
    Amadeus amadeusClient;

    @Autowired
    KafkaProducer kafkaProducer;

    @Autowired
    EmailService emailService;

    /**
     * Creates a flight order using the provided json object of flight order request.
     * @param flightOrderRequest = JsonObject
     * @return FlightOrder
     * @throws ResponseException
     */
    public FlightBookingResponse createFlightOrder(FlightBookingRequest flightOrderRequest) throws ResponseException {
        log.info("Creating flight order with received request");
        Gson gson = new Gson();

        FlightOfferSearch offer = gson.fromJson(flightOrderRequest.getFlightOffer(), FlightOfferSearch.class);

        FlightOrder.Traveler[] flightTravelers = FlightBookingResponseMapper.createTravelersFromDto(flightOrderRequest.getTravelers());

        FlightOrder order = amadeusClient.booking.flightOrders.post(offer, flightTravelers);

        FlightBookingResponse bookingResponse = FlightBookingResponseMapper.flightBookingResponse(order, flightOrderRequest.getTravelers());

        emitFlightBookingEvent(bookingResponse);

        return bookingResponse;
    }

    public FlightBookingResponse getFlightOrder(String orderId) throws ResponseException {
        log.info("Retrieving flight order with ID: {}", orderId);
        FlightOrder order = amadeusClient.booking.flightOrder(orderId).get();
        return FlightBookingResponseMapper.flightBookingResponse(order);
    }

    public Response cancelFlightOrder(String orderId) throws ResponseException {
        log.info("Cancelling flight order with ID: {}", orderId);
        //        System.out.println("response: " + res);
        return amadeusClient.booking.flightOrder(orderId).delete();
    }

    public void emitFlightBookingEvent(FlightBookingResponse booking) {
        List<String> toEmails = booking.getTravelers().stream().map(TravelerResponseDto::getEmail).toList();
        emailService.sendEmail(
                toEmails, "Flight booking confirmation",
                "order-confirmation-styled.html",
                booking
        );
        FlightBookingEvent bookingEvent = new FlightBookingEvent();
//        bookingEvent.setBookingResponse(booking);

        FlightAvailabilityResponse flightOffer = booking.getFlightOffer();
        KafkaFlightAvailabilityResponse flightAvailabilityResponse = new KafkaFlightAvailabilityResponse();
        flightAvailabilityResponse.setSeatsAvailable(flightOffer.getSeatsAvailable());
        flightAvailabilityResponse.setCurrencyCode(flightOffer.getCurrencyCode());
        flightAvailabilityResponse.setBasePrice(flightOffer.getBasePrice());
        flightAvailabilityResponse.setTrips(flightOffer.getTrips());
        flightAvailabilityResponse.setOneWay(flightOffer.isOneWay());
        flightAvailabilityResponse.setTotalPrice(flightOffer.getTotalPrice());
        flightAvailabilityResponse.setTotalTravelers(flightOffer.getTotalTravelers());

        bookingEvent.setOrderId(booking.getOrderId());
        bookingEvent.setTravelers(booking.getTravelers());
        bookingEvent.setFlightOffer(flightAvailabilityResponse);
        bookingEvent.setCreatedAt(Instant.now());

//        kafkaProducer.sendConfirmationEmail(bookingEvent);
    }
}
