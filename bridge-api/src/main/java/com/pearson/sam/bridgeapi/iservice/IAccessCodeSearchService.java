/**
 * 
 */
package com.pearson.sam.bridgeapi.iservice;

import com.pearson.sam.bridgeapi.elasticsearch.model.AccessCodeSearch;
import com.pearson.sam.bridgeapi.elasticsearch.model.UserSearch;

import java.util.List;

import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author VGUDLSA
 *
 */
public interface IAccessCodeSearchService {
	
	  public Page<AccessCodeSearch> search(Pageable pageable, String searchable);
	  
	  public AccessCodeSearch save(AccessCodeSearch accessCodeSearch);
	  
	  public AccessCodeSearch update(AccessCodeSearch accessCodeSearch);

	List<AccessCodeSearch> saveAll(List<AccessCodeSearch> accessCodeSearchs);

  /**
   * pageIt.
   * @param pageable
   * @param searchable
   * @param sm 
   * @return  
   */
  public Page<AccessCodeSearch> pageIt(Pageable pageable, AccessCodeSearch searchable, StringMatcher sm);

}
