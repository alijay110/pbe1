package com.pearson.sam.bridgeapi.resolvers;

import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.PGE_GRAPHQL_QUERY_GET_VOUCHER_HISTORY;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.UPDATE_VOUCHER_CODE_HISTORY;

import java.util.Optional;

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

import com.pearson.sam.bridgeapi.config.AuthContext;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGeneralException;
import com.pearson.sam.bridgeapi.ibridgeservice.IVoucherCodeHistoryBridgeService;
import com.pearson.sam.bridgeapi.model.MethodType;
import com.pearson.sam.bridgeapi.model.Sort;
import com.pearson.sam.bridgeapi.model.VoucherCodeHistory;
import com.pearson.sam.bridgeapi.validators.ModelValidator;
import com.pearson.sam.bridgeapi.validators.VoucherCodesValidator;

import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.GraphQLRootContext;

@Component
public class VoucherHistoryResolver{
	
	private static final Logger logger = LoggerFactory.getLogger(VoucherHistoryResolver.class);
	
	@Autowired
	IVoucherCodeHistoryBridgeService voucherCodeHistoryBridgeService;
	
	@Autowired
	private VoucherCodesValidator voucherCodesValidator;
	
	@Autowired
	private ModelValidator validator;
	
	@GraphQLMutation(name = UPDATE_VOUCHER_CODE_HISTORY, description = "Update Voucher Code History")
	public VoucherCodeHistory updateVoucheristory(@GraphQLArgument(name="data") VoucherCodeHistory voucherHistory) {
		validator.validateModel(voucherHistory, MethodType.CREATE.toString());
		validator.validateModel(voucherHistory.getAlteration(), MethodType.CREATE.toString());
		if( Optional.ofNullable(voucherHistory.getAlteration().getVoucherCode()).isPresent() && !voucherHistory.getVoucherCode().equals(voucherHistory.getAlteration().getVoucherCode())) {
			 //logger.error("VoucherHistory Voucher Code:"+voucherHistory.getVoucherCode() +" is not mathcing with VoucherCode Model Voucher Code:"+voucherHistory.getAlteration().getVoucherCode());
		      throw new BridgeApiGeneralException("VoucherHistory Voucher Code:"+voucherHistory.getVoucherCode() +" is not mathcing with VoucherCode Model Voucher Code:"+voucherHistory.getAlteration().getVoucherCode());
		}
		voucherCodesValidator.validateVoucherCode(voucherHistory.getVoucherCode());
		if (Optional.ofNullable(voucherHistory.getAlteration().getType()).isPresent()) 
			voucherCodesValidator.isValidVoucherCodeType(voucherHistory.getAlteration().getType());			
		return voucherCodeHistoryBridgeService.create(voucherHistory);
	}
	
	@GraphQLQuery(name = PGE_GRAPHQL_QUERY_GET_VOUCHER_HISTORY)
	  public Page<VoucherCodeHistory> getPaginatedVoucherCodeTableData(
	      @GraphQLArgument(name = "offset", defaultValue = "0") Integer pageNumber,
	      @GraphQLArgument(name = "limit", defaultValue = "15") Integer pageLimit,
	      @GraphQLArgument(name = "filter", defaultValue = "{}") VoucherCodeHistory filter,
	      @GraphQLArgument(name = "filterPattern", defaultValue = "5") StringMatcher sm,
	      @GraphQLArgument(name = "sort", defaultValue = "{\"field\":\"accessCode\",\"order\":\"ASC\"}") Sort sort,
	      @GraphQLRootContext AuthContext context) {

	    // donot allow to sort by voucherId
	    if (sort.getField().equals("voucherId")) {
	      sort.setField("createdDate");
	    }

	    Pageable pageable = PageRequest.of(pageNumber, pageLimit,
	        Direction.valueOf(sort.getOrder().toString()), sort.getField());
	    return voucherCodeHistoryBridgeService.pageIt(pageable,Example.of(filter, ExampleMatcher.matching().withStringMatcher(sm)));
	  }
}
