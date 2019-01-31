/**
 * 
 */
package com.pearson.sam.bridgeapi.iservice;

import com.pearson.sam.bridgeapi.elasticsearch.model.ClassroomSearch;

import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author VGUDLSA
 *
 */
public interface IClassroomSearchService {
	
	  public Page<ClassroomSearch> search(Pageable pageable, String searchable);
	  
	  public ClassroomSearch save(ClassroomSearch classroomSearch);
	  
	  public ClassroomSearch update(ClassroomSearch classroomSearch);
	  
	  public void delete(ClassroomSearch classroomSearch);
	  
	  public Page<ClassroomSearch> pageIt(Pageable pageable, ClassroomSearch searchable,StringMatcher sm);

}
