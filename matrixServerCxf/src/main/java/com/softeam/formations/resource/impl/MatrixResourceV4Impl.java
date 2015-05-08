package com.softeam.formations.resource.impl;

import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.UnknownHostException;

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
import com.softeam.formations.resource.MatrixResourceV4;
import com.softeam.formations.resources.helpers.MatrixHelper;
import com.softeam.springconfig.JaxrsResource;

@JaxrsResource
@Service("com.softeam.formations.resource.MatrixResourceV4")
public class MatrixResourceV4Impl implements MatrixResourceV4 {

	public static final String HOST = "http://127.0.0.1:8080/matrixServerCxf/services/rest";
	public static final String RESOURCE = "/matrix/v3";
	public static final String POWER = "/power";

	@Autowired
	private MatrixHelper matrixHelper;

	@Autowired
	private CloseableHttpAsyncClient httpClient;

	@Autowired
	private ObjectMapper objectMapper;

	@RequestMapping(value = POWER, method = RequestMethod.POST)
	public void power(@Suspended AsyncResponse asyncresponse, final Pair<Matrix, Integer> m) throws Exception {

		if (m.getRight() == 1) {
			asyncresponse.resume(m.getLeft());
			return;
		}

		final Pair<Matrix, Integer> operation = new Pair<Matrix, Integer>(m.getLeft(), m.getRight() - 1);

		HttpAsyncRequestProducer requestProducer = requestProducer(operation, objectMapper);

		httpClient.execute(requestProducer, new BasicAsyncResponseConsumer(), new FutureCallback<HttpResponse>() {

			@Override
			public void cancelled() {
				// TODO Auto-generated method stub

			}

			@Override
			public void completed(HttpResponse arg0) {
				BasicHttpResponse basicHttpResponse = (BasicHttpResponse) arg0;

				try {
					asyncresponse.resume(objectMapper.readValue(basicHttpResponse.getEntity().getContent(), Matrix.class));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			@Override
			public void failed(Exception arg0) {
				// TODO Auto-generated method stub

			}
		});

		return;
	}

	private HttpAsyncRequestProducer requestProducer(final Pair<Matrix, Integer> operation, ObjectMapper objectMapper) throws UnsupportedEncodingException,
			JsonProcessingException, UnknownHostException {
		String operationAsString = objectMapper.writeValueAsString(operation);

		HttpPost request = new HttpPost(HOST + RESOURCE + POWER);

		request.addHeader("Accept", "application/json");
		request.addHeader("Content-Type", "application/json");

		request.setEntity(new StringEntity(operationAsString));

		return HttpAsyncMethods.create(new HttpHost(Inet4Address.getLocalHost(), 8080), request);
	}
}
