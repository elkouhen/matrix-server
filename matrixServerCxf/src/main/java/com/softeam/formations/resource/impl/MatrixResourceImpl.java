package com.softeam.formations.resource.impl;

import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.Response;
import com.softeam.formations.datalayer.dto.Matrix;
import com.softeam.formations.datalayer.dto.Pair;
import com.softeam.formations.resource.MatrixResource;
import com.softeam.springconfig.JaxrsResource;

@JaxrsResource
@Service("com.softeam.formations.resource.MatrixResource")
public class MatrixResourceImpl implements MatrixResource {

	private static final String RESOURCE_MATRIX_POWER = "http://localhost:8080/matrixServerCxf/services/rest/matrix/power";

	@Override
	public void power(@Suspended AsyncResponse response, Pair<Matrix, Integer> m)
			throws JsonProcessingException {

		if (m.getRight() == 1) {
			response.resume(m.getLeft());
		} else {
			Pair<Matrix, Integer> pair = new Pair<Matrix, Integer>(m.getLeft(),
					m.getRight() - 1);

			AsyncHttpClient async = new AsyncHttpClient();

			String msg = new ObjectMapper().writeValueAsString(pair);

			async.preparePost(RESOURCE_MATRIX_POWER).setBody(msg)
					.addHeader("Content-Type", "application/json")
					.execute(new AsyncCompletionHandler<Response>() {

						@Override
						public Response onCompleted(Response arg0)
								throws Exception {

							response.resume(arg0.getResponseBody());

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
