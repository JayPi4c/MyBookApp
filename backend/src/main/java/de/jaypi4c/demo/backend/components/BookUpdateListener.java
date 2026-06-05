package de.jaypi4c.demo.backend.components;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.jaypi4c.demo.backend.model.BookUpdateMessage;
import de.jaypi4c.demo.backend.registry.SseEmitterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookUpdateListener implements MessageListener {
    private final SseEmitterRegistry sseEmitterRegistry;

    private final ObjectMapper mapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            BookUpdateMessage bookUpdateMessage = mapper.readValue(message.getBody(), BookUpdateMessage.class);
            UUID jobId = bookUpdateMessage.jobId();

            sseEmitterRegistry.get(jobId).ifPresent(emitter -> {
                try {
                    emitter.send(bookUpdateMessage.toJson());
                } catch (Exception e) {
                    emitter.complete();
                }
            });
        } catch (IOException e) {
            log.error("failed", e);
        }
    }
}
