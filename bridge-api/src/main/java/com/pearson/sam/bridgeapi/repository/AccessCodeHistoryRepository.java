package com.pearson.sam.bridgeapi.repository;

import com.pearson.sam.bridgeapi.model.AccessCodes;
import com.pearson.sam.bridgeapi.model.AccessCodesHistory;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessCodeHistoryRepository extends MongoRepository<AccessCodesHistory, String> {
	public AccessCodesHistory findTopByOrderByIdDesc();

}
