package com.softeam.springconfig;

import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.reactor.IOReactorException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.AsyncClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsAsyncClientHttpRequestFactory;
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
	public CloseableHttpAsyncClient asyncHttpClient() throws IOReactorException {
		final IOReactorConfig ioReactorConfig = IOReactorConfig.custom()//
				.setIoThreadCount(30)//
				.setConnectTimeout(30000)//
				.setSoTimeout(30000).build();

		DefaultConnectingIOReactor ioreactor = new DefaultConnectingIOReactor(ioReactorConfig);
		PoolingNHttpClientConnectionManager cm = new PoolingNHttpClientConnectionManager(ioreactor);

		cm.setDefaultMaxPerRoute(10000);
		cm.setMaxTotal(10000);

		HttpAsyncClientBuilder builder = HttpAsyncClients.custom();

		builder.setConnectionManager(cm);

		CloseableHttpAsyncClient httpclient = builder.build();

		httpclient.start();

		return httpclient;
	}

	@Bean
	public AsyncRestTemplate asyncRestTemplate() throws IOReactorException {
		return new AsyncRestTemplate(asyncHttpRequestFactory());
	}

	@Bean
	public CloseableHttpClient httpClient() {

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
	public AsyncHttpClient ningAsyncHttpClient() {

		AsyncHttpClientConfig config = new AsyncHttpClientConfig.Builder().setAllowPoolingConnections(true).build();

		return new AsyncHttpClient(config);
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
