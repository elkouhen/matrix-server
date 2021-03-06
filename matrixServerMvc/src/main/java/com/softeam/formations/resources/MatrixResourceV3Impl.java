package com.softeam.formations.resources;

import java.io.InputStream;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softeam.formations.datalayer.dto.Matrix;
import com.softeam.formations.datalayer.dto.Pair;
import com.softeam.formations.resources.helpers.MatrixHelper;
import com.softeam.formations.statsd.StatsWriter;

@RestController
@RequestMapping(value = MatrixResourceV3Impl.RESOURCE + MatrixResourceV3Impl.VERSION, method = RequestMethod.POST)
public class MatrixResourceV3Impl {

    public static final int PORT = 8081;
    public static final String HOST = "http://localhost";
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

    @RequestMapping(value = POWER, method = RequestMethod.POST)
    public DeferredResult<Matrix> power(@RequestBody final Pair<Matrix, Integer> m) throws Exception {

        statsWriter.increment();
        statsWriter.write();

        final DeferredResult<Matrix> deferredResult = new DeferredResult<Matrix>();

        if (m.getRight() == 1) {

            statsWriter.decrement();
            deferredResult.setResult(m.getLeft());

            return deferredResult;
        }

        final Pair<Matrix, Integer> operation = new Pair<Matrix, Integer>(m.getLeft(), m.getRight() - 1);

        HttpAsyncRequestProducer requestProducer = requestProducer(operation, objectMapper);

        BasicAsyncResponseConsumer responseConsumer = new BasicAsyncResponseConsumer();
        httpClient.execute(requestProducer, responseConsumer, new FutureCallback<HttpResponse>() {

            @Override
            public void cancelled() {

				// statsWriter.decrement();
                // deferredResult.setErrorResult(new Exception("cancelled"));
            }

            @Override
            public void completed(HttpResponse httpResponse) {
                statsWriter.decrement();

                BasicHttpResponse basicHttpResponse = (BasicHttpResponse) httpResponse;

                try {
                    InputStream content = basicHttpResponse.getEntity().getContent();

                    Matrix matrix = objectMapper.readValue(content, Matrix.class);

                    content.close();

                    responseConsumer.close();

                    deferredResult.setResult(matrixHelper.multiply(m.getLeft(), matrix));
                } catch (Exception e) {
                    deferredResult.setErrorResult(e);
                }
            }

            @Override
            public void failed(Exception exception) {

				// statsWriter.decrement();
                // deferredResult.setErrorResult(exception);
            }
        });

        return deferredResult;
    }

    private HttpAsyncRequestProducer requestProducer(final Pair<Matrix, Integer> operation, ObjectMapper objectMapper) throws UnsupportedEncodingException,
            Exception {
        String operationAsString = objectMapper.writeValueAsString(operation);

        HttpPost request = new HttpPost(HOST + ':' + PORT + RESOURCE + VERSION + POWER);

        request.addHeader("Accept", "application/json");
        request.addHeader("Content-Type", "application/json");

        StringEntity entity = new StringEntity(operationAsString);

        request.setEntity(entity);

        return HttpAsyncMethods.create(new HttpHost(InetAddress.getLocalHost(), PORT), request);
    }
}
