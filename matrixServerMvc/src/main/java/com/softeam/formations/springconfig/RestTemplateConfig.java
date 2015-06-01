package com.softeam.formations.springconfig;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.AsyncClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsAsyncClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by elkouhen on 26/03/15.
 */
@Configuration
public class RestTemplateConfig {

	@Bean
	public ExecutorService executorService() {
		return Executors.newFixedThreadPool(10);

	}

	@Bean (destroyMethod = "close")
	public CloseableHttpAsyncClient asyncHttpClient() throws IOReactorException {

		RequestConfig config = RequestConfig.custom().setConnectTimeout(3000).build();

		ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor();
		PoolingNHttpClientConnectionManager cm = new PoolingNHttpClientConnectionManager(ioReactor);

		cm.setDefaultMaxPerRoute(10000);
		cm.setMaxTotal(10000);

		HttpAsyncClientBuilder builder = HttpAsyncClients.custom();

		builder.setConnectionManager(cm).setDefaultRequestConfig(config);

		CloseableHttpAsyncClient httpclient = builder.build();

		httpclient.start();

		return httpclient;
	}

	@Bean
	public AsyncRestTemplate asyncRestTemplate() throws IOReactorException {
		return new AsyncRestTemplate(asyncHttpRequestFactory());
	}

	@Bean
	public HttpClient httpClient() {

		HttpClientConnectionManager poolingConnManager = new PoolingHttpClientConnectionManager();
		CloseableHttpClient client = HttpClients.custom().setConnectionManager(poolingConnManager).build();

		return client;
	}

	@Bean
	public AsyncClientHttpRequestFactory asyncHttpRequestFactory() throws IOReactorException {
		return new HttpComponentsAsyncClientHttpRequestFactory(asyncHttpClient());
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
