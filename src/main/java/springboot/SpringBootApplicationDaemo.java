package springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import wechart.WechartApplication;

/**
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2017/8/18
 * @description
 */
@SpringBootApplication
@RestController
public class SpringBootApplicationDaemo {

    @RequestMapping("/test")
    @ResponseBody
    public String getValue() {
        return "test";
    }

    public static void main(String[] args) {

        SpringApplication.run(SpringBootApplicationDaemo.class, args);

    }

}
