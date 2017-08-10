package wechart.rabbitmq.config;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;

/**
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2017/8/10
 * @description
 */

@Configurable
public class RabbitMqConfig {

    @Bean("connectionFactoryClient")
    public ConnectionFactory getConnectionFactoryClient() {
        ConnectionFactory factory = new ConnectionFactory();
        //设置RabbitMQ相关信息
        factory.setHost("10.1.1.153");
        factory.setUsername("tianjian");
        factory.setPassword("tianjian");
        factory.setPort(5672);
        factory.setVirtualHost("service");
        return factory;
    }

    @Bean("connectionFactoryCore")
    public org.springframework.amqp.rabbit.connection.ConnectionFactory getConnectionFactoryCore() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses("10.1.1.153:5672");
        connectionFactory.setUsername("tianjian");
        connectionFactory.setPassword("tianjian");
        connectionFactory.setVirtualHost("service");
        connectionFactory.setPublisherConfirms(true); //必须要设置
        return connectionFactory;
    }
}
