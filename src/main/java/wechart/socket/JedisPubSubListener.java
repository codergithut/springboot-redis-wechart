package wechart.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
@Scope("prototype")
public class JedisPubSubListener extends JedisPubSub {

    MyWebSocket myWebSocket;

    boolean flag = true;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    List<JedisPubSubListener> listeners = new ArrayList<JedisPubSubListener>();

    public void add(JedisPubSubListener jedisPubSubListener) {
        listeners.add(jedisPubSubListener);
    }


    @Override
    public void onMessage(String channel, String message ) {
        try {
            System.out.println("hahahah");
            System.out.println("channel : " + channel + " message : " + message);
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
    public void listen(final JedisPubSubListener jedisPubSubListener) {
        System.out.println("注册ID为：" + myWebSocket.getId());


        new Thread() {
            @Override
            public void  run() {
                final Jedis jedis = new Jedis("192.168.50.210");
                jedis.subscribe(jedisPubSubListener, myWebSocket.getId());
                jedis.close();
            }
        }.start();
    }
}
