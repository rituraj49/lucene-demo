package cl.lcd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class OrderReportScheduler {

    @Autowired
    private EmailServiceScheduler emailServiceScheduler;

    @Autowired
    private OrderCsvGenerator orderCsvGenerator;


    @Scheduled(cron = "0 24 * * * *")  // for every min
    //@Scheduled(cron = "0 0 0 * * *" )
    public void sendDailyOrderReport() {
        try {
            File csvFile = orderCsvGenerator.generateTodayOrdersCsv();
            emailServiceScheduler.sendEmailWithAttachment(
                    "mr.dhananjaykr2003@gmail.com",
                    "Daily Orders Report",
                    "Attached is today's order report.",
                    csvFile
            );
            csvFile.delete(); // Cleanup
        } catch (Exception e) {
            e.printStackTrace(); // Log error
        }
    }
}

