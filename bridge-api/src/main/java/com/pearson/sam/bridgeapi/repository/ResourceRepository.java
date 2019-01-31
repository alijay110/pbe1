package com.pearson.sam.bridgeapi.repository;

import com.pearson.sam.bridgeapi.model.Resource;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRepository extends MongoRepository<Resource, String> {

	//Resource findByContentId(String resourceId);

}