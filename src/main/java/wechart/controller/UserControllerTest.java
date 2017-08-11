package wechart.controller;

import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import wechart.config.CommonValue;
import wechart.model.User;
import wechart.service.impl.UserServiceImpl;
import wechart.util.GetPingyin;
import wechart.util.UUIDTool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static wechart.util.RandomUtils.getNumberAsId;

/**
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2017/8/4
 * @description
 */
@RestController
@RequestMapping(value = "/test")
public class UserControllerTest implements CommonValue{

    @Autowired
    HashOperations hashOperations;

    @Autowired
    SetOperations setOperations;

    @Autowired
    @Qualifier(JEDIS)
    Jedis write;

    @Autowired
    private UserServiceImpl service;

    //注册添加用户信息
    @RequestMapping(value = "/registerTest", method = RequestMethod.GET)
    @ResponseBody
    public Object registUserTest() throws BadHanyuPinyinOutputFormatCombination {
        User m = new User();
        String userId = getNumberAsId(2);
        while(write.sismember(USERID, userId)) {
            userId = getNumberAsId(10);
        }
        setOperations.add(USERID, userId);
        m.setRedisKey(userId);
        m.setPassword("root1");
        m.setUsername("root1");
        m.setPinying(GetPingyin.getPingYin(m.getUsername()));
        m.setBinding("1467237662@qq.com");
        service.put(m.getRedisKey(), m, -1);
        hashOperations.put(BINDINGINFO, "1467237662@qq.com", m.getRedisKey());
        return m;
    }

    //根据用户名进行用户信息验证
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @ResponseBody
    public Object loignInfo(HttpServletResponse response) {
        boolean flag = false;

        User m = service.get("15");

        String token = UUIDTool.getUUID();
        hashOperations.put(LOGININFO, token, m.getRedisKey());
        Cookie cookie = new Cookie(COOK_NAME, token);
        response.addCookie(cookie);
        return token;
    }

    //根据用户名进行用户信息验证
    @RequestMapping(value = "/getUser", method = RequestMethod.GET)
    @ResponseBody
    public Object getAllUser(HttpServletResponse response) {
        boolean flag = false;

        List<User> m = service.getAll();

        for(User user : m) {
            System.out.println(user.getRedisKey());
        }

        String token = UUIDTool.getUUID();
        Cookie cookie = new Cookie(COOK_NAME, token);
        response.addCookie(cookie);
        return token;
    }

    //根据用户名进行用户信息验证
    @RequestMapping(value = "/getToken", method = RequestMethod.GET)
    @ResponseBody
    public Object tokenCheck(HttpServletResponse response) {
        boolean flag = false;
        User user = new User();
        user.setBinding("sfasdfasdf");
        setOperations.add("test", user);

        Object o = setOperations.members("test");

        System.out.println();

        return null;
    }

}
