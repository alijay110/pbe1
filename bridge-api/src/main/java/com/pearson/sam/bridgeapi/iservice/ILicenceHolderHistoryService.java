package com.pearson.sam.bridgeapi.iservice;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pearson.sam.bridgeapi.model.KeyType;
import com.pearson.sam.bridgeapi.model.LicenceHolderHistory;

public interface ILicenceHolderHistoryService {
	/**
	 * 
	 * @param licenceHolderHistory
	 * @return LicenceHolderHistory
	 */
	LicenceHolderHistory create(LicenceHolderHistory licenceHolderHistory);
	/**
	 * 
	 * @param licenceHolderHistory
	 * @return LicenceHolderHistory
	 */
	LicenceHolderHistory update(LicenceHolderHistory licenceHolderHistory);
	/**
	 * 
	 * @param licenceHolderHistory
	 * @return LicenceHolderHistory
	 */
	LicenceHolderHistory fetch(LicenceHolderHistory licenceHolderHistory);
	/**
	 * 
	 * @param key
	 * @param ids
	 * @return List<LicenceHolderHistory>
	 */
	List<LicenceHolderHistory> fetchMultiple(KeyType key, List<String> ids);
	/**
	 * 
	 * @param licenceHolderHistory
	 * @return LicenceHolderHistory
	 */
	LicenceHolderHistory delete(LicenceHolderHistory licenceHolderHistory);
	/**
	 * 
	 * @param pageable
	 * @param e
	 * @return Page<LicenceHolderHistory>
	 */
	Page<LicenceHolderHistory> pageIt(Pageable pageable, Example<LicenceHolderHistory> e);

}
