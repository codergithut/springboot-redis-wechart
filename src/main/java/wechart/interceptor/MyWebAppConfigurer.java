package wechart.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import wechart.config.CommonValue;
import wechart.config.RedisConfig;

/**
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2017/8/4
 * @description
 */
//@Configuration
//@Import(RedisConfig.class)
public class MyWebAppConfigurer extends WebMvcConfigurerAdapter implements CommonValue {
    @Autowired
    RedisConfig redisConfig;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(redisConfig.getMyInterceptor()).addPathPatterns("/**").excludePathPatterns("//" + PRFIX_LOGIN,"//" + PRFIX_REGISTER,"/save");
        super.addInterceptors(registry);
    }

}