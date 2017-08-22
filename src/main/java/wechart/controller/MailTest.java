package wechart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2017/5/18
 * @description
 */
@RestController
@RequestMapping(value="/mail")
public class MailTest {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String username;

    @RequestMapping(value="", method= RequestMethod.GET)
    public void testSendMail() {

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setFrom(username);
        simpleMailMessage.setTo("1468195034@qq.com");//接收邮件的邮箱
        simpleMailMessage.setSubject("测试");
        simpleMailMessage.setText("测试服务");

        mailSender.send(simpleMailMessage);
    }

}
