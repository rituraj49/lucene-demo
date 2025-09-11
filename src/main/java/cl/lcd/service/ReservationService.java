package cl.lcd.service;

import cl.lcd.dto.booking.FlightBookingRequest;
import cl.lcd.dto.booking.FlightBookingResponse;
import cl.lcd.model.Reservation;
import cl.lcd.repo.ReservationRepository;
//import cl.lcd.config.JacksonConfig.*;
import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@Profile("!nodb")
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public void saveReservation(String bookingId, String price, String currencyCode, String source, String destination, String traveler_name, String email, String phoneNo , Reservation.BookingStatus bookingStatus, String bookingResponseJson){
        Reservation reservation=new Reservation(bookingId,price,currencyCode,source,destination,traveler_name,email,phoneNo,bookingStatus,bookingResponseJson);
        reservationRepository.save(reservation);
    }


    public void createReservation(FlightBookingResponse bookingResponse) throws JsonProcessingException {
        String bookingId=bookingResponse.getOrderId();
        bookingId = URLDecoder.decode(bookingId, StandardCharsets.UTF_8);
        String price=bookingResponse.getFlightOffer().getTotalPrice();
        String currencyCode=bookingResponse.getFlightOffer().getCurrencyCode();
        String source=bookingResponse.getFlightOffer().getTrips().get(0).getFrom();

        //for multi city search
        int legs=bookingResponse.getFlightOffer().getTrips().size();
        System.out.println("number of lages : "+ legs);



        String destination=null;
        if(legs>1){
             destination=bookingResponse.getFlightOffer().getTrips().get(legs-1).getTo();
        }else{
             destination=bookingResponse.getFlightOffer().getTrips().get(0).getTo();
        }
        //String destination=bookingResponse.getFlightOffer().getTrips().get(0).getTo();



        String traveler_name=bookingResponse.getTravelers().get(0).getFirstName()+" "+bookingResponse.getTravelers().get(0).getLastName();
        String email=bookingResponse.getTravelers().get(0).getEmail();
        String phoneNo=bookingResponse.getTravelers().get(0).getPhones().get(0).getNumber();
        Reservation.BookingStatus bookingStatus= Reservation.BookingStatus.CONFIRM;
        String bookingResponseJson=objectMapper.writeValueAsString(bookingResponse);
        saveReservation(bookingId,price,currencyCode,source,destination,traveler_name,email,phoneNo,bookingStatus,bookingResponseJson);
    }


    public List<Reservation> findAllReservationByName(String name){
        return reservationRepository.findReservationByTravelerNameContainingIgnoreCase(name);
    }


    public Reservation findByBookingId(String bookingId){
        return  reservationRepository.findReservationByBookingId(bookingId);
    }
}
