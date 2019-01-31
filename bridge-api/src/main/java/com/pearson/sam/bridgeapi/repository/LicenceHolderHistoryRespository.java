/**
 * 
 */
package com.pearson.sam.bridgeapi.repository;

import com.pearson.sam.bridgeapi.model.LicenceHolderHistory;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author VGUDLSA
 *
 */

@Repository
public interface LicenceHolderHistoryRespository extends MongoRepository<LicenceHolderHistory, String>{

	LicenceHolderHistory findByLicenceHolderHistoryId(String licenceHolderHistoryId);

	LicenceHolderHistory findTopByOrderByIdDesc();
}
