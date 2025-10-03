package com.jaypi4c.demo.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Data
@ConfigurationProperties(prefix = "demo.security")
public class SecurityProperties {

    /**
     * Allowed origins for CORS.
     */
    private List<String> allowedOrigins = new ArrayList<>();
}
