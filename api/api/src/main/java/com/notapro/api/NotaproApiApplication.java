package com.notapro.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class NotaproApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotaproApiApplication.class, args);
    }

}
