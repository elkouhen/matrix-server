package com.softeam.formations.resources;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.nio.client.methods.HttpAsyncMethods;
import org.apache.http.nio.protocol.BasicAsyncResponseConsumer;
import org.apache.http.nio.protocol.HttpAsyncRequestProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softeam.formations.datalayer.dto.Matrix;
import com.softeam.formations.datalayer.dto.Pair;
import com.softeam.formations.resources.helpers.MatrixHelper;

@RestController
@RequestMapping(value = MatrixResourceV4Impl.RESOURCE + MatrixResourceV4Impl.VERSION, method = RequestMethod.POST)
public class MatrixResourceV4Impl {

	public static final String HOST = "http://localhost:8080";
	public static final String RESOURCE = "/matrix/";
	public static final String VERSION = "V3";
	public static final String POWER = "/power";

	@Autowired
	private MatrixHelper matrixHelper;

	@Autowired
	private CloseableHttpAsyncClient httpClient;

	@Autowired
	private ObjectMapper objectMapper;

	@RequestMapping(value = POWER, method = RequestMethod.POST)
	public DeferredResult<Matrix> power(@RequestBody final Pair<Matrix, Integer> m) throws Exception {

		final DeferredResult<Matrix> deferredResult = new DeferredResult<Matrix>();

		if (m.getRight() == 1) {
			deferredResult.setResult(m.getLeft());
			return deferredResult;
		}

		final Pair<Matrix, Integer> operation = new Pair<Matrix, Integer>(m.getLeft(), m.getRight() - 1);

		HttpAsyncRequestProducer requestProducer = requestProducer(operation, objectMapper);

		makeRequest(operation, requestProducer)//
				.map(httpResponse -> {
					BasicHttpResponse basicHttpResponse = (BasicHttpResponse) httpResponse;
					Matrix matrix = null;
					try {
						matrix = objectMapper.readValue(basicHttpResponse.getEntity().getContent(), Matrix.class);

					} catch (Exception e) {

					}

					return matrix;
				})//
				.subscribe(matrix -> deferredResult.setResult(matrix));

		return deferredResult;
	}

	private Observable<? super HttpResponse> makeRequest(final Pair<Matrix, Integer> operation, HttpAsyncRequestProducer requestProducer) {

		return Observable.create(new OnSubscribe<HttpResponse>() {

			@Override
			public void call(Subscriber<? super HttpResponse> subscriber) {

				httpClient.execute(requestProducer, new BasicAsyncResponseConsumer(), new FutureCallback<HttpResponse>() {

					@Override
					public void cancelled() {

					}

					@Override
					public void completed(HttpResponse response) {

						subscriber.onNext(response);
						subscriber.onCompleted();
					}

					@Override
					public void failed(Exception exception) {
						subscriber.onError(exception);
					}
				});
			}
		});
	}

	private HttpAsyncRequestProducer requestProducer(final Pair<Matrix, Integer> operation, ObjectMapper objectMapper) throws UnsupportedEncodingException,
			Exception {
		String operationAsString = objectMapper.writeValueAsString(operation);

		HttpPost request = new HttpPost(HOST + RESOURCE + VERSION + POWER);

		request.addHeader("Accept", "application/json");
		request.addHeader("Content-Type", "application/json");

		request.setEntity(new StringEntity(operationAsString));

		return HttpAsyncMethods.create(new HttpHost(InetAddress.getLocalHost(), 8080), request);
	}
}
