package wechart.socket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Component;
import wechart.config.CommonValue;
import wechart.model.User;
import wechart.rabbitmq.service.receive.ReceivedMessage;
import wechart.rabbitmq.service.send.SendMessage;
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
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;


/**
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2017/4/24
 * @description
 */
@ServerEndpoint(value = "/websocket")
@Component
public class MyWebSocket implements CommonValue, ChannelAwareMessageListener {

    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<MyWebSocket> webSocketSet = new CopyOnWriteArraySet<MyWebSocket>();

    /**
     * 为了实现同一个用户不同客户端消息收发
     */
    private CopyOnWriteArrayList<MyWebSocket> webSockets = new CopyOnWriteArrayList<MyWebSocket>();

    private Logger logger =  LoggerFactory.getLogger(this.getClass());



    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    private String id;

    UserServiceImpl userServiceImpl;

    SetOperations setOperations;

    HashOperations hashOperations;

    SendMessage sendMessage;

    ReceivedMessage receivedMessage;

    private void init() {

        setOperations = BeanUtils.getBean("setOperations");

        hashOperations = BeanUtils.getBean("hashOperations");

        userServiceImpl = BeanUtils.getBean("userServiceImpl");

        receivedMessage = BeanUtils.getBean("receivedMessage");

        sendMessage = BeanUtils.getBean("sendMessage");
    }


    public  CopyOnWriteArrayList<MyWebSocket> getWebSockets() {
        return webSockets;
    }

    public  void setWebSockets(CopyOnWriteArrayList<MyWebSocket> webSockets) {
        this.webSockets = webSockets;
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) throws Exception {

        logger.info("有用户上线了");
        init();
        this.session = session;
        String token = session.getRequestParameterMap().get(TOKEN).toString();
        token = token.substring(1, token.length() - 1);
        String id = (String) hashOperations.get(LOGININFO, token);
        //加入set中
        //在线数加1
        addOnlineCount();
        if (id != null) {
            this.id = id;

            for(MyWebSocket socket : webSocketSet) {
                if(socket.getId().equals(id)) {
                    socket.getWebSockets().add(this);
                    this.getWebSockets().add(socket);
                }
            }

            webSocketSet.add(this);

            System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
            try {
                Set<String> friends = setOperations.members(this.id);
                Map<String, Object> friendsData = new HashMap<String, Object>();
                friendsData.put("type", "friendsinfo");
                friendsData.put("Data", friends);

                User user = userServiceImpl.get(this.id);
                Map<String, Object> userData = new HashMap<String, Object>();
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

        /**
         * 订阅会歇逼 需要异步处理
         *
         */

        Queue queue = new Queue(id);

        Queue[] queues = new Queue[]{queue};

        receivedMessage.messageContainer(queues, this).start();


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
     * 先将消息备份存入到redis中然后将消息发送到消息中间件中，为了实现消息快速路由和离线消息保存
     * @param recMessage 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String recMessage, Session session) throws IOException {
        //todo message需要给我特定的说明比如 添加好友addfriend@+消息体 聊天就是talk@+消息体 群聊天就是allTalk@+消息体  等等

//        WebSocketMessage message = JSON.parseObject(recMessage, WebSocketMessage.class);
//
//        saveHistoryContent(recMessage, message.getReceivedId());


//        System.out.println("asdfasdfasdfasdf" + recMessage);
//
//        sendUserInfo(recMessage, message.getReceivedId());

        sendUserInfo(recMessage, id);

//        WebSocketMessage message = JSON.parseObject(recMessage, WebSocketMessage.class);
//
//        if(message.getType().equals("TALK")) {
//            sendUserInfo(message);
//        }
//
//
//        JSONObject jsStr = JSONObject.parseObject(recMessage);
//        sendUserInfo(jsStr);
//        String type = jsStr.get("type").toString();
//        if(type.equals("talk")) {
//            sendUserInfo(jsStr);
//        }
//
//        if(type.equals("addFriend")) {
//            sendAddFriendRequest(jsStr);
//        }
//
//        if(type.equals("agreeFriend")) {
//            sendResultFrendRequest(jsStr);
//        }
//
//        if(type.equals("recFriend")) {
//
//        }

    }


    /**
     * @param message 用户发送的消息报文主体
     * @param receivedId 接收方用户ID
     */
    private void saveHistoryContent(String message, String receivedId) {

        /**
         * 发送方ID和接受方ID进行排序，作为无差别备份库的键，暂时存放为redis服务中，定期刷入到mongodb中
         */
        String talkKey = StringSort.getKeyBySort(new String[]{this.id, receivedId});

        if (!setOperations.isMember(HISTORYCONTENT, talkKey)) {

            setOperations.add(HISTORYCONTENT, talkKey);
        }

        setOperations.add(talkKey, message);

    }

    private void sendResultFrendRequest(JSONObject jsStr) throws IOException {
        String toid = jsStr.get("account").toString();
        String result = jsStr.get("result").toString();
        String id = jsStr.get("id").toString();
        if (result.equals("agree")) {

            setOperations.add(this.id, id);
            setOperations.add(id, this.id);

            Set<String> friends = setOperations.members(toid);
            Map<String, Object> friendsData = new HashMap<String, Object>();
            friendsData.put("type", "friendsinfo");
            friendsData.put("Data", friends);

            Map<String, String> send = new HashMap<String, String>();
            send.put("type", "agree");
            send.put("friendid", id);
        }
    }

    private void sendAddFriendRequest(JSONObject jsStr) throws IOException {
        String account = jsStr.get("account").toString();
        String id = jsStr.get("id").toString();
        Map<String, Object> data = new HashMap<String, Object>();
        User user = userServiceImpl.get(id);
        data.put("type", "friendRequest");
        data.put("user", user);
    }


    /**
     *
     * @param jsStr 发送消息的报文
     * @param receiveId 对端的ID（可以是好友，也可能是某个群） 需要使用exchange属性控制
     * @throws IOException
     */
    public void sendUserInfo(String jsStr, String receiveId) throws IOException {

        sendMessage.sendExchangeMsg(null, receiveId, jsStr);

    }


    /**
     * 发生错误时调用
     *
     * @OnError
     */
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }


    public void sendMessage(String message) throws IOException {

        System.out.println("会话对象" + this.session.getBasicRemote().toString());

        System.out.println("会话对象HashCode" + this.session.toString());

        this.session.getBasicRemote().sendText(message);
//     this.session.getAsyncRemote().sendText(message);
    }


    /**
     * 系统通知消息
     */
    public static void sendAllInfo(String message) throws IOException {
        for (MyWebSocket item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                continue;
            }
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public static CopyOnWriteArraySet<MyWebSocket> getWebSocketSet() {
        return webSocketSet;
    }

    public static void setWebSocketSet(CopyOnWriteArraySet<MyWebSocket> webSocketSet) {
        MyWebSocket.webSocketSet = webSocketSet;
    }

    /**
     *
     * @param message 接受到的消息内容
     * @param channel
     * @throws Exception
     * 一旦接受到消息会发给已经登录的同一个用户的客户端
     *
     */
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {

        byte[] body = message.getBody();

        sendMessage(new String(body));

        System.out.println(channel.toString());

        /**
         * 有其它客户端需要同时通知
         */
        if(!webSockets.isEmpty()) {

            for(MyWebSocket socket : webSockets) {

                socket.sendMessage(new String(body));

            }

        }



    }
}