package com.pearson.sam.bridgeapi.ibridgeservice;

import com.pearson.sam.bridgeapi.elasticsearch.model.SchoolSearch;
import com.pearson.sam.bridgeapi.model.KeyType;
import com.pearson.sam.bridgeapi.model.School;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ISchoolBridgeService {
	/**
	 * 
	 * @param school
	 * @return School
	 */
	School create(School school);
	/**
	 * 
	 * @param school
	 * @return School
	 */
	School update(School school);
	/**
	 * 
	 * @param school
	 * @return School
	 */
	School fetch(School school);
	/**
	 * 
	 * @param school
	 * @return School
	 */
	School delete(School school);
	/**
	 * 
	 * @param pageable
	 * @param e
	 * @return Page<School>
	 */
	Page<School> pageIt(Pageable pageable, Example<School> e);
	/**
	 * 
	 * @param pageable
	 * @param name
	 * @param schoolIds
	 * @return Page<School>
	 */
	Page<School> pageIt(Pageable pageable, String name, List<String> schoolIds);
	/**
	 * 
	 * @param teacherCode
	 * @return School
	 */
	 School findSchoolByTeacherCode(String teacherCode);
	 /**
	  * 
	  * @param studentCode
	  * @return School
	  */
	 School findSchoolByStudentCode(String studentCode);
	 /**
	  * 
	  * @param code
	  * @return School
	  */
	 School findSchoolByCode(String code);
	 /**
	  * 
	  * @param key
	  * @param ids
	  * @return List<School>
	  */
	 List<School> fetchMultiple(KeyType key, List<String> ids);
	 
	 public Page<SchoolSearch> search(Pageable pageable,String searchable);
  /**
   * pageIt.
   * @param pageable
   * @param query
   * @return  
   */
  Page<SchoolSearch> pageIt(Pageable pageable, SchoolSearch query,StringMatcher sm);
}
