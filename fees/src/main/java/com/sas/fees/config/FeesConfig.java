package com.sas.fees.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeesConfig {

    @Value("${rabbitmq.exchanges.internal}")
    private String internalExchange;

    @Value("${rabbitmq.queues.fees}")
    private String feesQueue;

    @Value("${rabbitmq.routing-keys.internal-fees}")
    private String internalFeesRoutingKey;

    @Bean
    public TopicExchange internalTopicExchange() {
        return new TopicExchange(this.internalExchange);
    }

    @Bean
    public Queue feesQueue() {
        return new Queue(this.feesQueue);
    }

    @Bean
    public Binding internalToNotificationBinding() {
        return BindingBuilder
                .bind(feesQueue())
                .to(internalTopicExchange())
                .with(this.internalFeesRoutingKey);
    }


    public String getInternalExchange() {
        return internalExchange;
    }

    public String getFeesQueue() {
        return feesQueue;
    }

    public String getInternalFeesRoutingKey() {
        return internalFeesRoutingKey;
    }
}