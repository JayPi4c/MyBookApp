package com.jaypi4c.demo.backend.components;

import com.jaypi4c.demo.backend.registry.SseEmitterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@EnableScheduling
public class SseHeartbeat {

    private final SseEmitterRegistry sseEmitterRegistry;

    @Scheduled(fixedRate = 30_000)
    public void heartbeat() {
        for (Map.Entry<UUID, SseEmitter> entry : sseEmitterRegistry.getAll().entrySet()) {
            SseEmitter emitter = entry.getValue();
            try {
                emitter.send(SseEmitter.event().comment("heartbeat"));
            } catch (Exception e) {
                sseEmitterRegistry.remove(entry.getKey());
                emitter.complete();
            }
        }
    }


}
