package com.pearson.sam.bridgeapi.repository;


import com.pearson.sam.bridgeapi.elasticsearch.model.UserSearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserElasticSearchRepository extends ElasticsearchRepository<UserSearch, String> {
	
  public UserSearch findByUid(String uid);

  public UserSearch findByEmail(String email);

  public Boolean existsByEmail(String email);
  
  public Long countBySchool(String schoolId);
  
  public Boolean existsByUid(String uid);
  
}
