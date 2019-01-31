/**
 * 
 */
package com.pearson.sam.bridgeapi.iservice;

import com.pearson.sam.bridgeapi.elasticsearch.model.SchoolSearch;

import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author VGUDLSA
 *
 */
public interface ISchoolSearchService {


	  public Page<SchoolSearch> search(Pageable pageable, String searchable);
	  
	  public SchoolSearch save(SchoolSearch schoolSearch);
	  
	  public SchoolSearch update(SchoolSearch schoolSearch);

    /**
     * pageIt.
     * @param pageable
     * @param searchable
     * @return  
     */
    public Page<SchoolSearch> pageIt(Pageable pageable, SchoolSearch searchable,StringMatcher sm);
}
