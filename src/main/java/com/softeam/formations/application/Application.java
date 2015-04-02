package com.softeam.formations.application;

import de.codecentric.boot.admin.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@EnableAdminServer
@EnableDiscoveryClient
@ComponentScan("com.softeam")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);

	}
}
