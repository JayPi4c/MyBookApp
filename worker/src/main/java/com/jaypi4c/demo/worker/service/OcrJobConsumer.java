package com.jaypi4c.demo.worker.service;

import com.jaypi4c.demo.worker.config.RabbitConfig;
import com.jaypi4c.demo.worker.dto.HelloProto;
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
    public void receiveOCRJob(HelloProto.HelloRequest request) throws InterruptedException {

        log.info("Processing: {}", request);

        Thread.sleep(2000); // Simulate time taken to process the job

        log.info("Finished processing: {}", request);

        rabbitTemplate.convertAndSend(RabbitConfig.RESULTS_QUEUE, HelloProto.HelloResponse.newBuilder().setGreeting("Processed: " + request.getName()).build());
    }
}