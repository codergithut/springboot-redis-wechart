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

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;


/**
 * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
 * @version 1.0, 2017/4/24
 * @description websocket 实现页面数据监听， 当前类继承了ChannelAwareMessageListener会主动监听消息队列端口
 */
@ServerEndpoint(value = "/websocket")
@Component
public class MyWebSocket implements CommonValue, ChannelAwareMessageListener {

    /**
     * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
     * @description 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
     */
    private static int onlineCount = 0;

    /**
     * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
     * @description concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     */
    private static CopyOnWriteArraySet<MyWebSocket> webSocketSet = new CopyOnWriteArraySet<MyWebSocket>();


    /**
     * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
     * @description 为了实现同一个用户不同客户端消息收发
     */
    private CopyOnWriteArrayList<MyWebSocket> webSockets = new CopyOnWriteArrayList<MyWebSocket>();

    /**
     * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
     * @description 日志记录
     */
    private Logger logger =  LoggerFactory.getLogger(this.getClass());



    /**
     * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
     * @description 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
     * @description 用户id
     */
    private String id;

    /**
     * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
     * @description 用户验证模块
     */
    UserServiceImpl userServiceImpl;

    /**
     * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
     * @description redis set操作实例
     */
    SetOperations setOperations;

    /**
     * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
     * @description redis hash操作实例
     */
    HashOperations hashOperations;

    /**
     * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
     * @description RabbitMq消息发送实例
     */
    SendMessage sendMessage;

    /**
     * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
     * @description RabbitMq接受消息实例
     */
    ReceivedMessage receivedMessage;

    /**
     * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
     * @description 最后发送消息的内容，防止消息重复发送
     */
    String lastSendMessage;

    /**
     * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
     * @param
     * @return void
     * @description 初始化需要的服务，通过BeanUtils获取实例
     */
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
     * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
     * @param session 客户端连接的会话
     * @return void
     * @description 用户尝试链接到服务器初始化方法
     */
    @OnOpen
    public void onOpen(Session session) throws Exception {

        logger.info("有用户上线了");
        init();
        this.session = session;

        /**
         * 获取用户token信息来验证用户身份，获取用户的ID
         */
        String token = session.getRequestParameterMap().get(TOKEN).toString();
        token = token.substring(1, token.length() - 1);
        String id = (String) hashOperations.get(LOGININFO, token);
        addOnlineCount();

        /**
         * 如果用户ID不为null
         */
        if (id != null) {
            this.id = id;

            /**
             * 查看持有链接中是否已经有和当前用户相同ID的对话，
             * 如果有相互引用实现各个不同的客户端通信一致
             */
            for(MyWebSocket socket : webSocketSet) {
                if(socket.getId().equals(id)) {
                    socket.getWebSockets().add(this);
                    this.getWebSockets().add(socket);
                }
            }

            /**
             * 将当前对象放入websoket管理对象中
             */
            webSocketSet.add(this);

            System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());


            try {

                /**
                 * 初始化好友信息
                 */
                Set<String> friends = setOperations.members(this.id);
                Map<String, Object> friendsData = new HashMap<String, Object>();
                friendsData.put("type", "friendsinfo");
                friendsData.put("Data", friends);

                /**
                 * 初始化用户信息
                 */
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
         * 获取和当前用户ID一致的消息队列，并监听该队列。
         */
        Queue queue = new Queue(id);

        Queue[] queues = new Queue[]{queue};

        receivedMessage.messageContainer(queues, this).start();


    }



    /**
     * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
     * @param
     * @return void
     * @description 链接关闭调用该方法，需要对用户的各种信息进行清理，比如用户登录记录等等，需要判断是用户关闭还是验证未通过关闭
     */
    @OnClose
    public void onClose() {
        clearData();
    }


    /**
     * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
     * @param recMessage 客户端发送的消息
     * @param session 对话基本信息
     * @return void
     * @description 收到客户端消息后调用的方法，将消息发送到需要发送的好友队列中去
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
     * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
     * @param message 用户发送的消息报文主体
     * @param receivedId 接收方用户ID
     * @return void
     * @description 将消息保存到redis内存服务中，定期刷新到mongodb数据库中
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
     * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
     * @param jsStr 发送消息的报文
     * @param receiveId 对端的ID（可以是好友，也可能是某个群） 需要使用exchange属性控制
     * @return void
     * @description 将消息发送到特定的队列中去
     */
    public void sendUserInfo(String jsStr, String receiveId) throws IOException {

        sendMessage.sendExchangeMsg(null, receiveId, jsStr);

    }


    /**
     * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
     * @param session 对话信息
     * @param error 异常信息
     * @return void
     * @description websocket调用异常处理
     */
    @OnError
    public void onError(Session session, Throwable error) {
        clearData();
        System.out.println("发生错误");
        error.printStackTrace();
    }


    /**
     * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
     * @param message 需要发送的消息
     * @return void
     * @description 将消息发到自己和与自己有相同用户ID的会话中
     */
    public void sendMessage(String message) throws IOException {
        /**
         * 判断该消息该对话是否已发过，如果发过就直接跳过
         */
        if(message.equals(lastSendMessage)) {
            return ;
        }

        System.out.println("会话对象" + this.session.getBasicRemote().toString());

        System.out.println("会话对象HashCode" + this.session.toString());

        this.session.getBasicRemote().sendText(message);

        /**
         * 跟新最新已发送消息
         */
        lastSendMessage = message;
//     this.session.getAsyncRemote().sendText(message);
    }


    /**
     * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
     * @param message 对所有在线的用户发送一条消息内容
     * @return void
     * @description 系统发送给在线用户的系统推送消息
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
     * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
     * @param message 接受到的消息内容
     * @param channel 信道
     * @return void
     * @description 监听消息的方法回调
     */
    @Override
    public void onMessage(Message message, Channel channel) {

        try {

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

            /**
             * 如果消息正常发送没啥问题如果在消息未能全部成功消费返回到队列中让其他客户端消费（消息部分消费）
             */
            basicAck(message, channel);

        } catch (Exception e) {

            failBasicAck(message, channel);

        }

    }

    /**
     * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
     * @param message 需要确认的消息
     * @return void
     * @description 确认已接受到的消息
     */
    private void basicAck(Message message, Channel channel) {
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), true); //确认消息成功消费
        } catch (IOException e) {
            logger.error("---------basicAck.IOException in!---------------");
        }
    }

    /**
     * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
     * @param message 需要确认的消息
     * @param channel 信道
     * @return void
     * @description 消息消费异常
     */
    private void failBasicAck(Message message, Channel channel) {
        try {
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), true, true); //消息消费失败
        } catch (IOException e) {
            logger.error("---------basicAck.IOException in!---------------");
        }
    }

    /**
     * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
     * @param
     * @return void
     * @description 清除websocket注册信息
     */
    private void clearData() {

        /**
         * 这个服务拒绝提供监听服务
         */
        receivedMessage.getContainer().stop();

        /**
         * 从各种注册的地方消除该服务注册记录
         */
        for(MyWebSocket socket : webSocketSet) {
            if(socket.getId().equals(id)) {
                socket.getWebSockets().remove(this);
            }
        }

        /**
         * 清除自己持有的对象
         */
        webSockets.clear();
        subOnlineCount();

        /**
         * 所有服务列表中去除
         */
        webSocketSet.remove(this);
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }
}