package com.softeam.formations.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.ContextClosedEvent;

import reactor.spring.context.config.EnableReactor;

@EnableAutoConfiguration
@EnableReactor
// @EnableAdminServer
// @EnableDiscoveryClient
@ComponentScan("com.softeam")
public class Application {

	public static void main(String[] args) {
		ConfigurableApplicationContext cac = SpringApplication.run(Application.class, args);

		cac.addApplicationListener(new ApplicationListener<ContextClosedEvent>() {

			@Override
			public void onApplicationEvent(ContextClosedEvent event) {

			}
		});
	}
}
