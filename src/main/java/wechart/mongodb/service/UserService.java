package wechart.mongodb.service;

import org.springframework.stereotype.Repository;
import wechart.mongodb.model.User;

/**
 * Created by tianjian on 2017/8/6.
 */
@Repository
public interface UserService {
    void save(User user);

    User findByName(String name);

}
