package com.pearson.sam.bridgeapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.pearson.sam.bridgeapi.model.BulkUploadJobSummary;

@Repository	
public interface BulkUploadJobSummaryRepository extends MongoRepository<BulkUploadJobSummary, String> {
	
	public BulkUploadJobSummary findByJobId(int jobId);

}