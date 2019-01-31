package com.pearson.sam.bridgeapi.iservice;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pearson.sam.bridgeapi.model.KeyType;
import com.pearson.sam.bridgeapi.model.School;

public interface ISchoolService {
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
	 * @param schoolId
	 * @return School
	 */
	School findSchoolById(String schoolId);

	/**
	 * 
	 * @param key
	 * @param ids
	 * @return List<School>
	 */
	List<School> fetchMultiple(KeyType key, List<String> ids);

	List<School> fetchSchoolList(List<School> schoolList);

	School getSchoolDetails(School school);

  /**
   * getTotalCount.
   * @return  
   */
  Long getTotalCount();

}
