package com.sas.fees;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@SpringBootApplication(
        scanBasePackages = {
                "com.sas.fees",
                "com.sas.amqp",
                "com.sas.exception",
        }
)
@EnableDiscoveryClient
@EnableFeignClients(
        basePackages = "com.sas.clients"
)
@PropertySources({
        @PropertySource("classpath:clients-${spring.profiles.active}.properties"),
        @PropertySource("classpath:exception.properties"),
        @PropertySource("classpath:amqp.properties")
})
public class FeesApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeesApplication.class, args);
    }

}
