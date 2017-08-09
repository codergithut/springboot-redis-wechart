package wechart.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import wechart.config.CommonValue;
import wechart.interceptor.MyInterceptor;
import wechart.socket.JedisPubSubListener;
import wechart.socket.MyWebSocket;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2017/8/7
 * @description
 */
@Component
@Service
public class InitServer implements CommandLineRunner, CommonValue {

    @Autowired
    @Qualifier(SUBJEDIS)
    Jedis redis;

    @Autowired
    JedisPubSubListener jedisPubSubListener;

    @Autowired
    Locked locked;

    /**
     * 启动订阅功能实现内部路由
     * @param args
     */
    @Override
    public void run(String... args) {
        boolean flag = true;
        while(flag) {
            System.out.println(locked.isLocked());
            if(locked.isLocked()) {
                if(jedisPubSubListener.getListeners().size() > 0) {
                    jedisPubSubListener.listen();
                    locked.setLocked(false);
                }

            }

        }

    }

}