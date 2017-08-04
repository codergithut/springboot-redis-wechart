package wechart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import wechart.model.User;

import wechart.service.impl.UserServiceImpl;
import wechart.util.RandomUtils;
import wechart.util.UUIDTool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Random;

/**
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2017/8/4
 * @description
 */
@RestController
public class UserController {

    @Autowired
    HashOperations hashOperations;

    @Autowired
    private UserServiceImpl service;

    //添加
    @RequestMapping(value = "/registuser", method = RequestMethod.GET)
    @ResponseBody
    public Object test() {
        System.out.println("start.....");
        User m = new User();
        m.setRedisKey(UUIDTool.getUUID());
        m.setPassword("123");
        m.setUsername("陈龙");
        m.setBinding("1731857742@qq.com");
        service.put(m.getRedisKey(), m, -1);
        hashOperations.put("BINDINGINFO", "1731857742@qq.com", m.getRedisKey());
        hashOperations.put("USRNAMEINFO", m.getUsername(), m.getRedisKey());
        System.out.println("add success end...");
        return m;
    }


    /**
     * 需要获取code
     */
    @RequestMapping(value = "/changeinfo", method = RequestMethod.GET)
    @ResponseBody
    public void changeInfo() {

        //根据邮箱获取的验证码，前端获取
        String code = "34567FSX";

        String changeId = (String)hashOperations.get("RESETUSERINFO", code);

        User m = service.get(changeId);
        m.setPassword("tianjian");
        service.put(m.getRedisKey(), m, -1);
        System.out.println("add success end...");
    }

    @RequestMapping(value = "/logininfo", method = RequestMethod.GET)
    @ResponseBody
    public String loignInfo(HttpServletResponse response) {
        boolean flag = false;

        String o = (String)hashOperations.get("USRNAMEINFO", "陈龙");

        User m = service.get(o);
        String token = UUIDTool.getUUID();

        if(m.getUsername().equals("陈龙") && m.getPassword().equals("123")) {
            hashOperations.put("LOGININFO", token, m.getRedisKey());
            Cookie cookie = new Cookie("wechart-cookie", token);
            response.addCookie(cookie);
            flag = true;
        }

        if(!flag) {
            //验证错误的提示
        }

        return "hahahaha";
    }

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String username;

    @RequestMapping(value = "/getmail", method = RequestMethod.GET)
    @ResponseBody
    public String getMail(HttpServletResponse response) {

        String changeId = (String)hashOperations.get("BINDINGINFO", "1731857742@qq.com");

        User m = service.get(changeId);

        String code = RandomUtils.randomUtil();

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(username);
        simpleMailMessage.setTo("1731857742@qq.com");//接收邮件的邮箱
        simpleMailMessage.setSubject("微聊密码重置");
        simpleMailMessage.setText("尊敬的用户你好，你现在正在尝试重置用户名为:" + m.getUsername()
                + "的密码！" + "验证码如下: " + code);

        hashOperations.put("RESETUSERINFO", code, m.getRedisKey());

        mailSender.send(simpleMailMessage);

        return "hahahaha";
    }



}
