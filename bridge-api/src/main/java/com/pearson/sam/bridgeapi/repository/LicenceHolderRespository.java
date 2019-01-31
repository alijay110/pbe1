/**
 * 
 */
package com.pearson.sam.bridgeapi.repository;

import com.pearson.sam.bridgeapi.model.LicenceHolder;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author VGUDLSA
 *
 */

@Repository
public interface LicenceHolderRespository extends MongoRepository<LicenceHolder, String> {

	LicenceHolder findByLicenceHolderId(String licenceHolderId);

	LicenceHolder findByHolderSchoolSchoolId(String schoolId);

	LicenceHolder findByHolderOrganisationOrganisationId(String organisationId);

	LicenceHolder findTopByOrderByIdDesc();

	List<LicenceHolder> findAllByHolderSchoolSchoolIdInAndHolderTeacherAccess(List<String> schools, Boolean isTeacherAccess);
	
	public boolean existsByLicenceHolderId(String licenceHolderId);

}
