package de.jaypi4c.demo.backend.config;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisPublisher {
    private final StringRedisTemplate template;

    public RedisPublisher(StringRedisTemplate template) {
        this.template = template;
    }

    public void publishBookUpdate(String jobId, String bookName, String status) {
        // TODO fix sneaky hack to prepend jobId via "clientId|" -> use proper template
        String msg = String.format("%s|{\"bookId\":\"%s\",\"status\":\"%s\"}", jobId, bookName, status);
        template.convertAndSend("book-updates", msg);
    }
}