package com.ebi.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class EbiPersonServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbiPersonServiceApplication.class, args);
    }

}
