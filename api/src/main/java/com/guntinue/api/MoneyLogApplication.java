package com.guntinue.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.guntinue")
@EntityScan(basePackages = "com.guntinue")
@EnableJpaRepositories(basePackages = "com.guntinue")
public class MoneyLogApplication {
    public static void main(String[] args) {
        SpringApplication.run(MoneyLogApplication.class, args);
    }
}
