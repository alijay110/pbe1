/**
 * 
 */
package com.pearson.sam.bridgeapi.ibridgeservice;

import com.pearson.sam.bridgeapi.elasticsearch.model.AccessCodeSearch;
import com.pearson.sam.bridgeapi.elasticsearch.model.UserSearch;
import com.pearson.sam.bridgeapi.model.AccessCodes;
import com.pearson.sam.bridgeapi.model.KeyType;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author VGUDLSA
 *
 */
public interface IAccessCodeBridgeService {
	
	AccessCodes create(AccessCodes accesscode);
	
	AccessCodes update(AccessCodes accesscode);
	
	AccessCodes fetchOne(AccessCodes accesscode);
	
	List<AccessCodes> fetchMultiple(KeyType key, List<String> ids);
	
	Page<AccessCodes> pageIt(Pageable pageable, Example<AccessCodes> e);
	
	List<AccessCodes> generateAccessCodes(AccessCodes accessCodedata);
	
	List<AccessCodes> getAccessCodesFromBatch(String batch );

	Page<AccessCodeSearch> search(Pageable pageable, String searchable);

  /**
   * pageIt.
   * @param pageable
   * @param query
   * @param sm 
   * @return  
   */
  Page<AccessCodeSearch> pageIt(Pageable pageable, AccessCodeSearch query, StringMatcher sm);

}
