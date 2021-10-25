package com.example.onlinelearning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class OnlineLearningApplication {
    public static void main(String[] args) {
        SpringApplication.run(OnlineLearningApplication.class, args);
    }

}
