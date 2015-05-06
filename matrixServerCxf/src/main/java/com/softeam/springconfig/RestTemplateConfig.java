package com.softeam.springconfig;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;

/**
 * Created by elkouhen on 26/03/15.
 */
@Configuration
public class RestTemplateConfig {

	@Bean
	public CloseableHttpAsyncClient asyncHttpClient() {
		final CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();

		httpclient.start();

		return httpclient;
	}

	@Bean
	public AsyncHttpClient ningAsyncHttpClient() {

		AsyncHttpClientConfig config = new AsyncHttpClientConfig.Builder().setAllowPoolingConnections(true).build();

		return new AsyncHttpClient(config);
	}

	@Bean
	public AsyncRestTemplate asyncRestTemplate() {
		return new AsyncRestTemplate();
	}

	@Bean
	public CloseableHttpClient httpClient() {

		final HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();

		final CloseableHttpClient client = httpClientBuilder.build();

		return client;
	}

	@Bean
	public HttpComponentsClientHttpRequestFactory httpRequestFactory() {
		return new HttpComponentsClientHttpRequestFactory(httpClient());
	}

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate(httpRequestFactory());
	}
}
