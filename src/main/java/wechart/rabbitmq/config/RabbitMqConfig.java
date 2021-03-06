package wechart.rabbitmq.config;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;

/**
 * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
 * @version 1.0, 2017/8/10
 * @description
 */

@Configurable
public class RabbitMqConfig {

    @Bean("connectionFactoryClient")
    public ConnectionFactory getConnectionFactoryClient() {

        ConnectionFactory factory = new ConnectionFactory();

        //设置RabbitMQ相关信息
        factory.setHost("127.0.0.1");
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setPort(5672);
        factory.setVirtualHost("/");

        return factory;

    }

    @Bean("connectionFactoryCore")
    public org.springframework.amqp.rabbit.connection.ConnectionFactory getConnectionFactoryCore() {

        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();

        connectionFactory.setAddresses("127.0.0.1:5672");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("/");

        connectionFactory.setPublisherConfirms(true); //必须要设置

        return connectionFactory;

    }
}
