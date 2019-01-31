package com.pearson.sam.bridgeapi.resolvers;

import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_MUTATION_ADD_SCHOOL;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_MUTATION_DEACTIVATE_SCHOOL;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_MUTATION_UPDATE_SCHOOL;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_QUERY_GET_SCHOOL_ID_BY_STUDENT_CODE;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_QUERY_GET_SCHOOL_ID_BY_TEACHER_CODE;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_QUERY_SCHOOL;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_QUERY_SCHOOLS;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_QUERY_SCHOOLS_ALL;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_QUERY_SCHOOLS_ALL_SELECTED;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_QUERY_SCHOOLS_BY_USER_ID;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_QUERY_SCHOOLS_PAGINATED;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_QUERY_SCHOOL_DATA_BY_SCHOOL_ID;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_QUERY_SCHOOL_ID_BY_CODE;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.PGE_GRAPHQL_QUERY_GET_SCHOOLSEARCH;
import static com.pearson.sam.bridgeapi.util.Utils.copyNonNullProperties;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.SCHOOL_NAME_SHOULD_NOT_BE_EMPTY;

import com.pearson.sam.bridgeapi.config.AuthContext;
import com.pearson.sam.bridgeapi.elasticsearch.model.SchoolSearch;
import com.pearson.sam.bridgeapi.elasticsearch.model.UserSearch;
import com.pearson.sam.bridgeapi.ibridgeservice.ISchoolBridgeService;
import com.pearson.sam.bridgeapi.model.KeyType;
import com.pearson.sam.bridgeapi.model.MethodType;
import com.pearson.sam.bridgeapi.model.School;
import com.pearson.sam.bridgeapi.model.Sort;
import com.pearson.sam.bridgeapi.model.User;
import com.pearson.sam.bridgeapi.util.Utils;
import com.pearson.sam.bridgeapi.validators.ModelValidator;
import com.pearson.sam.bridgeapi.validators.SchoolValidator;

import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.GraphQLRootContext;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import com.pearson.sam.bridgeapi.config.AuthContext;
import com.pearson.sam.bridgeapi.elasticsearch.model.SchoolSearch;
import com.pearson.sam.bridgeapi.ibridgeservice.ISchoolBridgeService;
import com.pearson.sam.bridgeapi.model.KeyType;
import com.pearson.sam.bridgeapi.model.MethodType;
import com.pearson.sam.bridgeapi.model.School;
import com.pearson.sam.bridgeapi.model.Sort;
import com.pearson.sam.bridgeapi.validators.ModelValidator;
import com.pearson.sam.bridgeapi.validators.SchoolValidator;

import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.GraphQLRootContext;

@Component
public class SchoolResolver {

	@Autowired
	ISchoolBridgeService schoolBridgeService;

	@Autowired
	ModelValidator validator;
	
	@Autowired
	SchoolValidator schoolValidator;

	@GraphQLMutation(name = GRAPHQL_MUTATION_ADD_SCHOOL)
	public School addSchool(@GraphQLArgument(name = "data") School data, @GraphQLRootContext AuthContext context) {
		validator.validateModel(data, MethodType.CREATE.toString());
		if(Utils.isNotEmpty(data.getSchoolId()))
			schoolValidator.isSchoolIdExisted(data.getSchoolId());
		return schoolBridgeService.create(data);
	}

	@GraphQLMutation(name = GRAPHQL_MUTATION_UPDATE_SCHOOL)
	public School updateSchool(@GraphQLArgument(name = "schoolId") String schoolId,
			@GraphQLArgument(name = "data") School data, @GraphQLRootContext AuthContext context) {
		validator.validateModel(data, MethodType.CREATE.toString());
		data.setSchoolId(schoolId);
		return schoolBridgeService.update(data);
	}

	/**
	 * 
	 * @param schoolId
	 * @param context
	 * @return
	 */
	@GraphQLMutation(name = GRAPHQL_MUTATION_DEACTIVATE_SCHOOL)
	public School deactivateSchool(@GraphQLArgument(name = "schoolId") String schoolId,
			@GraphQLRootContext AuthContext context) {
		School school = new School();
		school.setSchoolId(schoolId);
		return schoolBridgeService.delete(school);
	}

	/**
	 * school.
	 * 
	 * @param data desc
	 * @return
	 */
	@GraphQLQuery(name = GRAPHQL_QUERY_SCHOOL)
	public School school(@GraphQLArgument(name = "schoolId") String data) {
		School school = new School();
		school.setSchoolId(data);
		return schoolBridgeService.fetch(school);
	}

	/**
	 * getSchoolDataBySchoolId.
	 * 
	 * @return
	 */
	@GraphQLQuery(name = GRAPHQL_QUERY_SCHOOL_DATA_BY_SCHOOL_ID)
	public School getSchoolDataBySchoolId(@GraphQLArgument(name = "schoolId") String data) {
		School school = new School();
		school.setSchoolId(data);
		return schoolBridgeService.fetch(school);
	}

	@GraphQLQuery(name = GRAPHQL_QUERY_SCHOOLS)
	public List<School> schools(@GraphQLRootContext AuthContext context) {
		return schoolBridgeService.fetchMultiple(KeyType.USER_ID, null);
	}

	/**
	 * getPaginatedSchoolTableData.
	 * 
	 * @return
	 */
	@GraphQLQuery(name = GRAPHQL_QUERY_SCHOOLS_PAGINATED)
	public Page<School> getPaginatedSchoolTableData(
			@GraphQLArgument(name = "offset", defaultValue = "0") Integer pageNumber,
			@GraphQLArgument(name = "limit", defaultValue = "15") Integer pageLimit,
      @GraphQLArgument(name = "filter", defaultValue = "{}") School filter,
      @GraphQLArgument(name = "filterPattern", defaultValue = "5") StringMatcher sm,
			@GraphQLArgument(name = "sort", defaultValue = "{\"field\":\"schoolId\",\"order\":\"ASC\"}") Sort sort,
			@GraphQLRootContext AuthContext context) {
    Pageable pageable = PageRequest.of(pageNumber, pageLimit,
        Direction.valueOf(sort.getOrder().toString()), sort.getField());
    SchoolSearch query = new SchoolSearch();
    copyNonNullProperties(filter,query);
    Page<SchoolSearch> oldPage = schoolBridgeService.pageIt(pageable,query,sm);
    return new PageImpl<>(oldPage.getContent().stream().map(schoolSrch -> new School(schoolSrch)).collect(Collectors.toList()), pageable,
        oldPage.getTotalElements());
	}

	/**
	 * getAllSchoolsIdAndNameByString.
	 * 
	 * @return
	 */
	@GraphQLQuery(name = GRAPHQL_QUERY_SCHOOLS_ALL)
	public Page<School> getAllSchoolsIdAndNameByString(
			@GraphQLArgument(name = "offset", defaultValue = "0") Integer pageNumber,
			@GraphQLArgument(name = "limit", defaultValue = "15") Integer pageLimit,
			@GraphQLArgument(name = "filter", defaultValue = "{}") School filter,
			@GraphQLArgument(name = "filterPattern", defaultValue = "5") StringMatcher sm,
			@GraphQLArgument(name = "sort", defaultValue = "{\"field\":\"schoolId\",\"order\":\"ASC\"}") Sort sort,
			@GraphQLRootContext AuthContext context) {
		Pageable pageable = PageRequest.of(pageNumber, pageLimit, Direction.valueOf(sort.getOrder().toString()),
				sort.getField());
		return schoolBridgeService.pageIt(pageable,
				Example.of(filter, ExampleMatcher.matching().withStringMatcher(sm)));
	}

	/**
	 * getAllSelectedSchoolsIdAndNameByString.
	 */
	@GraphQLQuery(name = GRAPHQL_QUERY_SCHOOLS_ALL_SELECTED)
	public Page<School> getAllSelectedSchoolsIdAndNameByString(
			@GraphQLArgument(name = "offset", defaultValue = "0") Integer pageNumber,
			@GraphQLArgument(name = "limit", defaultValue = "15") Integer pageLimit,
			@GraphQLArgument(name = "arrayOfSchoolIds", defaultValue = "[]") List<String> schooldIds,
			@GraphQLArgument(name = "name", defaultValue = "") String name,
			@GraphQLArgument(name = "sort", defaultValue = "{\"field\":\"schoolId\",\"order\":\"ASC\"}") Sort sort,
			@GraphQLRootContext AuthContext context) {
		schoolValidator.isListOfSchoolIdsEmpty(schooldIds);
		for(String schoolId:schooldIds) {
			schoolValidator.isSchoolIdEmpty(schoolId);
		}
		schoolValidator.isSchoolNameEmpty(name);
		Pageable pageable = PageRequest.of(pageNumber, pageLimit, Direction.valueOf(sort.getOrder().toString()),
				sort.getField());
		return schoolBridgeService.pageIt(pageable, name, schooldIds);
	}

	@GraphQLQuery(name = GRAPHQL_QUERY_SCHOOLS_BY_USER_ID)
	public List<School> schoolsByUserId(@GraphQLRootContext AuthContext context) {
		return schoolBridgeService.fetchMultiple(KeyType.USER_ID, null);
	}

	/**
	 * schoolByCode.
	 * 
	 * @return
	 */
	@GraphQLQuery(name = GRAPHQL_QUERY_SCHOOL_ID_BY_CODE)
	public School schoolByCode(@GraphQLArgument(name = "schoolCode") String schoolCode) {
		return schoolBridgeService.findSchoolByCode(schoolCode);
	}

	/**
	 * schoolByCodeByTeacherCode.
	 * 
	 * @return
	 */
	@GraphQLQuery(name = GRAPHQL_QUERY_GET_SCHOOL_ID_BY_TEACHER_CODE)
	public School schoolByCodeByTeacherCode(@GraphQLArgument(name = "teacherCode") String teacherCode) {
		return schoolBridgeService.findSchoolByTeacherCode(teacherCode);
	}

	/**
	 * schoolIdByStudentCode.
	 * 
	 * @return
	 */
	@GraphQLQuery(name = GRAPHQL_QUERY_GET_SCHOOL_ID_BY_STUDENT_CODE)
	public School schoolIdByStudentCode(@GraphQLArgument(name = "studentCode") String studentCode) {
		return schoolBridgeService.findSchoolByStudentCode(studentCode);
	}
	
	/**
	 * This Method will give the paginated getSchoolSearch as response.
	 * 
	 * @param pageNumber
	 * @param pageLimit
	 * @param filter
	 * @param context
	 * @return Page<SchoolSearch>
	 */
	@GraphQLQuery(name = PGE_GRAPHQL_QUERY_GET_SCHOOLSEARCH, description = "Search school in elastic search")
	public Page<SchoolSearch> getSchoolSearch(@GraphQLArgument(name = "offset", defaultValue = "0") Integer pageNumber,
			@GraphQLArgument(name = "limit", defaultValue = "15") Integer pageLimit,
			@GraphQLArgument(name = "filter", defaultValue = "{}") String query,
			@GraphQLRootContext AuthContext context) {
		Pageable pageable = PageRequest.of(pageNumber, pageLimit);
		return schoolBridgeService.search(pageable, query);
	}

}
