package cl.lcd;

import cl.lcd.service.LuceneService;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class LuceneDemoApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(LuceneDemoApplication.class, args);
//		LuceneService.analyzeText();
	}

}
