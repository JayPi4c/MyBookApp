package com.jaypi4c.demo.worker.service;

import com.jaypi4c.demo.worker.config.RabbitConfig;
import com.jaypi4c.demo.worker.config.RedisPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OcrJobConsumer {
    private final RedisPublisher redisPublisher;

    @RabbitListener(queues = RabbitConfig.QUEUE_NAME)
    public void receiveOCRJob(String bookName) throws InterruptedException {

        System.out.println("Processing " + bookName);

        Thread.sleep(2000); // Simulate time taken to process the job

        System.out.println("Finished processing " + bookName);

        redisPublisher.publishBookUpdate(bookName, "COMPLETED");

    }
}