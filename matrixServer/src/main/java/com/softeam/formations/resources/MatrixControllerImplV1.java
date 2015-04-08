package com.softeam.formations.resources;

import com.softeam.formations.datalayer.dto.Matrix;
import com.softeam.formations.resources.dto.Pair;
import com.softeam.formations.resources.helpers.MatrixHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController

@RequestMapping(value = MatrixControllerImplV1.MATRIX_RESOURCE_URL, method = RequestMethod.POST)
public class MatrixControllerImplV1 {

    public static final String MATRIX_RESOURCE_HOST = "http://localhost:8080";
    public static final String MATRIX_RESOURCE_URL = "/matrix/v1";
    public static final String POWER = "/power";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MatrixHelper matrixHelper;

    @RequestMapping(value = POWER, method = RequestMethod.POST)
    public Matrix power(@RequestBody final Pair<Matrix, Integer> m) {

        if (m.getRight() == 1) {
            return m.getLeft();
        }

        final Pair<Matrix, Integer> operation = new Pair<Matrix, Integer>(m.getLeft(), m.getRight() - 1);

        ResponseEntity<Matrix> response = restTemplate.exchange(MATRIX_RESOURCE_HOST + MATRIX_RESOURCE_URL + POWER,
                HttpMethod.POST, new HttpEntity<Object>(operation), Matrix.class);

        return matrixHelper.multiply(m.getLeft(), response.getBody());
    }
}
