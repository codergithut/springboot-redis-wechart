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
public class MyWebAppConfigurer extends WebMvcConfigurerAdapter {

    @Autowired
    RedisConfig redisConfig;

    @Autowired
    MyInterceptor myInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(myInterceptor).addPathPatterns("/**").excludePathPatterns("//" + CommonValue.PRFIX_LOGIN,"//" + CommonValue.PRFIX_REGISTER,"/save");
        super.addInterceptors(registry);
    }

}