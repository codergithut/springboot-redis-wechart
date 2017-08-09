package wechart.socket;

import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2017/8/9
 * @description
 */
@Service
public class JedisPubSubListener extends JedisPubSub {

    MyWebSocket myWebSocket;

    List<JedisPubSubListener> listeners = new ArrayList<JedisPubSubListener>();

    public Jedis getJedis() {
        return jedis;
    }

    public void setJedis(Jedis jedis) {
        this.jedis = jedis;
    }

    public void add(JedisPubSubListener jedisPubSubListener) {
        listeners.add(jedisPubSubListener);
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

    public List<JedisPubSubListener> getListeners() {
        return listeners;
    }

    public void setListeners(List<JedisPubSubListener> listeners) {
        this.listeners = listeners;
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
