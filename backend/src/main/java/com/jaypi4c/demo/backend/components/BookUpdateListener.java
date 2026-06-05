package com.jaypi4c.demo.backend.components;

import com.jaypi4c.demo.backend.registry.SseEmitterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BookUpdateListener implements MessageListener {
    private final SseEmitterRegistry sseEmitterRegistry;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String payload = message.toString();

        int divider = payload.indexOf("|");
        String job = payload.substring(0, divider);
        String msg = payload.substring(divider + 1);// skip "|"

        UUID jobId = UUID.fromString(job);

        sseEmitterRegistry.get(jobId).ifPresent(emitter -> {
            try {
                emitter.send(msg);
            } catch (Exception e) {
                emitter.complete();
            }
        });
    }
}
