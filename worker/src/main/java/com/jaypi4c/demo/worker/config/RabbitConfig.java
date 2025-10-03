package com.jaypi4c.demo.worker.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String QUEUE_NAME = "ocr-jobs";

    @Bean
    public Queue ocrQueue() {
        return new Queue(QUEUE_NAME, true);
    }
}