package neo4j;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

import java.io.IOException;

/**
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2017/8/11
 * @description
 */
@SpringBootApplication
public class ServerApplication extends SpringBootServletInitializer implements CommandLineRunner {


    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ServerApplication.class);
    }


    public static void main(String[] args) throws IOException {
        SpringApplication.run(ServerApplication.class, args);
    }


    @Override
    public void run(String... strings) throws Exception {

        System.out.println("初始化。。。。。。。。。。。。");
    }
}
