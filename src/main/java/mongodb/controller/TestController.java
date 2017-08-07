package mongodb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import mongodb.model.User;
import mongodb.service.UserService;

import java.util.List;

/**
 * Created by tianjian on 2017/8/6.
 */
@RestController
public class TestController {
    @Autowired
    private UserService userService;
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * save use before findName
     * @return
     */
    @GetMapping("/save")
    public User save() {
        User user = new User(2, "Tseng", 21);
        mongoTemplate.save(user);
        //也可以使用Repository插入数据，userService.save(user);
        return user;
    }

    @GetMapping("/find")
    public List<User> find() {
        List<User> userList = mongoTemplate.findAll(User.class);
        return userList;
    }

    /**
     * input String name "Tseng"
     * @param name
     * @return
     */
    @GetMapping("/findByName")
    public User findByName(@RequestParam("name") String name) {
        User user = userService.findByName(name);
        return user;
    }
}
