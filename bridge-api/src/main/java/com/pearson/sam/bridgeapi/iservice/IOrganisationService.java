package com.pearson.sam.bridgeapi.iservice;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pearson.sam.bridgeapi.model.KeyType;
import com.pearson.sam.bridgeapi.model.Organisation;

public interface IOrganisationService {
	/**
	 * 
	 * @param organisation
	 * @return Organisation
	 */
	Organisation create(Organisation organisation);
	/**
	 * 
	 * @param organisation
	 * @return Organisation
	 */
	Organisation update(Organisation organisation);
	/**
	 * 
	 * @param organisation
	 * @return Organisation
	 */
	Organisation fetch(Organisation organisation);
	/**
	 * 
	 * @param pageable
	 * @param e
	 * @return Page<Orgnisation>
	 */
	Page<Organisation> pageIt(Pageable pageable, Example<Organisation> e);
	/**
	 * 
	 * @param pageable
	 * @param name
	 * @param organisationIds
	 * @return Page<Orgnisation>
	 */
	Page<Organisation> pageIt(Pageable pageable, String name, List<String> organisationIds);
	/**
	 * 
	 * @param organisationId
	 * @return Organisation
	 */
	Organisation findByOrganisationId(String organisationId);
	/**
	 * 
	 * @param organisation
	 * @return Organisation
	 */
	Organisation delete(Organisation organisation);
	/**
	 * 
	 * @param key
	 * @param ids
	 * @return List<Organisation>
	 */
	List<Organisation> fetchMultiple(KeyType key, List<String> ids);
}
