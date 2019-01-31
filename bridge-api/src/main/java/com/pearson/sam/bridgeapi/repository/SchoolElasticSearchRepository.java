package com.pearson.sam.bridgeapi.repository;


import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.pearson.sam.bridgeapi.elasticsearch.model.SchoolSearch;

@Repository
public interface SchoolElasticSearchRepository extends ElasticsearchRepository<SchoolSearch, String> {

	SchoolSearch findBySchoolId(String schoolId);
	
}
