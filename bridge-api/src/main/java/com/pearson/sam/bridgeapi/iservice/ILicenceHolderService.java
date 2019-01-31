package com.pearson.sam.bridgeapi.iservice;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pearson.sam.bridgeapi.model.KeyType;
import com.pearson.sam.bridgeapi.model.LicenceHolder;

public interface ILicenceHolderService {
	/**
	 * 
	 * @param licenceHolder
	 * @return LicenceHolder
	 */
	LicenceHolder create(LicenceHolder licenceHolder);
	/**
	 * 
	 * @param licenceHolder
	 * @return LicenceHolder
	 */
	LicenceHolder update(LicenceHolder licenceHolder,String operation);
	/**
	 * 
	 * @param licenceHolder
	 * @return LicenceHolder
	 */
	LicenceHolder fetch(LicenceHolder licenceHolder);
	/**
	 * 
	 * @param key
	 * @param ids
	 * @return List<LicenceHolder>
	 */
	List<LicenceHolder> fetchMultiple(KeyType key, List<String> ids);
	/**
	 * 
	 * @param licenceHolder
	 * @return LicenceHolder
	 */
	LicenceHolder delete(LicenceHolder licenceHolder);
	/**
	 * 
	 * @param pageable
	 * @param e
	 * @return Page<LicenceHolder>
	 */
	Page<LicenceHolder> pageIt(Pageable pageable, Example<LicenceHolder> e);

}
