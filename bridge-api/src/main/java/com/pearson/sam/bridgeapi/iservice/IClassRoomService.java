package com.pearson.sam.bridgeapi.iservice;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pearson.sam.bridgeapi.model.Classroom;
import com.pearson.sam.bridgeapi.model.KeyType;

public interface IClassRoomService {
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
	 * @param classRoomId
	 * @return Classroom
	 */
	Classroom findClassRoomById(String classRoomId);
	/**
	 * 
	 * @param validationCode
	 * @return Classroom
	 */
	Classroom findClassRoomByValidationCode(String validationCode);
	/**
	 * 
	 * @param schoolId
	 * @return Long
	 */
	Long countBySchoolId(String schoolId);
  /**
   * getTotalCount.
   * @return  
   */
  Long getTotalCount();
}
