package cl.lcd.messaging;

import cl.lcd.dto.booking.FlightBookingResponse;
import cl.lcd.messaging.event.FlightBookingEvent;
import cl.lcd.service.mailing.EmailService;
import org.apache.kafka.common.internals.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {
    @Autowired
    private KafkaTemplate<String , FlightBookingEvent> kafkaTemplate;

    private static final String TOPIC = "lucene.flight.booking.topic";

    public void sendConfirmationEmail(FlightBookingEvent bookingEvent) {
        var message = MessageBuilder
                .withPayload(bookingEvent)
                .setHeader(KafkaHeaders.TOPIC, TOPIC)
                .build();

        kafkaTemplate.send(message);
    }
}
