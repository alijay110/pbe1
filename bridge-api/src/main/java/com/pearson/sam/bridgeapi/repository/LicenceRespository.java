package com.pearson.sam.bridgeapi.repository;

import com.pearson.sam.bridgeapi.model.Licence;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LicenceRespository extends MongoRepository<Licence, String> {
	Licence findByLicenceId(String licenceId);
	List<Licence> findByAttachedSchoolAndAttachedUser(String schoolId,String user);
	List<Licence> findByAttachedSchool(String schoolId);
}
