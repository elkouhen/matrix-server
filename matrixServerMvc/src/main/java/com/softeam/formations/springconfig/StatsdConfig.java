package com.softeam.formations.springconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;

@Configuration
public class StatsdConfig {

	@Bean
	public StatsDClient statsdClient() {
		return new NonBlockingStatsDClient("com.softeam", "localhost", 8125);
	}
}
