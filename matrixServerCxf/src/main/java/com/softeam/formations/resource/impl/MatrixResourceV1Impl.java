package com.softeam.formations.resource.impl;

import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import com.softeam.formations.datalayer.dto.Matrix;
import com.softeam.formations.datalayer.dto.Pair;
import com.softeam.formations.resource.MatrixResourceV1;
import com.softeam.formations.resources.helpers.MatrixHelper;
import com.softeam.springconfig.JaxrsResource;

@JaxrsResource
@Service("com.softeam.formations.resource.MatrixResourceV1")
public class MatrixResourceV1Impl implements MatrixResourceV1 {

	public static final String HOST = "http://localhost:8080/matrixServerCxf/services/rest";
	public static final String RESOURCE_URL = "/matrix/v3";
	public static final String POWER = "/power";

	@Autowired
	private AsyncHttpClient async;

	@Autowired
	private MatrixHelper matrixHelper;

	@Override
	public void power(@Suspended AsyncResponse response, Pair<Matrix, Integer> m)
			throws JsonProcessingException {

		if (m.getRight() == 1) {
			response.resume(m.getLeft());
		} else {
			Pair<Matrix, Integer> pair = new Pair<Matrix, Integer>(m.getLeft(),
					m.getRight() - 1);

			ObjectMapper objectMapper = new ObjectMapper();

			String msg = objectMapper.writeValueAsString(pair);

			async.preparePost(HOST + RESOURCE_URL + POWER).setBody(msg)
					.addHeader("Content-Type", "application/json")
					.addHeader("Accept", "application/json")
					.execute(new AsyncCompletionHandler<Response>() {

						@Override
						public Response onCompleted(Response responsePower)
								throws Exception {

							Matrix responsePowerMat = objectMapper.readValue(
									responsePower.getResponseBody(),
									Matrix.class);

							response.resume(matrixHelper.multiply(m.getLeft(),
									responsePowerMat));

							return null;
						}

						@Override
						public void onThrowable(Throwable throwable) {

							response.resume(throwable);

							return;
						}

					});
		}

		return;
	}
}
