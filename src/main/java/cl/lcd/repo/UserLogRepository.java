package cl.lcd.repo;

import cl.lcd.model.UserLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLogRepository extends MongoRepository<UserLog, String> {
}
