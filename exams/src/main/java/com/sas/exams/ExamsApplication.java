package com.sas.exams;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ExamsApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExamsApplication.class, args);
    }
}
