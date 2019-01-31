package com.pearson.sam.bridgeapi.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.pearson.sam.bridgeapi.model.Organisation;

@Repository
public interface OrganisationRepository extends MongoRepository<Organisation, String>{
	
	Organisation findByOrganisationId(String organisationId);
	
	Page<Organisation> findAllByOrganisationIdIn(List<String> organisationIds, Pageable pageable);
	
	public boolean existsByOrganisationId(String organisationId);

}
