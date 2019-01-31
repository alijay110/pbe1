/**
 * 
 */
package com.pearson.sam.bridgeapi.iservice;

import com.pearson.sam.bridgeapi.model.AccessCodes;
import com.pearson.sam.bridgeapi.model.KeyType;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author VGUDLSA
 *
 */
public interface IAccessCodeRepoService {
	
	AccessCodes create(AccessCodes accesscode);
	
	AccessCodes update(AccessCodes accesscode);
	
	AccessCodes fetchOne(AccessCodes accesscode);
	
	List<AccessCodes> fetchMultiple(KeyType key, List<String> ids);
	
	Page<AccessCodes> pageIt(Pageable pageable, Example<AccessCodes> e);
	
	List<AccessCodes> generateAccessCodes(AccessCodes accessCodedata);
	
	List<AccessCodes> getAccessCodesFromBatch(String batch );
	
	Map<String, Object> addAccessCodes(String bodyString);

  /**
   * getTotalCount.
   * @return  
   */
  Long getTotalCount();

}
