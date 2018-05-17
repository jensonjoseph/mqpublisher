package com.jensonjo.mqpublisher;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class MqpublisherApplication {

	@Autowired
	private Environment environment;

	@Autowired
	@Qualifier("CustomRabbitConnectionFactory")
	private ConnectionFactory customRabbitConnectionFactory;

	static final String topicExchangeName = "spring-boot-exchange";

	static final String queueName = "spring-boot";

	static final String key = "spring-boot-key";

	@Bean
	Queue queue() {
		return new Queue(queueName, false);
	}

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

	@Bean
	Binding binding(Queue queue, TopicExchange exchange) {
		//return BindingBuilder.bind(queue).to(exchange).with("foo.bar.#");
		Binding binding = new Binding(queueName, Binding.DestinationType.QUEUE, topicExchangeName, key, null);
		binding.setAdminsThatShouldDeclare(rabbitAdmin());
		return binding;
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

	public static void main(String[] args) {
		SpringApplication.run(MqpublisherApplication.class, args);
	}
}
