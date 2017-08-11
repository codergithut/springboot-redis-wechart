package mongodb.service;

import mongodb.model.User;
import org.springframework.stereotype.Repository;

/**
 * Created by tianjian on 2017/8/6.
 */
@Repository
public interface UserService {
    void save(User user);

    User findByName(String name);

}
