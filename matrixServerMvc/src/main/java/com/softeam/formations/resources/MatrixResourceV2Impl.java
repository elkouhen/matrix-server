package com.softeam.formations.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.context.request.async.DeferredResult;

import com.softeam.formations.datalayer.dto.Matrix;
import com.softeam.formations.datalayer.dto.Pair;
import com.softeam.formations.resources.helpers.MatrixHelper;

@RestController
@RequestMapping(value = MatrixResourceV2Impl.RESOURCE, method = RequestMethod.POST)
public class MatrixResourceV2Impl {

	public static final String HOST = "http://localhost:8080";
	public static final String RESOURCE = "/matrix/v2";
	public static final String POWER = "/power";

	private static final Logger logger = LoggerFactory.getLogger(MatrixResourceV2Impl.class);

	@Autowired
	private AsyncRestTemplate restTemplate;

	@Autowired
	private MatrixHelper matrixHelper;

	@RequestMapping(value = POWER, method = RequestMethod.POST)
	public DeferredResult<Matrix> power(@RequestBody final Pair<Matrix, Integer> m) {

		final DeferredResult<Matrix> deferredResult = new DeferredResult<Matrix>();

		if (m.getRight() == 1) {
			deferredResult.setResult(m.getLeft());
			return deferredResult;
		}

		final Pair<Matrix, Integer> operation = new Pair<Matrix, Integer>(m.getLeft(), m.getRight() - 1);

		restTemplate.exchange(HOST + RESOURCE + POWER, HttpMethod.POST, new HttpEntity<Object>(operation), Matrix.class).addCallback(
				new ListenableFutureCallback<ResponseEntity<Matrix>>() {
					@Override
					public void onFailure(Throwable ex) {

						logger.error(ex.getMessage());
						deferredResult.setErrorResult(ex.getMessage());
					}

					@Override
					public void onSuccess(ResponseEntity<Matrix> response) {
						deferredResult.setResult(matrixHelper.multiply(m.getLeft(), response.getBody()));
					}
				});

		return deferredResult;
	}
}
