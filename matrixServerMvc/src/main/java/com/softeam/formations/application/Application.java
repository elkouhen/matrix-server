package com.softeam.formations.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import reactor.spring.context.config.EnableReactor;

@EnableAutoConfiguration
@EnableReactor
@ComponentScan("com.softeam")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

    }
}
