package com.jensonjo.mqpublisher;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Created by jensonkakkattil on May, 2018
 */

@Component
public class Runner implements CommandLineRunner {

    private final RabbitTemplate rabbitTemplate;

    public Runner(RabbitTemplate rabbitTemplate) {

        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        for (int count = 0; count > -1; count++) {
            System.out.println("Sending message..." + count);
            rabbitTemplate.convertAndSend(PublisherConfig.topicExchangeName, PublisherConfig.key, "Hello from RabbitMQ!" + count);
            Thread.sleep(2000);
        }

    }

}
