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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.softeam.formations.datalayer.dto.Matrix;
import com.softeam.formations.datalayer.dto.Pair;
import com.softeam.formations.resource.MatrixResourceV2;
import com.softeam.formations.resources.helpers.MatrixHelper;
import com.softeam.springconfig.JaxrsResource;

@JaxrsResource
@Service("com.softeam.formations.resource.MatrixResource" + MatrixResourceV2Impl.VERSION)
public class MatrixResourceV2Impl implements MatrixResourceV2 {

	public static final String HOST = "http://127.0.0.1:8080/matrixServerCxf/services/rest";
	public static final String RESOURCE = "/matrix/";
	public static final String VERSION = "V2";
	public static final String POWER = "/power";

	@Autowired
	private AsyncRestTemplate restTemplate;

	@Autowired
	private MatrixHelper matrixHelper;

	@Override
	public void power(@Suspended AsyncResponse asyncreponse, Pair<Matrix, Integer> m) throws JsonProcessingException {

		if (m.getRight() == 1) {
			asyncreponse.resume(m.getLeft());
			return;
		}

		final Pair<Matrix, Integer> operation = new Pair<Matrix, Integer>(m.getLeft(), m.getRight() - 1);

		restTemplate//
				.exchange(HOST + RESOURCE + VERSION + POWER, HttpMethod.POST, new HttpEntity<Object>(operation), Matrix.class)//
				.addCallback(new ListenableFutureCallback<ResponseEntity<Matrix>>() {
					@Override
					public void onFailure(Throwable ex) {

					}

					@Override
					public void onSuccess(ResponseEntity<Matrix> responsePower) {

						asyncreponse.resume(matrixHelper.multiply(m.getLeft(), responsePower.getBody()));
					}
				});

		return;
	}
}
