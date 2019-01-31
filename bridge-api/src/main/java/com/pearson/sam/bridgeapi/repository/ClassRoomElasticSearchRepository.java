package com.pearson.sam.bridgeapi.repository;


import com.pearson.sam.bridgeapi.elasticsearch.model.ClassroomSearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassRoomElasticSearchRepository extends ElasticsearchRepository<ClassroomSearch, String> {

	ClassroomSearch findByClassroomId(String classroomId);
	
}
