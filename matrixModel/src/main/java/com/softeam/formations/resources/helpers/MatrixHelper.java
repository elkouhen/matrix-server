package com.softeam.formations.resources.helpers;

import org.springframework.stereotype.Component;

import com.softeam.formations.datalayer.dto.Matrix;

/**
 * Created by elkouhen on 25/03/15.
 */
@Component
public class MatrixHelper {

	public Matrix identity(Matrix m) {

		int nx = m.getNx();

		for (int i = 0; i < nx; i++) {
			for (int j = 0; j < nx; j++) {
				m.set(i, j, i == j ? 1 : 0);
			}
		}

		return m;
	}

	public Matrix multiply(Matrix m, Matrix n) {
		final int nx = m.getNx();

		Matrix result = new Matrix(nx);

		for (int i = 0; i < nx; i++) {
			for (int j = 0; j < nx; j++) {

				float sum = 0;
				for (int k = 0; k < nx; k++) {
					sum += m.get(i, k) * n.get(k, j);
				}

				result.set(i, j, sum);
			}
		}

		return result;
	}
}
