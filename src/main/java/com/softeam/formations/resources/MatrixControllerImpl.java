package com.softeam.formations.resources;

import com.softeam.formations.resources.dto.Pair;
import com.softeam.formations.resources.helpers.MatrixHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.util.concurrent.SuccessCallback;
import org.springframework.web.bind.annotation.*;

import com.softeam.formations.resources.dto.Matrix;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
public class MatrixControllerImpl {

	public static final String MATRIX_RESOURCE_URL = "http://localhost:8080/matrix/power";
	@Autowired
	AsyncRestTemplate restTemplate;

	@Autowired
	private MatrixHelper matrixHelper;

	@RequestMapping(value = "/matrix/identity", method = RequestMethod.POST)
	public Matrix make(@RequestParam(value = "nx") int nx) {

		return matrixHelper.identity(new Matrix(nx));
	}

	@RequestMapping(value = "/matrix/power", method = RequestMethod.POST)
	public DeferredResult<Matrix> power(@RequestBody final Pair<Matrix, Integer> m) {

		final DeferredResult<Matrix> deferredResult = new DeferredResult<Matrix>();

		if (m.getRight().intValue() == 1){
			deferredResult.setResult(m.getLeft());
			return deferredResult;
		}

		final Pair<Matrix, Integer> operation = new Pair<>(m.getLeft(), m.getRight() - 1);

		restTemplate.exchange(MATRIX_RESOURCE_URL, HttpMethod.POST, new HttpEntity<Object>(operation), Matrix.class).addCallback(new ListenableFutureCallback<ResponseEntity<Matrix>>() {
			@Override
			public void onFailure(Throwable ex) {
				deferredResult.setErrorResult(ex.getMessage());
			}

			@Override
			public void onSuccess(ResponseEntity<Matrix> result) {
				deferredResult.setResult(matrixHelper.multiply(m.getLeft(),result.getBody()));
			}
		});

		return deferredResult;
	}
}
