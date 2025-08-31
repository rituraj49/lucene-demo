package cl.lcd.service.hotel;


import cl.lcd.dto.hotel.HotelOfferResponse;
import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.resources.HotelOfferSearch;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class HotelOfferService {


    private static final Logger log = LoggerFactory.getLogger(HotelOfferService.class);

    @Autowired
    private Amadeus amadeusClient;



    private final Gson gson = new Gson();




    public List<HotelOfferResponse> getOffers(Map<String, String> paramsMap) throws Exception {
        Params params = null;
        for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
            if (params == null) {
                params = Params.with(entry.getKey(), entry.getValue());
            } else {
                params.and(entry.getKey(), entry.getValue());
            }
        }


        HotelOfferSearch[] offers = amadeusClient.shopping.hotelOffersSearch.get(params);

        String json = gson.toJson(offers);
        HotelOfferResponse[] hotelOffers = gson.fromJson(json, HotelOfferResponse[].class);

        return Arrays.asList(hotelOffers);
    }


}
