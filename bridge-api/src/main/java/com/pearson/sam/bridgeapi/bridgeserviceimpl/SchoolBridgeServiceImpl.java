package com.pearson.sam.bridgeapi.bridgeserviceimpl;

import static com.pearson.sam.bridgeapi.util.Utils.copyNonNullProperties;

import com.pearson.sam.bridgeapi.elasticsearch.model.SchoolSearch;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGeneralException;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGraphqlException;
import com.pearson.sam.bridgeapi.ibridgeservice.ISchoolBridgeService;
import com.pearson.sam.bridgeapi.iservice.IClassRoomService;
import com.pearson.sam.bridgeapi.iservice.ISchoolSearchService;
import com.pearson.sam.bridgeapi.iservice.ISchoolService;
import com.pearson.sam.bridgeapi.iservice.IUserService;
import com.pearson.sam.bridgeapi.model.KeyType;
import com.pearson.sam.bridgeapi.model.School;
import com.pearson.sam.bridgeapi.model.User;
import com.pearson.sam.bridgeapi.security.ISessionFacade;
import com.pearson.sam.bridgeapi.util.Utils;

import graphql.ErrorType;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SchoolBridgeServiceImpl implements ISchoolBridgeService {
	private static final Logger logger = LoggerFactory.getLogger(SchoolBridgeServiceImpl.class);

	@Autowired
	private ISchoolService schoolService;

	@Autowired
	private IClassRoomService classRoomService;

	@Autowired
	private IUserService userService;

	@Autowired
	private ISessionFacade sessionFacade;
	
	@Autowired
	private ISchoolSearchService schoolSearchService;

	@Override
	public School create(School school) {

		if (StringUtils.isEmpty(school.getCreatedBy())) {
			User user = sessionFacade.getLoggedInUser(true);
			school.setCreatedBy(user.getUid());
		}
		School response = schoolService.create(school);
		populateUserNClassCount(response);
		SchoolSearch schoolSearch = getSchoolSearchObject(response);
		schoolSearchService.save(schoolSearch);
		return response;
	}

	@Override
	public School update(School school) {

		if (StringUtils.isEmpty(school.getUpdatedBy())) {
			User user = sessionFacade.getLoggedInUser(true);
			school.setUpdatedBy(user.getUid());
		}
		school = schoolService.update(school);
		schoolSearchService.update(getSchoolSearchObject(school));
		return school;
	}

	@Override
	public School fetch(School school) {
		School response = schoolService.getSchoolDetails(school);
		populateUserNClassCount(response);
		return response;
	}

	@Override
	public School delete(School school) {

		if (StringUtils.isEmpty(school.getUpdatedBy())) {
			User user = sessionFacade.getLoggedInUser(true);
			school.setUpdatedBy(user.getUid());
		}
		return schoolService.delete(school);
	}

	
	 private static Example<School> schoolEx = Example.of(new School());

	  public void loadSchoolToMaster() {
	    initLoad(PageRequest.of(0, schoolService.getTotalCount().intValue()));
	  }

	  private void initLoad(Pageable pageable) {
	    Page<School> schoolPage = this.pageIt(pageable, schoolEx);
	    schoolPage.getContent().stream().forEach( cls -> {SchoolSearch query = new SchoolSearch();copyNonNullProperties(cls, query);  schoolSearchService.save(query);});;
	  }

	  
	@Override
	public Page<School> pageIt(Pageable pageable, Example<School> e) {
		Page<School> schoolPage = schoolService.pageIt(pageable, e);
		List<School> schoolClientResponse = schoolService.fetchSchoolList(schoolPage.getContent());
		schoolClientResponse = schoolClientResponse.stream().map(this::populateUserNClassCount).collect(Collectors.toList());
		return new PageImpl<School>(schoolClientResponse, pageable, schoolPage.getTotalElements());
	}

	@Override
	public Page<School> pageIt(Pageable pageable, String name, List<String> schoolIds) {
		Page<School> schoolPage = schoolService.pageIt(pageable, name, schoolIds);
		List<School> schoolClientResponse = schoolService.fetchSchoolList(schoolPage.getContent());
		schoolClientResponse = schoolClientResponse.stream().map(this::populateUserNClassCount).collect(Collectors.toList());
		return new PageImpl<School>(schoolClientResponse, pageable, schoolPage.getTotalElements());
	}

	@Override
	public School findSchoolByTeacherCode(String teacherCode) {

		School school = schoolService.findSchoolByTeacherCode(teacherCode);
		String errorMessage = "Unable to find the School by teacher code:" + teacherCode;
		validateSchool(school, errorMessage);
		return school;
	}

	@Override
	public School findSchoolByStudentCode(String studentCode) {

		School school = schoolService.findSchoolByStudentCode(studentCode);
		String errorMessage = "Unable to find the School by student code:" + studentCode;
		validateSchool(school, errorMessage);
		return school;
	}

	@Override
	public School findSchoolByCode(String code) {

		School school = schoolService.findSchoolByCode(code);
		String errorMessage = "Unable to find the School by code:" + code;
		validateSchool(school, errorMessage);
		return school;
	}

	private School populateUserNClassCount(School school) {
		try {
			Long usersCount = userService.countBySchool(school.getSchoolId());
			Long classRoomsCount = classRoomService.countBySchoolId(school.getSchoolId());
			school.setUsersCount(usersCount);
			school.setClassRoomsCount(classRoomsCount);
		} catch (BridgeApiGeneralException e) {
			throw new BridgeApiGraphqlException(ErrorType.DataFetchingException, "Data Not Found!");
		}
		return school;
	}

	@Override
	public List<School> fetchMultiple(KeyType key, List<String> ids) {

		List<School> schoolList = null;

		switch (key) {
		case SCHOOL_ID:
			schoolList = schoolService.fetchMultiple(key, ids);
			break;

		case USER_ID:
			User user = sessionFacade.getLoggedInUser(true);
			//logger.info("logged User:" + user.getUid());
			schoolList = schoolService.fetchMultiple(key, user.getSchool());
			break;

		default:
			break;
		}
		return schoolList;
	}
	
	@Override
	public Page<SchoolSearch> search(Pageable pageable, String searchable) {
		return schoolSearchService.search(pageable, searchable);
	}

	private void validateSchool(School school, String errorMessage) {
		if (school == null) {
			//logger.error(errorMessage);
			throw new BridgeApiGraphqlException(ErrorType.DataFetchingException, errorMessage);
		}
	}
	
	private SchoolSearch getSchoolSearchObject(School school){
		SchoolSearch schoolSearch = new SchoolSearch();
		Utils.copyNonNullProperties(school, schoolSearch);
		return schoolSearch;
	}

  /* (non-Javadoc)
   * @see com.pearson.sam.bridgeapi.ibridgeservice.ISchoolBridgeService#pageIt(org.springframework.data.domain.Pageable, com.pearson.sam.bridgeapi.elasticsearch.model.UserSearch)
   */
  @Override
  public Page<SchoolSearch> pageIt(Pageable pageable, SchoolSearch searchable,StringMatcher sm) {
    return schoolSearchService.pageIt(pageable, searchable,sm);
  }

}
