package cl.lcd.service;

import cl.lcd.dto.search.FlightDateResponse;
import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.resources.FlightDate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Slf4j
@Service
public class CheapestDateSearchService {


    @Autowired
    private Amadeus amadeusClient;



    public List<FlightDateResponse> getCheapestFlightDates(Map<String, Object> params) throws Exception {
        try {
            Params queryParams = null;
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                if (queryParams == null) {
                    queryParams = Params.with(entry.getKey(), entry.getValue());
                } else {
                    queryParams.and(entry.getKey(), entry.getValue());
                }
            }

            if (queryParams == null) {
                throw new IllegalArgumentException("No parameters provided");
            }

            FlightDate[] flightDates = amadeusClient.shopping.flightDates.get(queryParams);

            List<FlightDateResponse> responseList = new ArrayList<>();

            for (FlightDate fd : flightDates) {
                FlightDateResponse resp = new FlightDateResponse();

                resp.setOrigin(fd.getOrigin());
                resp.setDestination(fd.getDestination());
                resp.setDepartureDate(String.valueOf(fd.getDepartureDate()));
                resp.setReturnDate(String.valueOf(fd.getReturnDate()));
                if (fd.getPrice() != null) {
                    resp.setTotalPrice(String.valueOf(fd.getPrice().getTotal()));
                }

                responseList.add(resp);
            }

           /* for(FlightDateResponse resp : responseList){
                System.out.println(resp);
            }*/


            return responseList;

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error fetching cheapest flight dates: " + e.getMessage(), e);
        }
    }

}
