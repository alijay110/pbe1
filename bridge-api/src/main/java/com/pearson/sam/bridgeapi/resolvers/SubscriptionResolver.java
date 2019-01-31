package com.pearson.sam.bridgeapi.resolvers;

import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.CREATE_ACCESS_CODE_SUBSCRIPTION;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.CREATE_VOUCHER_CODE_SUBSCRIPTION;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GET_ACCESS_CODE_SUBSCRIPTION;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.HYPHEN;

import com.pearson.sam.bridgeapi.exceptions.BridgeApiGeneralException;
import com.pearson.sam.bridgeapi.ibridgeservice.ISubscriptionBridgeService;
import com.pearson.sam.bridgeapi.model.AccessCodes;
import com.pearson.sam.bridgeapi.model.MethodType;
import com.pearson.sam.bridgeapi.model.Voucher;
import com.pearson.sam.bridgeapi.util.Utils;
import com.pearson.sam.bridgeapi.validators.SubscriptionValidator;

import io.jsonwebtoken.lang.Collections;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionResolver  {

	private static final Logger logger = LoggerFactory.getLogger(SubscriptionResolver.class);
	
	@Autowired
	private ISubscriptionBridgeService subscriptionBridgeService;
	
	@Autowired
	private SubscriptionValidator subscriptionValidator;
	
	@GraphQLMutation(name = CREATE_ACCESS_CODE_SUBSCRIPTION, description = "Create Access Codes Subscription")
	public AccessCodes generateMultipleAccessCodes(@GraphQLArgument(name = "accessCode") String accessCode ,@GraphQLArgument(name = "data") AccessCodes accessCodes) {
		subscriptionValidator.isCreatedByEmpty(accessCodes.getCreatedBy());
		accessCodes.setAccessCode(accessCode);
		subscriptionValidator.isAccessCodeEmpty(accessCode);
		subscriptionValidator.isAccessCodeEmpty(accessCodes.getAccessCode());
		if(!accessCode.equals(accessCodes.getAccessCode())) {
			 //logger.error("Subscription Access Code:"+accessCode +" is not mathcing with AccessCodeModel Access Code:"+accessCodes.getAccessCode());
		      throw new BridgeApiGeneralException("Subscription Access Code:"+accessCode +" is not mathcing with AccessCodeModel Access Code:"+accessCodes.getAccessCode());
		}
		subscriptionValidator.validateAccessCode(accessCodes.getAccessCode());
		if(Utils.isNotEmpty(accessCodes.getType()))
			subscriptionValidator.isValidAccessCodeType(accessCodes.getType());
		if(!Collections.isEmpty(accessCodes.getProducts())) {
			for(String productId:accessCodes.getProducts()) {
				subscriptionValidator.validateProduct(productId);
			}
		}
		return subscriptionBridgeService.createAccessCodeSubscription(accessCodes);
		
	}
	
	@GraphQLMutation(name = CREATE_VOUCHER_CODE_SUBSCRIPTION, description = "Create Voucher Codes Subscription")
	public Voucher generateVoucherCodeSubscription(@GraphQLArgument(name = "voucherCode") String voucherCode ,@GraphQLArgument(name = "data") Voucher graphVoucher) {
		graphVoucher.setVoucherCode(voucherCode);
		return subscriptionBridgeService.createVoucherSubscription(graphVoucher);
		
	}
	
	//Need for the future Implementation,Yet to decide
	/*@GraphQLMutation(name = UPDATE_ACCESS_CODE_SUBSCRIPTION, description = "Update Access Codes Subscription")
	public AccessCodes updateAccessCodeSubscription(@GraphQLArgument(name = "accessCode") String accessCode ,@GraphQLArgument(name = "data") AccessCodes accessCodes) {
		accessCodes.setAccessCode(accessCode);
		return callExecute(MethodType.UPDATE, accessCodes, abstractService);
		
	}*/
	
	
	@GraphQLQuery(name = GET_ACCESS_CODE_SUBSCRIPTION, description = "get Access Codes Subscription")
	public AccessCodes getAccessCodeSubscription(@GraphQLArgument(name = "accessCode") String accessCode ,@GraphQLArgument(name = "productId") String productCode ) {
		subscriptionValidator.isAccessCodeEmpty(accessCode);
		subscriptionValidator.validateAccessCode(accessCode);
		AccessCodes accessCodes = new AccessCodes();
		accessCodes.setAccessCode(accessCode);
		accessCodes.setProducts(Arrays.asList(productCode));
		if(Utils.isNotEmpty(productCode))
			subscriptionValidator.validateProduct(productCode);
		return subscriptionBridgeService.getAccessCodeSubscription(accessCodes);
		
	}
	 
	  
}
