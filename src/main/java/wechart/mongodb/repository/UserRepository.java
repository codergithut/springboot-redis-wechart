package wechart.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import wechart.mongodb.model.User;

/**
 * Created by tianjian on 2017/8/6.
 */
public interface UserRepository extends MongoRepository<User, String> {

    User findByName(String name);
}
