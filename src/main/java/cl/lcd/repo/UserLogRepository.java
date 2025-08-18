package cl.lcd.repo;

import cl.lcd.model.UserLog;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
@Profile("!nodb")
public interface UserLogRepository extends MongoRepository<UserLog, String> {
}
