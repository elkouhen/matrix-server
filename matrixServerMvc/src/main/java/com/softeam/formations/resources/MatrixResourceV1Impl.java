package com.softeam.formations.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.softeam.formations.datalayer.dto.Matrix;
import com.softeam.formations.datalayer.dto.Pair;
import com.softeam.formations.resources.helpers.MatrixHelper;
import com.softeam.formations.statsd.StatsWriter;

@RestController
@RequestMapping(value = MatrixResourceV1Impl.RESOURCE + MatrixResourceV1Impl.VERSION)
public class MatrixResourceV1Impl {

	public static final String HOST = "http://localhost:8080";
	public static final String RESOURCE = "/matrix/";
	public static final String VERSION = "V1";
	public static final String POWER = "/power";

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private MatrixHelper matrixHelper;
	
	@Autowired
	private StatsWriter statsWriter;

	@RequestMapping(value = POWER, method = RequestMethod.POST)
	public Matrix power(@RequestBody final Pair<Matrix, Integer> m) {
		
		statsWriter.write();

		if (m.getRight() == 1) {
			return m.getLeft();
		}

		final Pair<Matrix, Integer> operation = new Pair<Matrix, Integer>(m.getLeft(), m.getRight() - 1);

		ResponseEntity<Matrix> response = restTemplate.exchange(HOST + RESOURCE + VERSION + POWER, HttpMethod.POST, new HttpEntity<Object>(operation), Matrix.class);

		return matrixHelper.multiply(m.getLeft(), response.getBody());
	}
}
