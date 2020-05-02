package com.xantrix.webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ItemsWebServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItemsWebServiceApplication.class, args);
    }

}
