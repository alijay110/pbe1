package com.pearson.sam.bridgeapi.resolvers;

import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.CREATE_LARGER_ACCESS_CODES;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.CREATE_MULTI_ACCESS_CODES;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GET_ACCESS_CODE;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_QUERY_GET_PRODUCT_BY_ACCESS_CODE;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.PGE_GRAPHQL_QUERY_GET_ACCESSCODESEARCH;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.PGE_GRAPHQL_QUERY_GET_ACCESS_CODE;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.UPDATE_ACCESS_CODE;
import static com.pearson.sam.bridgeapi.util.Utils.copyNonNullProperties;

import com.pearson.sam.bridgeapi.config.AuthContext;
import com.pearson.sam.bridgeapi.elasticsearch.model.AccessCodeSearch;
import com.pearson.sam.bridgeapi.elasticsearch.model.UserSearch;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGraphqlException;
import com.pearson.sam.bridgeapi.ibridgeservice.IAccessCodeBridgeService;
import com.pearson.sam.bridgeapi.model.AccessCodes;
import com.pearson.sam.bridgeapi.model.MethodType;
import com.pearson.sam.bridgeapi.model.Sort;
import com.pearson.sam.bridgeapi.util.Utils;
import com.pearson.sam.bridgeapi.validators.AccessCodesValidator;
import com.pearson.sam.bridgeapi.validators.ModelValidator;

import graphql.ErrorType;

import io.jsonwebtoken.lang.Collections;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.GraphQLRootContext;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

@Component
public class AccessCodeResolver {
	
	private static final Logger logger = LoggerFactory.getLogger(AccessCodeResolver.class);
	
	@Autowired
	IAccessCodeBridgeService accessCodeBridgeService;
	
	@Autowired
	ModelValidator validator;
	
	@Autowired
	private AccessCodesValidator accessCodesValidator;

	@GraphQLMutation(name = CREATE_MULTI_ACCESS_CODES, description = "Create Bulk Access Codes")
	public List<AccessCodes> generateMultipleAccessCodes(@GraphQLArgument(name = "data") AccessCodes accessCodes) {
		validator.validateModel(accessCodes, MethodType.CREATE.toString());
		accessCodesValidator.validateAccessCode(accessCodes);
		if(Utils.isNotEmpty(accessCodes.getStartDate()) && Utils.isNotEmpty(accessCodes.getEndDate()))
			accessCodesValidator.validateStarAndEndDate(accessCodes.getStartDate(), accessCodes.getEndDate());
		return accessCodeBridgeService.generateAccessCodes(accessCodes);
	}

	@GraphQLMutation(name = CREATE_LARGER_ACCESS_CODES, description = "Create Large Access Codes")
	public AccessCodes generateLargeAccessCodes(@GraphQLArgument(name = "data") AccessCodes accessCodes) {
		validator.validateModel(accessCodes, MethodType.CREATE.toString());
		accessCodesValidator.isValidAccessCodeType(accessCodes.getType());
				AccessCodes responseaccessCodes = accessCodeBridgeService.generateAccessCodes(accessCodes).get(0);
		if (responseaccessCodes != null) {
			return responseaccessCodes;
		} else {
			throw new BridgeApiGraphqlException(ErrorType.ValidationError, "Problem while generating AccessCode");
		}
	}

	@GraphQLQuery(name = GET_ACCESS_CODE, description = "Get  Access Codes")
	public AccessCodes getAccessCode(@GraphQLArgument(name = "accessCode") String data) {
		accessCodesValidator.validateAccessCode(data);
		AccessCodes accessCodes = new AccessCodes();
		accessCodes.setCode(data);
		return accessCodeBridgeService.fetchOne(accessCodes);
	}

	@GraphQLMutation(name = UPDATE_ACCESS_CODE, description = "Update  Access Codes")
	public AccessCodes updateAccessCode(@GraphQLArgument(name = "accessCode") String code,
			@GraphQLArgument(name = "data") AccessCodes accessCodes) {
		if(Utils.isNotEmpty(code) && accessCodes!=null && Utils.isNotEmpty(accessCodes.getAccessCode()) && !code.equals(accessCodes.getAccessCode())) {
			 //logger.error("Access Code:"+code +" is not mathcing with AccessCode Model Access Code:"+accessCodes.getAccessCode());
		      //throw new BridgeApiGeneralException("Access Code:"+code +" is not macthing with AccessCode Model Access Code:"+accessCodes.getAccessCode());
		}
		accessCodes.setCode(code);
		accessCodes.setAccessCode(code);
		validator.validateModel(accessCodes, MethodType.CREATE.toString());
		if(!Collections.isEmpty(accessCodes.getProducts())) {
			for(String productId:accessCodes.getProducts()) {
				accessCodesValidator.validateProduct(productId);
			}
		}
		accessCodesValidator.validateAccessCode(code);
		return accessCodeBridgeService.update(accessCodes);
	}

	@GraphQLQuery(name = GRAPHQL_QUERY_GET_PRODUCT_BY_ACCESS_CODE)
	public AccessCodes getProductsByAccessCode(@GraphQLArgument(name = "accessCode") String data,
			@GraphQLRootContext AuthContext context) {
		accessCodesValidator.validateAccessCode(data);
		AccessCodes accessCodes = new AccessCodes();
		accessCodes.setCode(data);
		accessCodes.setAccessCode(data);
		return accessCodeBridgeService.fetchOne(accessCodes);
	}

	
	 /**
   * getPaginatedAccessCodeTableData.
   * 
   * @return
   */
  @GraphQLQuery(name = PGE_GRAPHQL_QUERY_GET_ACCESS_CODE)
  public Page<AccessCodes> getPaginatedAccessCodeTableData(
      @GraphQLArgument(name = "offset", defaultValue = "0") Integer pageNumber,
      @GraphQLArgument(name = "limit", defaultValue = "15") Integer pageLimit,
      @GraphQLArgument(name = "filter", defaultValue = "{}") AccessCodes filter,
      @GraphQLArgument(name = "filterPattern", defaultValue = "5") StringMatcher sm,
      @GraphQLArgument(name = "sort", defaultValue = "{\"field\":\"accessCode\",\"order\":\"ASC\"}") Sort sort,
      @GraphQLRootContext AuthContext context) {
    Pageable pageable = PageRequest.of(pageNumber, pageLimit,
        Direction.valueOf(sort.getOrder().toString()), sort.getField());
    AccessCodeSearch query = new AccessCodeSearch();
    copyNonNullProperties(filter,query);
    Page<AccessCodeSearch> oldPage = accessCodeBridgeService.pageIt(pageable, query,sm);
    return new PageImpl<>(oldPage.getContent().stream().map(accsCodeSrch -> new AccessCodes(accsCodeSrch)).collect(Collectors.toList()), pageable,
        oldPage.getTotalElements());
  }
  
  /**
   * This Method will give the paginated getAccessCodeSearch as response.
   * 
   * @param pageNumber
   * @param pageLimit
   * @param filter
   * @param context
   * @return Page<AccessCodeSearch>
   */
  @GraphQLQuery(name = PGE_GRAPHQL_QUERY_GET_ACCESSCODESEARCH, description = "Search accessCode in elastic search")
  public Page<AccessCodeSearch> getAccessCodeSearch(@GraphQLArgument(name = "offset", defaultValue = "0") Integer pageNumber,
      @GraphQLArgument(name = "limit", defaultValue = "15") Integer pageLimit,
      @GraphQLArgument(name = "filter", defaultValue = "{}") String query,
      @GraphQLRootContext AuthContext context) {
    Pageable pageable = PageRequest.of(pageNumber, pageLimit);
    return accessCodeBridgeService.search(pageable, query);
  }

}
