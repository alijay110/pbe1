package com.pearson.sam.bridgeapi.ibridgeservice;

import com.pearson.sam.bridgeapi.elasticsearch.model.ClassroomSearch;
import com.pearson.sam.bridgeapi.model.Classroom;
import com.pearson.sam.bridgeapi.model.KeyType;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IClassRoomBridgeService {
	/**
	 * 
	 * @param classRoom
	 * @return ClassRoom
	 */
	Classroom create(Classroom classRoom);
	/**
	 * 
	 * @param userClassRoom
	 * @return ClassRoom
	 */
	Classroom update(Classroom userClassRoom);
	/**
	 * 
	 * @param classRoom
	 * @return classRoom
	 */
	Classroom fetch(Classroom classRoom);
	/**
	 * 
	 * @param classRoom
	 * @return ClassRoom
	 */
	Classroom delete(Classroom classRoom);
	/**
	 * 
	 * @param key
	 * @param ids
	 * @return List<Classroom>
	 */
	public List<Classroom> fetchMultiple(KeyType key, List<String> ids);
	/**
	 * 
	 * @param pageable
	 * @param e
	 * @return Page<ClassRoom>
	 */
	Page<Classroom> pageIt(Pageable pageable, Example<Classroom> e);
	/**
	 * 
	 * @param validationCode
	 * @return Classroom
	 */
	Classroom findClassRoomByValidationCode(String validationCode);
	
	Page<ClassroomSearch> search(Pageable pageable, String searchable);
	
	public Page<ClassroomSearch> pageIt(Pageable pageable, ClassroomSearch searchable,StringMatcher sm);
}
