package cl.lcd.controller;

import cl.lcd.service.hotel.HotelBookingServices;
import com.amadeus.exceptions.ResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hotel")
public class HotelBookingController {


    @Autowired
     private HotelBookingServices hotelBookingServices;


    @PostMapping("/hotel/v2")
    public ResponseEntity<?> bookHotelV2(@RequestBody String body) {
        try {
            String response = hotelBookingServices.bookHotelV2(body);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        } catch (ResponseException e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body(e.getDescription());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Unexpected error: " + e.getMessage());
        }
    }


}
