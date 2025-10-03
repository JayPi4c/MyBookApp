package com.jaypi4c.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class App {
    static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
