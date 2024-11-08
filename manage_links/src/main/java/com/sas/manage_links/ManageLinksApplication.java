package com.sas.manage_links;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@SpringBootApplication(
        scanBasePackages = {
                "com.sas.users",
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
public class ManageLinksApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManageLinksApplication.class, args);
    }

}

