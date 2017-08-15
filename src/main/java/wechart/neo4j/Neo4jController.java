package wechart.neo4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.template.Neo4jTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import wechart.model.Masses;
import wechart.model.User;
import wechart.neo4j.repository.MassesRepository;
import wechart.neo4j.repository.UserRepository;

/**
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2017/8/11
 * @description
 */
@RestController
@RequestMapping("/user")
public class Neo4jController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    MassesRepository massesRepository;


    @Autowired
    Neo4jTemplate neo4jTemplate;

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    @Transactional
    public Object saveTask() {

        User user = new User();

        user.setPassword("diqiuren");

        user.setName("地球人");

        Masses masses = new Masses();

        masses.setType("普通群");

        masses.addMasses(user);

        user.addMasses(masses);

        user.setRedisKey("222");

        User huoXinRen = new User();

        huoXinRen.setPassword("huoxingren");

        Masses masses1 = new Masses();

        masses1.setType("火星群");

        huoXinRen.addMasses(masses1);

        huoXinRen.addMasses(masses);

        huoXinRen.addFriends(user);

        huoXinRen.setName("火星人");

        huoXinRen.setRedisKey("3333");

        user.addFriends(huoXinRen);

        return userRepository.save(user);
    }



    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public Object create() {
        Iterable<User> user = userRepository.findAll();
        Iterable<Masses> masses = massesRepository.findAll();
        userRepository.delete(user);
        massesRepository.delete(masses);
        return "success";

    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    public Object searchNode() {
        Object t = userRepository.findByRedisKey("222");
        return t;
    }
}
