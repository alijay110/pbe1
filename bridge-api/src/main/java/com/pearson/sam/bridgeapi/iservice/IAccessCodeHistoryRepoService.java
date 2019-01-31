/**
 * 
 */
package com.pearson.sam.bridgeapi.iservice;

import com.pearson.sam.bridgeapi.model.AccessCodesHistory;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author VGUDLSA
 *
 */
public interface IAccessCodeHistoryRepoService {
	AccessCodesHistory create(AccessCodesHistory accessCodesHistory);
	
	Page<AccessCodesHistory> pageIt(Pageable pageable, Example<AccessCodesHistory> e);

}
