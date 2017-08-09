package wechart.socket;

import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.io.IOException;

/**
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2017/8/9
 * @description
 */
@Service
@Scope("prototype")
public class JedisPubSubListener extends JedisPubSub {

    MyWebSocket myWebSocket;

    public Jedis getJedis() {
        return jedis;
    }

    public void setJedis(Jedis jedis) {
        this.jedis = jedis;
    }

    Jedis jedis;

    @Override
    public void onMessage(String channel, String message ) {
        try {

            System.out.println("hahahah");
            myWebSocket.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public MyWebSocket getMyWebSocket() {
        return myWebSocket;
    }

    public void setMyWebSocket(MyWebSocket myWebSocket) {
        this.myWebSocket = myWebSocket;
    }

    @Async("taskExecutor")
    public void listen() {
        jedis.subscribe(this, myWebSocket.getId());
    }
}
