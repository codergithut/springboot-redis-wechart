package wechart.rabbitmq.service;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2017/8/10
 * @description
 */
@Service
public class RabbitService {

    @Autowired
    ConnectionFactory connectionFactory;

    public void createExchange(String exchange, String type) throws IOException, TimeoutException {
        //创建一个新的连接
        Connection connection = connectionFactory.newConnection();
        //创建一个通道
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(exchange, type);
        channel.close();

    }

    public void createQueue(String queueName) throws IOException, TimeoutException {

        //创建一个新的连接
        Connection connection = connectionFactory.newConnection();
        //创建一个通道
        Channel channel = connection.createChannel();
        channel.queueDeclare(queueName, false, false, false, null);
        channel.close();

    }

    public void bindingQueue(String exchange, String queueName, String routeKey) throws IOException, TimeoutException {
        //创建一个新的连接
        Connection connection = connectionFactory.newConnection();
        //创建一个通道
        Channel channel = connection.createChannel();
        channel.queueBind(queueName, exchange, routeKey);
        channel.close();
    }

}
