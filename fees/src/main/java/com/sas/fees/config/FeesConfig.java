package com.sas.fees.config;

import lombok.Getter;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class FeesConfig {

    @Value("${rabbitmq.exchanges.internal}")
    private String internalExchange;

    @Value("${rabbitmq.queues.fees}")
    private String feesQueue;

    @Value("${rabbitmq.routing-keys.internal-fees}")
    private String internalFeesRoutingKey;

    @Value("${rabbitmq.queues.grade-term-fees}")
    private String gradeTermFeesQueue;

    @Value("${rabbitmq.routing-keys.internal-grade-term-fees}")
    private String internalGradeTermFeesRoutingKey;

    @Bean
    public TopicExchange internalTopicExchange() {
        return new TopicExchange(this.internalExchange);
    }

    @Bean
    public Queue feesQueue() {
        return new Queue(this.feesQueue);
    }

    @Bean
    public Binding internalToFeesBinding() {
        return BindingBuilder
                .bind(feesQueue())
                .to(internalTopicExchange())
                .with(this.internalFeesRoutingKey);
    }

    @Bean
    public Queue gradeTermFeesQueue() {
        return new Queue(this.gradeTermFeesQueue);
    }

    @Bean
    public Binding internalToGradeTermFeesBinding() {
        return BindingBuilder
                .bind(gradeTermFeesQueue())
                .to(internalTopicExchange())
                .with(this.internalGradeTermFeesRoutingKey);
    }
}