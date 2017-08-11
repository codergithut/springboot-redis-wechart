package mongodb.service.impl;

import mongodb.model.User;
import mongodb.repository.UserRepository;
import mongodb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by tianjian on 2017/8/6.
 */

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    public void save(User user) {
        userRepository.save(user);
    }

    public User findByName(String name) {
        return this.userRepository.findByName(name);
    }
}
