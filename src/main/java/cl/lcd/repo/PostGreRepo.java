package cl.lcd.repo;


//import cl.lcd.model.PostGreLog;
import cl.lcd.dto.logs.PostGreLog;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Profile("!nodb")
public interface PostGreRepo extends JpaRepository<PostGreLog,Long> {

    List<PostGreLog> findByLogTimestampBetween(LocalDateTime start, LocalDateTime end);
}

