package com.pearson.sam.bridgeapi.resolvers;

import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.PGE_GRAPHQL_QUERY_GET_ACCESS_CODE_HISTORY;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.UPDATE_ACCESS_CODE_HISTORY;

import com.pearson.sam.bridgeapi.config.AuthContext;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGeneralException;
import com.pearson.sam.bridgeapi.ibridgeservice.IAccessCodeHistoryBridgeService;
import com.pearson.sam.bridgeapi.model.AccessCodesHistory;
import com.pearson.sam.bridgeapi.model.MethodType;
import com.pearson.sam.bridgeapi.model.Sort;
import com.pearson.sam.bridgeapi.validators.AccessCodesValidator;
import com.pearson.sam.bridgeapi.validators.ModelValidator;

import io.jsonwebtoken.lang.Collections;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.GraphQLRootContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

@Component
public class AccessCodeHistoryResolver {

	private static final Logger logger = LoggerFactory.getLogger(AccessCodeHistoryResolver.class);
	
	@Autowired
	private IAccessCodeHistoryBridgeService accessCodeHistoryBridgeService;
	
	@Autowired
	private AccessCodesValidator accessCodesValidator;
	
	@Autowired
	private ModelValidator validator;

	@GraphQLMutation(name = UPDATE_ACCESS_CODE_HISTORY, description = "Create Access Code History")
	public AccessCodesHistory addAccessCodeHistory(
			@GraphQLArgument(name = "data") AccessCodesHistory accessCodesHistory) {
		
		validator.validateModel(accessCodesHistory, MethodType.CREATE.toString());
		validator.validateModel(accessCodesHistory.getAlteration(), MethodType.CREATE.toString());
		accessCodesValidator.isAccessCodeEmpty(accessCodesHistory.getAlteration().getAccessCode());
		if(!accessCodesHistory.getAccessCode().equals(accessCodesHistory.getAlteration().getAccessCode())) {
			 //logger.error("AccessCodeHistory Access Code:"+accessCodesHistory.getAccessCode() +" is not mathcing with AccessCodeModel Access Code:"+accessCodesHistory.getAlteration().getAccessCode());
		      throw new BridgeApiGeneralException("AccessCodeHistory Access Code:"+accessCodesHistory.getAccessCode() +" is not mathcing with AccessCodeModel Access Code:"+accessCodesHistory.getAlteration().getAccessCode());
		}
		accessCodesValidator.validateAccessCode(accessCodesHistory.getAccessCode());
		accessCodesValidator.isValidAccessCodeType(accessCodesHistory.getAlteration().getType());
		if(!Collections.isEmpty(accessCodesHistory.getAlteration().getProducts())) {
			for(String productId:accessCodesHistory.getAlteration().getProducts()) {
				accessCodesValidator.validateProduct(productId);
			}
		}
		return accessCodeHistoryBridgeService.create(accessCodesHistory);
	}

	/**
	 * getPaginatedAccessCodeTableData.
	 * 
	 * @return
	 */
	@GraphQLQuery(name = PGE_GRAPHQL_QUERY_GET_ACCESS_CODE_HISTORY)
	public Page<AccessCodesHistory> getPaginatedAccessCodeTableData(
			@GraphQLArgument(name = "offset", defaultValue = "0") Integer pageNumber,
			@GraphQLArgument(name = "limit", defaultValue = "15") Integer pageLimit,
			@GraphQLArgument(name = "filter", defaultValue = "{}") AccessCodesHistory filter,
			@GraphQLArgument(name = "filterPattern", defaultValue = "5") StringMatcher sm,
			@GraphQLArgument(name = "sort", defaultValue = "{\"field\":\"accessCode\",\"order\":\"ASC\"}") Sort sort,
			@GraphQLRootContext AuthContext context) {
		Pageable pageable = PageRequest.of(pageNumber, pageLimit, Direction.valueOf(sort.getOrder().toString()),
				sort.getField());
		return accessCodeHistoryBridgeService.pageIt(pageable,
				Example.of(filter, ExampleMatcher.matching().withStringMatcher(sm)));
	}

}
