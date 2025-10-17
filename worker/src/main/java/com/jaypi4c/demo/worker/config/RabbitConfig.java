package com.jaypi4c.demo.worker.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String JOBS_QUEUE = "ocr-jobs";
    public static final String RESULTS_QUEUE = "ocr-results";

    @Bean
    public Queue jobsQueue() {
        return new Queue(JOBS_QUEUE, true);
    }

    @Bean
    public Queue resultsQueue() {
        return new Queue(RESULTS_QUEUE, true);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory cf, ProtobufMessageConverter protobufConverter) {
        RabbitTemplate template = new RabbitTemplate(cf);
        template.setMessageConverter(protobufConverter);
        return template;
    }
}