package wechart.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import redis.clients.jedis.Jedis;
import wechart.interceptor.MyInterceptor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
 * @version 1.0, 2017/8/4
 * @description
 */
@Configuration
public class RedisConfig {

    /**
     * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
     * @description redis 服务的host地址
     */
    @Value("${custom.redis.host}")
    String host;

    /**
     * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
     * @description 注入到spring的RedisConnectionFactory连接工厂
     */
    @Autowired
    RedisConnectionFactory redisConnectionFactory;

    /**
     * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
     * @param
     * @return redisTemplate redis操作模板
     * @description 获取redisTemplate对象
     */
    @Bean
    public RedisTemplate<String, Object> functionDomainRedisTemplate() {

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();

        initDomainRedisTemplate(redisTemplate, redisConnectionFactory);

        return redisTemplate;

    }


    /**
     * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
     * @param
     * @return
     * @description 获取过滤器进行用户权限处理
     */
    @Bean
    @Autowired
    public MyInterceptor getMyInterceptor(HashOperations hashOperations) {

        MyInterceptor myInterceptor = new MyInterceptor();

        myInterceptor.setHashOperations(hashOperations);

        return myInterceptor;

    }

    /**
     * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
     * @param
     * @return
     * @description 获取redis连接池
     */
    @Bean
    public RedisConnectionFactory getRedisConnectionFactory() {

        JedisConnectionFactory redisFactory = new JedisConnectionFactory();

        redisFactory.setHostName(host);

        redisFactory.setTimeout(1000000);

        return redisFactory;

    }

    /**
     * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
     * @param redisTemplate
     * @param factory
     * @return
     * @description 设置redis序列化方式
     */
    private void initDomainRedisTemplate(RedisTemplate<String, Object> redisTemplate, RedisConnectionFactory factory) {

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setConnectionFactory(factory);

    }


    /**
     * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
     * @param redisTemplate
     * @return HashOperations 对象
     * @description 实例化 HashOperations 对象,可以使用 Hash 类型操作
     */
    @Bean
    public HashOperations<String, String, Object> hashOperations(RedisTemplate<String, Object> redisTemplate) {

        return redisTemplate.opsForHash();

    }

    /**
     * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
     * @param redisTemplate
     * @return ValueOperations 对象
     * @description 实例化 ValueOperations 对象,可以使用 Set 类型操作
     */
    @Bean
    public ValueOperations<String, Object> valueOperations(RedisTemplate<String, Object> redisTemplate) {

        return redisTemplate.opsForValue();

    }

    /**
     * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
     * @param redisTemplate
     * @return ListOperations 对象
     * @description 实例化 ListOperations 对象,可以使用 List 类型操作
     */
    @Bean
    public ListOperations<String, Object> listOperations(RedisTemplate<String, Object> redisTemplate) {

        return redisTemplate.opsForList();

    }

    /**
     * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
     * @param redisTemplate
     * @return SetOperations 对象
     * @description 实例化 SetOperations 对象,可以使用 Set 类型操作
     */
    @Bean
    public SetOperations<String, Object> setOperations(RedisTemplate<String, Object> redisTemplate) {

        return redisTemplate.opsForSet();

    }

    /**
     * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
     * @param redisTemplate
     * @return ZSetOperations 对象
     * @description 实例化 ZSetOperations 对象,可以使用 ZSet 类型操作
     */
    @Bean
    public ZSetOperations<String, Object> zSetOperations(RedisTemplate<String, Object> redisTemplate) {

        return redisTemplate.opsForZSet();

    }
}
