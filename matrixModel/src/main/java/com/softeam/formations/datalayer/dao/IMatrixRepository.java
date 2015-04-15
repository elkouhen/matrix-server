package com.softeam.formations.datalayer.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.softeam.formations.datalayer.dto.Matrix;

public interface IMatrixRepository extends MongoRepository<Matrix, String> {

	public Matrix findById(String id);
}
