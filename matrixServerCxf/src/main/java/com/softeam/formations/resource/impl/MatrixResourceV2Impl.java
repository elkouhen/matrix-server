package com.softeam.formations.resource.impl;

import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import com.softeam.formations.datalayer.dto.Matrix;
import com.softeam.formations.datalayer.dto.Pair;
import com.softeam.formations.resource.MatrixResource;
import com.softeam.formations.resource.MatrixResourceV2;
import com.softeam.formations.resources.helpers.MatrixHelper;
import com.softeam.springconfig.JaxrsResource;

@JaxrsResource
@Service("com.softeam.formations.resource.MatrixResourceV2")
public class MatrixResourceV2Impl implements MatrixResourceV2 {

	private static final String RESOURCE_MATRIX_POWER = "http://localhost:8080/matrixServerCxf/services/rest/v2/matrix/power";

	@Autowired
	private AsyncRestTemplate restTemplate;

	@Autowired
	private MatrixHelper matrixHelper;

	@Override
	public void power(@Suspended AsyncResponse response, Pair<Matrix, Integer> m)
			throws JsonProcessingException {

		if (m.getRight() == 1) {
			response.resume(m.getLeft());
		} else {

			final Pair<Matrix, Integer> operation = new Pair<Matrix, Integer>(
					m.getLeft(), m.getRight() - 1);

			restTemplate
					.exchange(RESOURCE_MATRIX_POWER, HttpMethod.POST,
							new HttpEntity<Object>(operation), Matrix.class)
					.addCallback(
							new ListenableFutureCallback<ResponseEntity<Matrix>>() {
								@Override
								public void onFailure(Throwable ex) {

								}

								@Override
								public void onSuccess(
										ResponseEntity<Matrix> responsePower) {

									response.resume(matrixHelper.multiply(
											m.getLeft(),
											responsePower.getBody()));
								}
							});

		}

		return;
	}
}
