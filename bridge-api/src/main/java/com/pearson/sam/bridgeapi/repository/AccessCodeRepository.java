package com.pearson.sam.bridgeapi.repository;

import com.pearson.sam.bridgeapi.model.AccessCodes;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessCodeRepository extends MongoRepository<AccessCodes, String> {

	public AccessCodes findByCode(String code);
	
	public void deleteByCode(String code);
	
	public AccessCodes findTopByOrderByIdDesc();
	
	public List<AccessCodes> findByBatch(String batch);

	public Boolean existsByCode(String code);

	public AccessCodes findByAccessCode(String accessCode);
	
	public List<AccessCodes> findByLastActivatedBy(String userName);

}
