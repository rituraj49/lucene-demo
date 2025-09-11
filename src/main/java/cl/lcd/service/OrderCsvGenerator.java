package cl.lcd.service;

import cl.lcd.dto.logs.PostGreLog;
import cl.lcd.repo.PostGreRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderCsvGenerator {

    @Autowired(required = false)
    private PostGreRepo orderRepository;

    public File generateTodayOrdersCsv() throws IOException {
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = start.plusDays(1);

        List<PostGreLog> logs = orderRepository.findByLogTimestampBetween(start, end);

        File csvFile = File.createTempFile("user-logs-", ".csv");

        try (PrintWriter writer = new PrintWriter(csvFile)) {
            writer.println("Order ID,From,To,Travellers,Amount, CurrencyCode,Timestamp");

            for (PostGreLog log : logs) {
                writer.printf("\"%s\",\"%s\",\"%s\",%d,\"%s\",\"%s\",\"%s\"%n",
                        log.getOrderId(),
                        log.getFromLocation(),
                        log.getToLocation(),
                        log.getNumberOfTravellers(),
                        log.getTotalAmount(),
                        log.getCurrencyCode(),
                        log.getLogTimestamp());
            }
        }

        return csvFile;
    }


}
