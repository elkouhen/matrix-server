package com.softeam.formations.resource.impl;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softeam.formations.datalayer.dto.Matrix;
import com.softeam.formations.datalayer.dto.Pair;
import com.softeam.formations.resource.MatrixResourceV3;
import com.softeam.formations.resources.helpers.MatrixHelper;
import com.softeam.formations.statsd.StatsWriter;
import com.softeam.springconfig.JaxrsResource;

@JaxrsResource
@Service("com.softeam.formations.resource.MatrixResource" + MatrixResourceV3Impl.VERSION)
public class MatrixResourceV3Impl implements MatrixResourceV3 {

	public static final String HOST = "http://127.0.0.1:8080/matrixServerCxf/services/rest";
	public static final String RESOURCE = "/matrix/";
	public static final String VERSION = "V3";
	public static final String POWER = "/power";

	@Autowired
	private MatrixHelper matrixHelper;

	@Autowired
	private CloseableHttpAsyncClient httpClient;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private StatsWriter statsWriter;

	@Override
	@RequestMapping(value = POWER, method = RequestMethod.POST)
	public void power(@Suspended AsyncResponse asyncresponse, final Pair<Matrix, Integer> m) throws Exception {

		statsWriter.increment();
		statsWriter.write();

		asyncresponse.setTimeout(3000, TimeUnit.MILLISECONDS);

		if (m.getRight() == 1) {
			
			statsWriter.decrement();
			asyncresponse.resume(m.getLeft());
			return;
		}

		final Pair<Matrix, Integer> operation = new Pair<Matrix, Integer>(m.getLeft(), m.getRight() - 1);

		HttpAsyncRequestProducer requestProducer = requestProducer(operation, objectMapper);

		httpClient.execute(requestProducer, new BasicAsyncResponseConsumer(), new FutureCallback<HttpResponse>() {

			@Override
			public void cancelled() {
				
				statsWriter.decrement();
				asyncresponse.resume(new Exception("cancelled"));

			}

			@Override
			public void completed(HttpResponse arg0) {
				
				statsWriter.decrement();
				BasicHttpResponse basicHttpResponse = (BasicHttpResponse) arg0;

				try {
					Matrix matrix = objectMapper.readValue(basicHttpResponse.getEntity().getContent(), Matrix.class);
					asyncresponse.resume(matrixHelper.multiply(m.getLeft(), matrix));
				} catch (Exception e) {
					asyncresponse.resume(e);
				}

			}

			@Override
			public void failed(Exception exception) {
				
				statsWriter.decrement();
				asyncresponse.resume(exception);

			}
		});

		return;
	}

	private HttpAsyncRequestProducer requestProducer(final Pair<Matrix, Integer> operation, ObjectMapper objectMapper) throws UnsupportedEncodingException,
			JsonProcessingException, UnknownHostException {
		String operationAsString = objectMapper.writeValueAsString(operation);

		HttpPost request = new HttpPost(HOST + RESOURCE + VERSION + POWER);

		request.addHeader("Accept", "application/json");
		request.addHeader("Content-Type", "application/json");

		request.setEntity(new StringEntity(operationAsString));

		return HttpAsyncMethods.create(new HttpHost(InetAddress.getLocalHost(), 8080), request);
	}
}
