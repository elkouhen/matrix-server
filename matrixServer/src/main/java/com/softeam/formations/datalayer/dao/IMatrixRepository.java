package com.softeam.formations.datalayer.dao;

import com.softeam.formations.datalayer.dto.Matrix;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

public interface IMatrixRepository extends MongoRepository<Matrix, String> {

    public Matrix findById(String id);
}
