package wechart.rabbitmq.service.receive;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
 * @version 1.0, 2017/8/10
 * @description 消息接受监听
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ReceivedMessage {

    @Autowired
    ConnectionFactory connectionFactory;

    SimpleMessageListenerContainer container;

    public SimpleMessageListenerContainer messageContainer(Queue[] queueList, ChannelAwareMessageListener listener) {

        container = new SimpleMessageListenerContainer(connectionFactory);
        container.setQueues(queueList);
        container.setMessageListener(listener);


        return container;
    }

    public SimpleMessageListenerContainer getContainer() {
        return container;
    }

    public void setContainer(SimpleMessageListenerContainer container) {
        this.container = container;
    }
}
