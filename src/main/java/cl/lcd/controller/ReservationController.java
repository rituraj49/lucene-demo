package cl.lcd.controller;

import cl.lcd.model.Reservation;
import cl.lcd.service.ReservationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class ReservationController {

    @Autowired(required = false)
    private ReservationService reservationService;
/*

    @GetMapping("/traveler-name/{name}")
    public ResponseEntity<?> findAllReservationByName(@PathVariable String name){
        log.info(" Reservation Controller Traveler Name is {}",name);
       List<Reservation> reservations=reservationService.findAllReservationByName(name);
        if (name==null || reservations.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reservation Not Found ");
        }
        log.info("Reservation Found SuccessFully Name {}",name);
        return ResponseEntity.status(HttpStatus.OK).body(reservations);
    }
*/

    @GetMapping("/reservation-booking/{bookingId}")
    public ResponseEntity<?> findReservationByBookingId(@PathVariable String bookingId){
         Reservation reservation= reservationService.findByBookingId(bookingId);
        log.info(" Reservation Controller Booking Id is {}",bookingId);
        if (bookingId==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reservation Not Found ");
        }
        log.info("Reservation Found SuccessFully BookingId {}",bookingId);
        return ResponseEntity.status(HttpStatus.OK).body(reservation);
    }






/*

    @GetMapping("/reservation-search")
    public ResponseEntity<?> findReservation(@RequestParam(required = true) String bookingId,@RequestParam(required = false) String name) {

        if (name != null) {
            log.info("Searching reservations by traveler name: {}", name);
            List<Reservation> reservations = reservationService.findAllReservationByName(name);
            if (reservations.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No reservations found for traveler name: " + name);
            }
            log.info("Reservations found for traveler name: {}", name);
            return ResponseEntity.ok(reservations);
        }

        if (bookingId != null) {
            log.info("Searching reservation by booking ID: {}", bookingId);
            Reservation reservation = reservationService.findByBookingId(bookingId);
            if (reservation == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No reservation found for booking ID: " + bookingId);
            }
            log.info("Reservation found for booking ID: {}", bookingId);
            return ResponseEntity.ok(reservation);
        }

        return ResponseEntity.badRequest().body("Please provide either 'name' or 'bookingId' as a query parameter.");
    }
*/



}
