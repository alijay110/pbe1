package com.pearson.sam.bridgeapi.ibridgeservice;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pearson.sam.bridgeapi.model.KeyType;
import com.pearson.sam.bridgeapi.model.Licence;

public interface ILicenceBridgeService {
	/**
	 * 
	 * @param licence
	 * @return Licence
	 */
	Licence create(Licence licence);
	/**
	 * 
	 * @param licence
	 * @return Licence
	 */
	Licence update(Licence licence);
	/**
	 * 
	 * @param licence
	 * @return Licence
	 */
	Licence fetch(Licence licence);
	/**
	 * 
	 * @param licence
	 * @return Licence
	 */
	Licence delete(Licence licence);
	/**
	 * 
	 * @param key
	 * @param ids
	 * @return List<Licence>
	 */
	List<Licence> fetchMultiple(KeyType key, List<String> ids);
	/**
	 * 
	 * @param pageable
	 * @param e
	 * @return Page<Licence>
	 */
	Page<Licence> pageIt(Pageable pageable, Example<Licence> e);
}
