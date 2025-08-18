package cl.lcd;

import cl.lcd.service.LuceneService;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class LuceneDemoApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(LuceneDemoApplication.class, args);
//		LuceneService.analyzeText();
	}

}
