package com.pearson.sam.bridgeapi.validators;

import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.SCHOOL_IDS_LIST_SHOULD_NOT_BE_EMPTY;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.SCHOOL_ID_ALREADY_EXISTED;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.SCHOOL_ID_SHOULD_NOT_BE_EMPTY;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.SCHOOL_NAME_SHOULD_NOT_BE_EMPTY;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pearson.sam.bridgeapi.exceptions.BridgeApiGraphqlException;
import com.pearson.sam.bridgeapi.model.School;
import com.pearson.sam.bridgeapi.repository.SchoolRepository;
import com.pearson.sam.bridgeapi.util.Utils;
@Component
public class SchoolValidator {
	private static final Logger logger = LoggerFactory.getLogger(SchoolValidator.class);
	
	@Autowired
	private SchoolRepository schoolRepository;
	
	public void validate(School school) {
		isSchoolNameEmpty(school.getName());
	}
	
	public void isSchoolNameEmpty(String schoolName) {
		if (Utils.isEmpty(schoolName)) {
			//logger.error("Unalbel to create School due to School name is empty.");
			throw new BridgeApiGraphqlException(SCHOOL_NAME_SHOULD_NOT_BE_EMPTY);
		}
	}

	public void isSchoolIdEmpty(String schoolId) {
		if (Utils.isEmpty(schoolId)) {
			//logger.error("Unabel to fetch School due to School Id is empty.");
			throw new BridgeApiGraphqlException(SCHOOL_ID_SHOULD_NOT_BE_EMPTY);
		}
	}
	public void isListOfSchoolIdsEmpty(List<String> listOfSchoolIds) {
		if(listOfSchoolIds == null || listOfSchoolIds.isEmpty()) {
			 //logger.error("Unabel to fetch School due to School Ids list is empty.");
		      throw new BridgeApiGraphqlException(SCHOOL_IDS_LIST_SHOULD_NOT_BE_EMPTY);
		}
	}
	
	public void isSchoolIdExisted(String schoolId) {
	    School mongoDBSchool = schoolRepository.findBySchoolId(schoolId);
	    if(mongoDBSchool!=null && mongoDBSchool.getSchoolId().equals(schoolId)) {
	    	throw new BridgeApiGraphqlException(SCHOOL_ID_ALREADY_EXISTED);
	    }

	}

}
