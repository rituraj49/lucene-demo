package cl.lcd.repo;


import cl.lcd.dto.logs.PostGreLogs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;


public interface PostGreRepo extends JpaRepository<PostGreLogs,Long> {

    List<PostGreLogs> findByLogTimestampBetween(LocalDateTime start, LocalDateTime end);

}

