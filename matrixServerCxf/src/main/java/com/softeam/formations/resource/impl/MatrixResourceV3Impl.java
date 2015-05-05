package com.softeam.formations.resource.impl;

import java.io.UnsupportedEncodingException;

import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;

import org.apache.http.entity.ContentType;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.nio.client.methods.HttpAsyncMethods;
import org.apache.http.nio.protocol.HttpAsyncRequestProducer;
import org.apache.http.protocol.BasicHttpContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import rx.Notification;
import rx.apache.http.ObservableHttp;
import rx.apache.http.ObservableHttpResponse;
import rx.functions.Action1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softeam.formations.datalayer.dto.Matrix;
import com.softeam.formations.datalayer.dto.Pair;
import com.softeam.formations.resource.MatrixResourceV3;
import com.softeam.formations.resources.helpers.MatrixHelper;
import com.softeam.springconfig.JaxrsResource;

@JaxrsResource
@Service("com.softeam.formations.resource.MatrixResourceV3")
public class MatrixResourceV3Impl implements MatrixResourceV3 {

	public static final String HOST = "http://localhost:8080/matrixServerCxf/services/rest";
	public static final String RESOURCE_URL = "/matrix/v3";
	public static final String POWER = "/power";

	@Autowired
	private MatrixHelper matrixHelper;

	@Autowired
	private CloseableHttpAsyncClient httpClient;

	@Autowired
	private ObjectMapper objectMapper;

	@RequestMapping(value = POWER, method = RequestMethod.POST)
	public void power(@Suspended AsyncResponse asyncresponse,
			final Pair<Matrix, Integer> m) throws JsonProcessingException,
			UnsupportedEncodingException {

		if (m.getRight() == 1) {
			asyncresponse.resume(m.getLeft());
			return;
		}

		final Pair<Matrix, Integer> operation = new Pair<Matrix, Integer>(
				m.getLeft(), m.getRight() - 1);

		HttpAsyncRequestProducer requestProducer = requestProducer(operation,
				objectMapper);

		ObservableHttp
				.createRequest(requestProducer, httpClient)
				.toObservable()
				.doOnEach(
						new Action1<Notification<? super ObservableHttpResponse>>() {

							@Override
							public void call(
									Notification<? super ObservableHttpResponse> t1) {
								asyncresponse.resume("coucou");

							}
						});

		return;
	}

	private HttpAsyncRequestProducer requestProducer(
			final Pair<Matrix, Integer> operation, ObjectMapper objectMapper)
			throws UnsupportedEncodingException, JsonProcessingException {
		String operationAsString = objectMapper.writeValueAsString(operation);

		return HttpAsyncMethods.createPost(HOST + RESOURCE_URL + POWER,
				operationAsString, ContentType.APPLICATION_JSON);
	}
}
