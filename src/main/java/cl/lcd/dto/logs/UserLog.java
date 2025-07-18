package cl.lcd.dto.logs;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.time.LocalDateTime;

@Document(collection = "user_logs")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLog {

    @Id
    private String id;

    private String orderId;

    private LocalDateTime logTimestamp = LocalDateTime.now();

    private String requestPayload;

    private String responsePayload;

    private Integer numberOfTravellers;

    private String totalAmount;

    private String fromLocation;

    private String toLocation;

    public UserLog(String orderId, LocalDateTime logTimestamp, String requestPayload, String responsePayload, Integer numberOfTravellers, String totalAmount, String fromLocation, String toLocation) {
        this.orderId = orderId;
        this.logTimestamp = logTimestamp;
        this.requestPayload = requestPayload;
        this.responsePayload = responsePayload;
        this.numberOfTravellers = numberOfTravellers;
        this.totalAmount = totalAmount;
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public LocalDateTime getLogTimestamp() {
        return logTimestamp;
    }

    public void setLogTimestamp(LocalDateTime logTimestamp) {
        this.logTimestamp = logTimestamp;
    }

    public String getRequestPayload() {
        return requestPayload;
    }

    public void setRequestPayload(String requestPayload) {
        this.requestPayload = requestPayload;
    }

    public String getResponsePayload() {
        return responsePayload;
    }

    public void setResponsePayload(String responsePayload) {
        this.responsePayload = responsePayload;
    }

    public Integer getNumberOfTravellers() {
        return numberOfTravellers;
    }

    public void setNumberOfTravellers(Integer numberOfTravellers) {
        this.numberOfTravellers = numberOfTravellers;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(String fromLocation) {
        this.fromLocation = fromLocation;
    }

    public String getToLocation() {
        return toLocation;
    }

    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }
}

