package com.pearson.sam.bridgeapi.ibridgeservice;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pearson.sam.bridgeapi.model.LicenceHistory;

public interface ILicenceHistoryBridgeService {
	/**
	 * 
	 * @param licenceHistory
	 * @return LicenceHistory
	 */
	LicenceHistory create(LicenceHistory licenceHistory);
	
	/**
	 * 
	 * @param pageable
	 * @param e
	 * @return Page<LicenceHistory>
	 */
	Page<LicenceHistory> pageIt(Pageable pageable, Example<LicenceHistory> e);
}
