package wechart.controller;

import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import wechart.config.CommonValue;
import wechart.model.User;
import wechart.util.GetPingyin;
import wechart.util.RandomUtils;
import wechart.util.UUIDTool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import static wechart.util.RandomUtils.getNumberAsId;

/**
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2017/8/4
 * @description
 */
@RestController
@RequestMapping(value = "/user")
public class UserController implements CommonValue{

    @Autowired
    HashOperations hashOperations;

    @Autowired
    SetOperations setOperations;

//    @Autowired
//    @Qualifier(JEDIS)
//    Jedis write;
//
//    @Autowired
//    private UserServiceImpl service;

    //注册添加用户信息
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public Object registUser(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("email") String email) throws BadHanyuPinyinOutputFormatCombination {

//        String inviteCode = "00000000";

        //http://localhost:8080/user/register
//
//        if(setOperations.isMember("INVITECODE", inviteCode)) {
//            setOperations.move("INVITECODE", inviteCode, "");
//        } else {
//            return null;
//        }
        User m = new User();
        String userId = getNumberAsId(2);


        while(setOperations.isMember(USERID,userId)) {
            userId = getNumberAsId(10);
        }
        setOperations.add(USERID, userId);
        m.setRedisKey(userId);
        m.setPassword(password);
        m.setUsername(username);
        m.setPinying(GetPingyin.getPingYin(m.getUsername()));
        m.setBinding(email);
        hashOperations.put(USRINFO, m.getRedisKey(), m);
        hashOperations.put(BINDINGINFO, email, m.getRedisKey());
        System.out.println("add success end...");
        return m;
    }


    /**
     * 页面需要获取code，然后修改用户信息
     */
    @RequestMapping(value = "/change", method = RequestMethod.POST)
    @ResponseBody
    public Object changeInfo() {

        //根据邮箱获取的验证码，前端获取
        String code = "34567FSX";

        String changeId = (String)hashOperations.get(RESETUSERINFO, code);

        User m = (User)hashOperations.get(USRINFO, changeId);
        m.setPassword("tianjian");

        hashOperations.put(USERID, m.getRedisKey(), m);
        System.out.println("add success end...");
        return m;
    }

    //根据用户名进行用户信息验证
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public Object loignInfo(HttpServletResponse response, @RequestParam("account") String account, @RequestParam("password") String password) {
        boolean flag = false;
        User m = (User)hashOperations.get(USERID,account);

        if(m == null) {
            return false;
        }

        String token = UUIDTool.getUUID();
        if(m.getRedisKey().equals(account) && m.getPassword().equals(password)) {
            hashOperations.put(LOGININFO, token, m.getRedisKey());
            Cookie cookie = new Cookie(COOK_NAME, token);
            response.addCookie(cookie);
            flag = true;
        }

        if(!flag) {
            return false;
        }

        return token;
    }



    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String username;

    //根据用户信息发送消息到用户邮箱，用户提供邮箱信息
    @RequestMapping(value = "/sendMailInfo", method = RequestMethod.POST)
    @ResponseBody
    public String getMail(HttpServletResponse response) {

        String changeId = (String)hashOperations.get(BINDINGINFO, "1731857742@qq.com");

        User m = (User)hashOperations.get(USERID,changeId);

        String code = RandomUtils.randomUtil();

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(username);
        simpleMailMessage.setTo("1731857742@qq.com");//接收邮件的邮箱
        simpleMailMessage.setSubject("微聊密码重置");
        simpleMailMessage.setText("尊敬的用户你好，你现在正在尝试重置用户名为:" + m.getUsername()
                + "的密码！" + "验证码如下: " + code);

        hashOperations.put(RESETUSERINFO, code, m.getRedisKey());

        mailSender.send(simpleMailMessage);

        return "hahahaha";
    }


    //用户发送邀请码
    @RequestMapping(value = "/sendInviteCode", method = RequestMethod.POST)
    @ResponseBody
    public String getInviteCode(HttpServletResponse response) {

        String inviteCode = RandomUtils.randomUtil();

        String initor = "1731857742@qq.com";

        String user = "1468195034@qq.com";

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(username);
        simpleMailMessage.setTo("1731857742@qq.com");//接收邮件的邮箱
        simpleMailMessage.setSubject("微聊密码邀请码已发送");
        simpleMailMessage.setText("尊敬的用户你好，你已将邀请码发送到邮箱:" + user
                + "邀请码码如下: " + inviteCode);

        simpleMailMessage.setFrom(username);
        simpleMailMessage.setTo("1468195034@qq.com");//接收邮件的邮箱
        simpleMailMessage.setSubject("你已收到邀请码");
        simpleMailMessage.setText("尊敬的用户你好，已收到:" + initor
                + " 发出的邀请码，邀请码码如下: " + inviteCode);

        setOperations.add(INVITECODE, inviteCode);

        System.out.println(inviteCode);

        return "hahahaha";
    }

    @RequestMapping(value = "/checkToken", method = RequestMethod.POST)
    @ResponseBody
    public Object checkToken(HttpServletResponse response) {
        return true;
    }

}
