package cl.lcd.messaging.listener;

import cl.lcd.dto.booking.FlightBookingResponse;
import cl.lcd.dto.booking.TravelerResponseDto;
import cl.lcd.messaging.event.FlightBookingEvent;
import cl.lcd.messaging.event.KafkaFlightAvailabilityResponse;
import cl.lcd.service.mailing.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlightBookingListener {

    @Autowired
    EmailService emailService;

    @KafkaListener(topics = "lucene.flight.booking.topic", groupId = "lucene-flights-booking-group")
    public void emailSender(FlightBookingEvent bookingEvent) {
        System.out.println("received kafka event" + bookingEvent.getOrderId());
        List<String> toEmails = bookingEvent.getTravelers().stream().map(TravelerResponseDto::getEmail).toList();

        emailService.sendEmail(
                toEmails, "Flight booking confirmation",
                "order-confirmation-styled.html", bookingEvent
        );
    }
}
