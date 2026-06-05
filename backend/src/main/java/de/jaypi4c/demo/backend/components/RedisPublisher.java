package de.jaypi4c.demo.backend.components;

import de.jaypi4c.demo.backend.model.BookUpdateMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RedisPublisher {

    private final RedisTemplate<String, Object> template;

    public void publishBookUpdate(UUID jobId, String bookName, String status) {
        template.convertAndSend("book-updates", new BookUpdateMessage(jobId, bookName, status));
    }
}
