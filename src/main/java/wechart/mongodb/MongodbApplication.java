package wechart.mongodb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2017/8/4
 * @description
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class MongodbApplication {

    public static void main(String[] args) {

        SpringApplication.run(MongodbApplication.class, args);

    }
}
