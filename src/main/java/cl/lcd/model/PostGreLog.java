package cl.lcd.model;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.context.annotation.Profile;

import java.time.LocalDateTime;


@Entity
@Table(name = "user_logs")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Profile("!nodb")
public class PostGreLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderId;

    private LocalDateTime logTimestamp = LocalDateTime.now();

    @Column(length = 5000)
    private String requestPayload;

    @Column(length = 5000)
    private String responsePayload;

    private Integer numberOfTravellers;

    private String totalAmount;

    private String fromLocation;

    private String toLocation;

    public PostGreLog(String orderId, LocalDateTime logTimestamp, String requestPayload,
                      String responsePayload, Integer numberOfTravellers,
                      String totalAmount, String fromLocation, String toLocation) {
        this.orderId = orderId;
        this.logTimestamp = logTimestamp;
        this.requestPayload = requestPayload;
        this.responsePayload = responsePayload;
        this.numberOfTravellers = numberOfTravellers;
        this.totalAmount = totalAmount;
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
    }
}



