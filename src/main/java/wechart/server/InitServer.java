package wechart.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import wechart.config.CommonValue;
import wechart.interceptor.MyInterceptor;
import wechart.socket.MyWebSocket;

import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2017/8/7
 * @description
 */
//@Component
public class InitServer implements CommandLineRunner, CommonValue {

    @Autowired
    @Qualifier(SUBJEDIS)
    Jedis redis;

    /**
     * 启动订阅功能实现内部路由
     * @param args
     */
    @Override
    public void run(String... args) {
//        CopyOnWriteArraySet<MyWebSocket> sockets = MyWebSocket.getWebSocketSet();
//        for(MyWebSocket socket : sockets) {
//            redis.subscribe(socket, "32");
//        }
    }

}