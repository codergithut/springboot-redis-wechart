package wechart.socket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.context.annotation.ComponentScan;
import redis.clients.jedis.Jedis;
import wechart.config.CommonValue;
import wechart.model.User;
import wechart.model.WebSocketMessage;
import wechart.service.impl.UserServiceImpl;
import wechart.util.BeanUtils;
import wechart.util.StringSort;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;


/**
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2017/4/24
 * @description
 */
@ComponentScan
@ServerEndpoint(value = "/websocket")
public class MyWebSocket implements CommonValue {

    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<MyWebSocket> webSocketSet = new CopyOnWriteArraySet<MyWebSocket>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    private String userid;

    Jedis write = BeanUtils.getBean(JEDIS);

    UserServiceImpl userServiceImpl = BeanUtils.getBean("userServiceImpl");


    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(Session session) throws Exception {
        this.session = session;

        String token = session.getRequestParameterMap().get(TOKEN).toString();
        String id = null;
        List<String> val = write.hmget(LOGININFO, token);
        if(val != null && val.size() == 1) {
            id = val.get(0);
        }
        //加入set中
        //在线数加1
        addOnlineCount();
        if(id != null){
            userid = id;
            webSocketSet.add(this);
            System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
            try {
                Set<String> friends = write.smembers(userid);
                Map<String,Object> friendsData = new HashMap<String,Object>();
                friendsData.put("type", "friendsinfo");
                friendsData.put("Data", friends);

                User user = userServiceImpl.get(userid);
                Map<String,Object> userData = new HashMap<String,Object>();
                userData.put("type", "userinfo");
                userData.put("Data", user);
                /**
                 * 返回好友信息
                 */
                sendMessage(JSON.toJSONString(friendsData));

                /**
                 * 返回用户信息
                 */
                sendMessage(JSON.toJSONString(userData));

                //获取离线消息
                Set<String> allinfo = write.smembers(WAITERECEIVEMESSAGE + userid);
                write.srem(WAITERECEIVEMESSAGE + userid);

                for(String s : allinfo) {
                    WebSocketMessage message = JSON.parseObject(s, WebSocketMessage.class);
                    /**
                     * 先将文件存放到redis中
                     */
                    saveHistoryContent(s, message.getReceivedId());
                }
            } catch (IOException e) {
                System.out.println("IO异常");
            }
        } else {
            /**
             * 通过抛出异常触发管理器对容器中的session进行清理
             * 不是很友好
             */
            onClose();
            throw new Exception("this is unsave session");
        }

    }


    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param recMessage 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String recMessage, Session session) throws IOException {
        //todo message需要给我特定的说明比如 添加好友addfriend@+消息体 聊天就是talk@+消息体 群聊天就是allTalk@+消息体  等等

        WebSocketMessage message = JSON.parseObject(recMessage, WebSocketMessage.class);

        if(message.getType().equals("TALK")) {
            sendUserInfo(message);
        }




        JSONObject jsStr = JSONObject.parseObject(recMessage);
        String type = jsStr.get("type").toString();
        if(type.equals("talk")) {
            sendUserInfo(jsStr);
        }

        if(type.equals("addFriend")) {
            sendAddFriendRequest(jsStr);
        }

        if(type.equals("agreeFriend")) {
            sendResultFrendRequest(jsStr);
        }

        if(type.equals("recFriend")) {

        }

    }

    private void sendUserInfo(WebSocketMessage message) throws IOException {

        boolean flag = false;

        String messageInfo = JSON.toJSONString(message);

        for(MyWebSocket item : webSocketSet) {
            if(item.getUserid() != null && item.getUserid().equals(message.getReceivedId())) {
                item.sendMessage(messageInfo);
                flag = true;
                saveHistoryContent(messageInfo, message.getReceivedId());

            }
        }

        if(!flag) {
            write.sadd(WAITERECEIVEMESSAGE + message.getReceivedId(), messageInfo);
        }
    }


    private void saveHistoryContent(String message, String receivedId) {
        String talkKey = StringSort.getKeyBySort(new String[]{userid, receivedId});
        if(!write.sismember(HISTORYCONTENT, talkKey)){
            write.sadd(HISTORYCONTENT, talkKey);
        }
        write.sadd(talkKey, message);
    }


    private void sendResultFrendRequest(JSONObject jsStr) throws IOException {
        String toid = jsStr.get("account").toString();
        String result = jsStr.get("result").toString();
        String id = jsStr.get("id").toString();
        if(result.equals("agree")) {

            write.sadd(userid, toid);
            write.sadd(toid, userid);

            for(MyWebSocket item : webSocketSet) {
                if(item.getUserid() != null && item.getUserid().equals(toid)) {

                    Set<String> friends = write.smembers(toid);
                    Map<String,Object> friendsData = new HashMap<String,Object>();
                    friendsData.put("type", "friendsinfo");
                    friendsData.put("Data", friends);
                    item.sendMessage(JSON.toJSONString(friendsData));

                    Map<String,String> send = new HashMap<String,String>();
                    send.put("type", "agree");
                    send.put("friendid", id);
                    item.sendMessage(JSON.toJSONString(send));
                }
            }

            for(MyWebSocket item : webSocketSet) {
                if(item.getUserid() != null && item.getUserid().equals(id)) {
                    Set<String> friends = write.smembers(id);
                    Map<String,Object> friendsData = new HashMap<String,Object>();
                    friendsData.put("type", "friendsinfo");
                    friendsData.put("Data", friends);
                    item.sendMessage(JSON.toJSONString(friendsData));
                }
            }
        }
    }

    private void sendAddFriendRequest(JSONObject jsStr) throws IOException {
        String account = jsStr.get("account").toString();
        String id = jsStr.get("id").toString();
        for(MyWebSocket item : webSocketSet) {
            if(item.getUserid() != null && item.getUserid().equals(account)) {
                Map<String,Object> data = new HashMap<String,Object>();
                User user = userServiceImpl.get(id);
                data.put("type", "friendRequest");
                data.put("user",user);
                item.sendMessage(JSON.toJSONString(data));
            }
        }
    }


    public void sendUserInfo(JSONObject jsStr) throws IOException {
        /**
         * 如果用户在线，发送消息到用户，并记录到mongodb如果用户不在线，记录到redis，当用户上线推送数据并记录到mngodb数据库中
         */
        String toid = jsStr.get("otherId").toString();
        boolean flag = false;
        for(MyWebSocket item : webSocketSet) {
            if(item.getUserid() != null && item.getUserid().equals(toid)) {
                item.sendMessage(jsStr.toJSONString());
                flag = true;
            }
        }

        if(!flag) {
            //todo 记录消息
        }
    }







    /**
     * 发生错误时调用
     @OnError
     */
     public void onError(Session session, Throwable error) {
     System.out.println("发生错误");
     error.printStackTrace();
     }


     public void sendMessage(String message) throws IOException {
     this.session.getBasicRemote().sendText(message);
     //this.session.getAsyncRemote().sendText(message);
     }


     /**
      * 群发自定义消息
      * */
    public static void sendAllInfo(String message) throws IOException {
        for (MyWebSocket item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                continue;
            }
        }
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        MyWebSocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        MyWebSocket.onlineCount--;
    }

}