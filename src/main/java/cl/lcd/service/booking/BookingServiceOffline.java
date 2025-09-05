package cl.lcd.service.booking;

import cl.lcd.dto.booking.FlightBookingRequest;
import cl.lcd.dto.booking.FlightBookingResponse;
import com.amadeus.exceptions.ResponseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;


@Service
@Profile("offline")
public class BookingServiceOffline implements BookingServiceInterface{

    private final ObjectMapper mapper = new ObjectMapper();

    private String readFile(String fileName) throws Exception {
        File file = new ClassPathResource(fileName).getFile();
        return Files.readString(file.toPath());
    }

    @Override
    public FlightBookingResponse bookFlight(FlightBookingRequest flightOrderRequest) throws ResponseException {
        try {
            String json=readFile("booking_confirm_response.text");
            return mapper.readValue(json, new TypeReference<>() {});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
