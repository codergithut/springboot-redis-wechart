package mongodb.repository;

import mongodb.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by tianjian on 2017/8/6.
 */
public interface UserRepository extends MongoRepository<User, String> {

    User findByName(String name);
}
