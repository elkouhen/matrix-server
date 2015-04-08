package com.softeam.formations.resources;


import com.softeam.formations.datalayer.dto.Matrix;
import com.softeam.formations.resources.dto.Pair;
import com.softeam.formations.resources.helpers.MatrixHelper;
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

@RestController

@RequestMapping(value = MatrixControllerImplV2.MATRIX_RESOURCE_URL, method = RequestMethod.POST)
public class MatrixControllerImplV2 {

    public static final String MATRIX_RESOURCE_HOST = "http://localhost:8080";
    public static final String MATRIX_RESOURCE_URL = "/matrix/v2";
    public static final String POWER = "/power";

    private static final Logger logger = LoggerFactory.getLogger(MatrixControllerImplV2.class);

    @Autowired
    private AsyncRestTemplate restTemplate;

    @Autowired
    private MatrixHelper matrixHelper;


    @RequestMapping(value = POWER, method = RequestMethod.POST)
    public DeferredResult<Matrix> power(@RequestBody final Pair<Matrix, Integer> m) {

        final DeferredResult<Matrix> deferredResult = new DeferredResult<>();

        if (m.getRight() == 1) {
            deferredResult.setResult(m.getLeft());
            return deferredResult;
        }

        final Pair<Matrix, Integer> operation = new Pair<>(m.getLeft(), m.getRight() - 1);

        restTemplate.exchange(MATRIX_RESOURCE_HOST + MATRIX_RESOURCE_URL + POWER,
                HttpMethod.POST, new HttpEntity<Object>(operation), Matrix.class).addCallback(new ListenableFutureCallback<ResponseEntity<Matrix>>() {
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
