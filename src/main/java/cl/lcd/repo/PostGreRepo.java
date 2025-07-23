package cl.lcd.repo;


import cl.lcd.dto.logs.PostGreLogs;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PostGreRepo extends JpaRepository<PostGreLogs,Long> {
}

