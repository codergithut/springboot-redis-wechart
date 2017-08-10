package wechart.rabbitmq.service.receive;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2017/8/10
 * @description
 */
@Service
public class ReceivedMessage {

    @Autowired
    ConnectionFactory connectionFactory;

    public SimpleMessageListenerContainer messageContainer(Queue[] queueList, ChannelAwareMessageListener listener) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setQueues(queueList);
        container.setMessageListener(listener);
        return container;
    }

}
