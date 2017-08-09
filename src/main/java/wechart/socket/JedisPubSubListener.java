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
public class JedisPubSubListener extends JedisPubSub {

    MyWebSocket myWebSocket;

    boolean flag = true;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @Autowired
    @Qualifier("SUBJEDIS")
    Jedis jedis;

    List<JedisPubSubListener> listeners = new ArrayList<JedisPubSubListener>();

    public void add(JedisPubSubListener jedisPubSubListener) {
        listeners.add(jedisPubSubListener);
    }


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
        for(JedisPubSubListener listener : listeners) {
            if(listener.isFlag()) {
                jedis.subscribe(this, myWebSocket.getId());
            }
            listener.setFlag(false);
        }

    }
}
