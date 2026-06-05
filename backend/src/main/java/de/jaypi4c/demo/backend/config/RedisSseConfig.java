package de.jaypi4c.demo.backend.config;

import de.jaypi4c.demo.backend.components.BookUpdateListener;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisSseConfig {

    private final RedisMessageListenerContainer listenerContainer;
    private final BookUpdateListener bookUpdateListener;

    @PostConstruct
    public void register() {
        ChannelTopic topic = new ChannelTopic("book-updates");
        listenerContainer.addMessageListener(bookUpdateListener, topic);
    }
}
