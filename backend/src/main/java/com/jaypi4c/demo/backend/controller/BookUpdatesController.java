package com.jaypi4c.demo.backend.controller;

import com.jaypi4c.demo.backend.config.RabbitConfig;
import com.jaypi4c.demo.backend.config.RedisPublisher;
import com.jaypi4c.demo.worker.dto.Worker;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor
public class BookUpdatesController {
    private final RedisMessageListenerContainer listenerContainer;
    private final RedisPublisher redisPublisher;

    @RabbitListener(queues = RabbitConfig.RESULTS_QUEUE)
    public void processMessage(Worker.Response response) {
        redisPublisher.publishBookUpdate(response.getBookname(), "COMPLETED");
    }
    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);
    @GetMapping(value = "/api/books/updates", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamUpdates() throws IOException {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE); // set timeout to be indefinite
        emitter.send(SseEmitter.event().comment("connected")); // send initial connected event for the headers to be sent
        MessageListener listener = (message, pattern) -> {
            try {
                emitter.send(message.toString());
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        };
        ChannelTopic topic = new ChannelTopic("book-updates");
        listenerContainer.addMessageListener(listener, topic);


        // Send heartbeat every 30 seconds
        ScheduledFuture<?> heartbeatTask =
                scheduler.scheduleAtFixedRate(() -> {
                    try {
                        emitter.send(
                                SseEmitter.event()
                                        .comment("heartbeat")
                        );
                    } catch (Exception e) {
                        emitter.completeWithError(e);
                    }
                }, 30, 30, TimeUnit.SECONDS);

        Runnable cleanup = () -> {
            heartbeatTask.cancel(true);
            listenerContainer.removeMessageListener(listener, topic);
        };

        // Clean up when emitter completes or times out
        emitter.onCompletion(() -> cleanup.run());
        emitter.onTimeout(() -> {
    cleanup.run();
    emitter.complete();
        });
        return emitter;
    }
}