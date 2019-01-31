package com.pearson.sam.bridgeapi.repository;

import com.pearson.sam.bridgeapi.model.LicenceHistory;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LicenceHistoryRespository extends MongoRepository<LicenceHistory, String> {

	LicenceHistory findByLicence(String licenceId);

}
