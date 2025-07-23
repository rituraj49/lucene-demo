package cl.lcd.repo;


import cl.lcd.model.PostGreLog;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PostGreRepo extends JpaRepository<PostGreLog,Long> {
}

