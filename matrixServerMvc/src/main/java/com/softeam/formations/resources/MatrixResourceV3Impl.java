package com.softeam.formations.resources;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.ContentType;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.nio.client.methods.HttpAsyncMethods;
import org.apache.http.nio.protocol.HttpAsyncRequestProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import rx.apache.http.ObservableHttp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softeam.formations.datalayer.dto.Matrix;
import com.softeam.formations.datalayer.dto.Pair;
import com.softeam.formations.resources.helpers.MatrixHelper;

@RestController
@RequestMapping(value = MatrixResourceV3Impl.RESOURCE_URL, method = RequestMethod.POST)
public class MatrixResourceV3Impl {

	public static final String HOST = "http://localhost:8080";
	public static final String RESOURCE_URL = "/matrix/v3";
	public static final String POWER = "/power";

	@Autowired
	private MatrixHelper matrixHelper;

	@Autowired
	private CloseableHttpAsyncClient closeableHttpAsyncClient;

	@Autowired
	private ObjectMapper objectMapper;

	@RequestMapping(value = POWER, method = RequestMethod.POST)
	public DeferredResult<Matrix> power(
			@RequestBody final Pair<Matrix, Integer> m)
			throws UnsupportedEncodingException, JsonProcessingException {

		final DeferredResult<Matrix> deferredResult = new DeferredResult<Matrix>();

		if (m.getRight() == 1) {
			deferredResult.setResult(m.getLeft());
			return deferredResult;
		}

		final Pair<Matrix, Integer> operation = new Pair<Matrix, Integer>(
				m.getLeft(), m.getRight() - 1);

		HttpAsyncRequestProducer requestProducer = requestProducer(operation,
				objectMapper);

		ObservableHttp
				.createRequest(requestProducer, closeableHttpAsyncClient)
				.toObservable()
				.flatMap(response -> {
					return response.getContent().map(bb -> {
						return new String(bb);
					});
				})
				.toBlocking()
				.forEach(
						response -> {

							System.out.println(response);

							try {
								Matrix readValue = objectMapper.readValue(
										(String) response, Matrix.class);

								deferredResult.setResult(readValue);
							} catch (Exception e) {
								deferredResult.setErrorResult(e);
							}
						});

		return deferredResult;
	}

	private HttpAsyncRequestProducer requestProducer(
			final Pair<Matrix, Integer> operation, ObjectMapper objectMapper)
			throws UnsupportedEncodingException, JsonProcessingException {
		String operationAsString = objectMapper.writeValueAsString(operation);

		return HttpAsyncMethods.createPost(HOST + RESOURCE_URL + POWER,
				operationAsString, ContentType.APPLICATION_JSON);
	}
}
