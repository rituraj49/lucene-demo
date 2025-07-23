package cl.lcd.dto.logs;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "user_logs")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostGreLogs {

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

    public PostGreLogs(String orderId, LocalDateTime logTimestamp, String requestPayload,
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



