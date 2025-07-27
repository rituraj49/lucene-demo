package cl.lcd.service;


import cl.lcd.dto.booking.FlightBookingRequest;
import cl.lcd.dto.booking.FlightBookingResponse;
import cl.lcd.model.PostGreLog;
import cl.lcd.repo.PostGreRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Slf4j
@Service
@Profile("!nodb")
public class PostGreLogsServices {

    @Autowired
    private PostGreRepo postGreRepo;


    @Autowired
    private ObjectMapper objectMapper;



    public void saveUserLog(String orderId, LocalDateTime logTimestamp, String requestPayload, String responsePayload,
                            Integer numberOfTravellers, String totalAmount,
                            String fromLocation, String toLocation) {
        PostGreLog log1 = new PostGreLog(orderId,logTimestamp, requestPayload, responsePayload,
                numberOfTravellers, totalAmount, fromLocation, toLocation);

//        System.out.println(log1);

        postGreRepo.save(log1);
    }




    public void createLogesPostGreDB(FlightBookingRequest orderRequest, FlightBookingResponse createdOrder){
        try {

            String requestJson = objectMapper.writeValueAsString(orderRequest);
            String responseJson = objectMapper.writeValueAsString(createdOrder);

            Integer numberOfTravellers = orderRequest.getTravelers() != null ? orderRequest.getTravelers().size() : 0;


            String totalAmount = createdOrder.getFlightOffer().getTotalPrice();
            String from = createdOrder.getFlightOffer().getTrips().get(0).getFrom();
            String to = createdOrder.getFlightOffer().getTrips().get(0).getTo();

            // Save log
            saveUserLog(
                    createdOrder.getOrderId(),
                    LocalDateTime.now(),
                    requestJson,
                    responseJson,
                    numberOfTravellers,
                    totalAmount,
                    from,
                    to
            );



        }catch (JsonProcessingException e) {
            log.error("Error converting request/response to JSON: {}", e.getMessage());
            e.printStackTrace();
        }

    }





}

