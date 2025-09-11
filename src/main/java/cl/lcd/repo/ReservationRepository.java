package cl.lcd.repo;

import cl.lcd.model.Reservation;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Profile("!nodb")
public interface ReservationRepository extends JpaRepository<Reservation,Long> {

    List<Reservation> findReservationByTravelerNameContainingIgnoreCase(String name);

    Reservation findReservationByBookingId(String id);

}
