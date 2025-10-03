package com.jaypi4c.demo.worker.service;

import com.jaypi4c.demo.worker.config.RabbitConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OcrJobConsumer {
    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = RabbitConfig.JOBS_QUEUE)
    public void receiveOCRJob(String bookName) throws InterruptedException {

        log.info("Processing: {}", bookName);

        Thread.sleep(2000); // Simulate time taken to process the job

        log.info("Finished processing: {}", bookName);

        rabbitTemplate.convertAndSend(RabbitConfig.RESULTS_QUEUE, bookName);
    }
}