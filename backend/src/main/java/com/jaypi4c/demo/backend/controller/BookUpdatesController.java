package com.jaypi4c.demo.backend.controller;

import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@RestController
public class BookUpdatesController {
    private final RedisMessageListenerContainer listenerContainer;

    public BookUpdatesController(RedisMessageListenerContainer listenerContainer) {
        this.listenerContainer = listenerContainer;
    }

    @GetMapping(value="/api/books/updates", produces= MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamUpdates() {
        SseEmitter emitter = new SseEmitter();
        MessageListener listener = (message, pattern) -> {
            try {
                emitter.send(message.toString());
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        };
        ChannelTopic topic = new ChannelTopic("book-updates");
        listenerContainer.addMessageListener(listener, topic);

        // Clean up when emitter completes or times out
        emitter.onCompletion(() -> listenerContainer.removeMessageListener(listener, topic));
        emitter.onTimeout(() -> {
            listenerContainer.removeMessageListener(listener, topic);
            emitter.complete();
        });
        return emitter;
    }
}