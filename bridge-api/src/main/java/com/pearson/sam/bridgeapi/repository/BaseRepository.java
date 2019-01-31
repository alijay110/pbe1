package com.pearson.sam.bridgeapi.repository;

import com.pearson.sam.bridgeapi.model.BaseModel;

import java.io.Serializable;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepository<T extends BaseModel, ID extends Serializable>
    extends MongoRepository<T, ID> {
}
