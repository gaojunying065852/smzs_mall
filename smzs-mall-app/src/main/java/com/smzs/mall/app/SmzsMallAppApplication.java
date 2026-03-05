package com.smzs.mall.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.smzs.mall"})
@EntityScan(basePackages = "com.smzs.mall.entity")
@EnableJpaRepositories(basePackages = "com.smzs.mall.repository")
public class SmzsMallAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmzsMallAppApplication.class, args);
    }

}