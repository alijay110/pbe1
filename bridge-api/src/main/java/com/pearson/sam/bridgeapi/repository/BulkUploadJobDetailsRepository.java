package com.pearson.sam.bridgeapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.pearson.sam.bridgeapi.model.BulkUploadJobDetails;

@Repository	
public interface BulkUploadJobDetailsRepository extends MongoRepository<BulkUploadJobDetails, String> {

	public BulkUploadJobDetails findByJobId(int jobId);
}