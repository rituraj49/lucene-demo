package cl.lcd.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bookingId;

    private String price;

    private String currencyCode;

    private String source;

    private String destination;

//    @Column(name = "traveler_name")
    private String travelerName;

    private String email;

    private String phoneNo;

    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;

    @Column(length = 7000)
    String bookingResponse;
/*

    public Reservation(String bookingId, String price, String currencyCode, String source, String destination, String travelerName, String email, String phoneNo) {
    }
*/

    public Reservation(String bookingId, String price, String currencyCode, String source, String destination, String traveler_name, String email, String phoneNo, BookingStatus bookingStatus,String bookingResponse) {
        this.bookingId = bookingId;
        this.price = price;
        this.currencyCode = currencyCode;
        this.source = source;
        this.destination = destination;
        this.travelerName = traveler_name;
        this.email = email;
        this.phoneNo = phoneNo;
        this.bookingStatus = bookingStatus;
        this.bookingResponse=bookingResponse;
    }

    public enum BookingStatus{
        CANCEL,
        CONFIRM
    }


    public Reservation() {
    }
}
