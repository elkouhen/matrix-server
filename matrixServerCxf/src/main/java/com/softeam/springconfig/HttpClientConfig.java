package com.softeam.springconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;

@Configuration
public class HttpClientConfig {

	@Bean
	public AsyncHttpClient asyncHttpClient() {

		AsyncHttpClientConfig config = new AsyncHttpClientConfig.Builder()
				.setAllowPoolingConnections(true).build();

		return new AsyncHttpClient(config);
	}
}
