package com.jensonjo.mqpublisher;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * Created by jensonkakkattil on May, 2018
 */
@Configuration
public class PublisherConfig {
    @Autowired
    private Environment environment;

    @Autowired
    @Qualifier("CustomRabbitConnectionFactory")
    private ConnectionFactory customRabbitConnectionFactory;

    static final String topicExchangeName = "spring-boot-exchange";

    static final String key = "spring-boot-key";

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }

    @Bean
    public RabbitAdmin rabbitAdmin() {
        RabbitAdmin ra= new RabbitAdmin(customRabbitConnectionFactory);
        ra.afterPropertiesSet();
        ra.setAutoStartup(true);
        return ra;
    }

    @Bean(name = "CustomRabbitConnectionFactory")
    public ConnectionFactory rabbitConnectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory(
                environment.getProperty("rabbitmq.host"),
                environment.getProperty("rabbitmq.port", Integer.class)
        );
        factory.setUsername(environment.getProperty("rabbitmq.username"));
        factory.setPassword(environment.getProperty("rabbitmq.password"));
        return factory;
    }
}
