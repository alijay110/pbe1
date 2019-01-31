package com.pearson.sam.bridgeapi.repository;


import com.pearson.sam.bridgeapi.elasticsearch.model.AccessCodeSearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessCodeElasticSearchRepository extends ElasticsearchRepository<AccessCodeSearch, String> {

	AccessCodeSearch findByAccessCode(String accessCode);

}
