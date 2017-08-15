package neo4j.controller;

import neo4j.model.Task;
import neo4j.model.User;
import neo4j.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.template.Neo4jTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2017/8/11
 * @description
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserRepository userRepository;


    @Autowired
    Neo4jTemplate neo4jTemplate;

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    @Transactional
    public Object saveTask() {
        User user = new User();
        user.setPassword("test");
        user.setName("地球人");



        User user1 = new User();
        user1.setPassword("test");
        user1.setName("火星人");
        user1.setSex("male");

        User user2 = new User();
        user2.setPassword("test");
        user2.setName("月球人");
        user2.setSex("male");

        user1.addFriend(user);

        user1.addFriend(user2);

        user2.addFriend(user1);

        user2.addFriend(user);

        user.addFriend(user1);

        user.addFriend(user2);

        User user3 = userRepository.save(user);
        return user3;
    }



    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    public Object create() {
        Iterable<User> user = userRepository.findAll();
        userRepository.delete(user);
        return user;
    }
}
