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

    /**
     * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
     * @description redis配置服务
     */
    @Autowired
    RedisConfig redisConfig;

    /**
     * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
     * @description 过滤器实例
     */
    @Autowired
    MyInterceptor myInterceptor;

    /**
     * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
     * @param
     * @return void
     * @description 过滤器配置
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(myInterceptor).addPathPatterns("/**").excludePathPatterns("//" + CommonValue.PRFIX_LOGIN,"//" + CommonValue.PRFIX_REGISTER,"/save");
        super.addInterceptors(registry);
    }

}