package com.kuymakov.socialmedia.subscriptions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.mongodb.config.EnableMongoAuditing;


@SpringBootApplication
@EnableEurekaClient
@EnableMongoAuditing
public class SubscriptionsServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SubscriptionsServiceApplication.class, args);
    }
}
