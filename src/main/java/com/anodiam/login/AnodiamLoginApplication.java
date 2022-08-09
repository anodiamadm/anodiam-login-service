package com.anodiam.login;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class AnodiamLoginApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnodiamLoginApplication.class, args);
    }
}
