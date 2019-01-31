/**
 * 
 */
package com.pearson.sam.bridgeapi.iservice;

import com.pearson.sam.bridgeapi.elasticsearch.model.UserSearch;

import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author VGUDLSA
 *
 */
public interface IUserSearchService {


	  public Page<UserSearch> search(Pageable pageable, String searchable);
	  
	  public UserSearch save(UserSearch userSearch);
	  
	  public UserSearch update(UserSearch userSearch);

    /**
     * pageIt.
     * @param pageable
     * @param searchable
     * @return  
     */
    public Page<UserSearch> pageIt(Pageable pageable, UserSearch searchable,StringMatcher sm);

    /**
     * insertIfNotFound.
     * @param userSearchObject  
     */
    public void insertIfNotFound(UserSearch userSearchObject);
}
