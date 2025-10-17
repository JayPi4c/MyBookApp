package com.jaypi4c.demo.backend.config;

import com.google.protobuf.Parser;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProtobufMessageConverter implements MessageConverter {

    private static final String CONTENT_TYPE = "application/x-protobuf";

    @Nonnull
    @Override
    public Message toMessage(@Nonnull Object object, @Nonnull MessageProperties messageProperties) throws MessageConversionException {
        if (!(object instanceof com.google.protobuf.Message protobuf)) {
            throw new MessageConversionException("Only protobuf Message instances are supported");
        }
        byte[] body = protobuf.toByteArray();
        messageProperties.setContentType(CONTENT_TYPE);
        messageProperties.setContentEncoding("binary");
        messageProperties.setHeader("__protobuf_type", object.getClass().getName());
        log.info("Converting protobuf message: {}", object.getClass().getName());
        return new Message(body, messageProperties);
    }

    @Nonnull
    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        // The caller must know which protobuf type to expect.
        // We store the fully‑qualified class name in a header (optional but handy).
        String className = (String) message.getMessageProperties().getHeaders().get("__protobuf_type");
        log.info("Deserializing message of type: {}", className);
        if (className == null) {
            throw new MessageConversionException("Missing __protobuf_type header");
        }
        try {
            Class<?> clazz = Class.forName(className);
            // All generated protobuf messages have a static parser() method.
            Parser<?> parser = (Parser<?>) clazz.getMethod("parser").invoke(null);
            return parser.parseFrom(message.getBody());
        } catch (Exception e) {
            throw new MessageConversionException("Failed to deserialize protobuf message", e);
        }
    }
}