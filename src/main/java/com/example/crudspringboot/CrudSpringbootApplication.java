package com.example.crudspringboot;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class CrudSpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrudSpringbootApplication.class, args);
    }

}
