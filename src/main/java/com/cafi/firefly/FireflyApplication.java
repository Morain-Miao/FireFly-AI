package com.cafi.firefly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class FireflyApplication {

    public static void main(String[] args) {
        SpringApplication.run(FireflyApplication.class, args);
    }

}
