package com.softeam.formations.resource.impl;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;

import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;

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
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softeam.formations.datalayer.dto.Matrix;
import com.softeam.formations.datalayer.dto.Pair;
import com.softeam.formations.resource.MatrixResourceV4;
import com.softeam.formations.resources.helpers.MatrixHelper;
import com.softeam.formations.statsd.StatsWriter;
import com.softeam.springconfig.JaxrsResource;

@JaxrsResource
@Service("com.softeam.formations.resource.MatrixResource" + MatrixResourceV4Impl.VERSION)
public class MatrixResourceV4Impl implements MatrixResourceV4 {

	public static final String HOST = "http://localhost:8080/matrixServerCxf/services/rest";
	public static final String RESOURCE = "/matrix/";
	public static final String VERSION = "V4";
	public static final String POWER = "/power";

	@Autowired
	private MatrixHelper matrixHelper;

	@Autowired
	private CloseableHttpAsyncClient httpClient;

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private StatsWriter statsWriter;

	private Observable<? super HttpResponse> makeRequest(final Pair<Matrix, Integer> operation, HttpAsyncRequestProducer requestProducer) {

		return Observable.create(new OnSubscribe<HttpResponse>() {

			@Override
			public void call(Subscriber<? super HttpResponse> subscriber) {
				HttpAsyncRequestProducer requestProducer = null;

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

	@Override
	@RequestMapping(value = POWER, method = RequestMethod.POST)
	public void power(@Suspended AsyncResponse asyncresponse, final Pair<Matrix, Integer> m) throws Exception {

		statsWriter.write();
		
		if (m.getRight() == 1) {
			asyncresponse.resume(m.getLeft());
			return;
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
				.subscribe(matrix -> asyncresponse.resume(matrix));

		return;
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
