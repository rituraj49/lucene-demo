
package cl.lcd.service.hotel;


import com.amadeus.resources.*;
import com.amadeus.Amadeus;
import com.amadeus.exceptions.ResponseException;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class HotelBookingServices {

    @Autowired
    private Amadeus amadeusClient;

    private final Gson gson=new Gson();


    public String bookHotelV2(String body) throws ResponseException {
        HotelOrder response = amadeusClient.booking.hotelOrders.post(body);
        return response.getResponse().getBody();
    }

}

