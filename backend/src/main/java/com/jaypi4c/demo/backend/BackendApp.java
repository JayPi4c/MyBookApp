package com.jaypi4c.demo.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class BackendApp {
    static void main(String[] args) {
        SpringApplication.run(BackendApp.class, args);
    }
}
