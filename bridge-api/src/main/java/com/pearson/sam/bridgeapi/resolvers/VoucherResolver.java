package com.pearson.sam.bridgeapi.resolvers;

import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.CREATE_LARGENUMBER_VOUCHER_CODES;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.CREATE_MULTI_VOUCHER_CODES;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.CREATE_VOUCHER_CODE;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GET_VOUCHER_CODE;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.PGE_GRAPHQL_QUERY_GET_VOUCHER;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.PGE_GRAPHQL_QUERY_GET_VOUCHERSEARCH;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.UPDATE_VOUCHER_CODE;
import static com.pearson.sam.bridgeapi.util.Utils.copyNonNullProperties;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import com.pearson.sam.bridgeapi.config.AuthContext;
import com.pearson.sam.bridgeapi.elasticsearch.model.UserSearch;
import com.pearson.sam.bridgeapi.elasticsearch.model.VoucherCodeSearch;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGraphqlException;
import com.pearson.sam.bridgeapi.ibridgeservice.IVoucherBridgeService;
import com.pearson.sam.bridgeapi.model.MethodType;
import com.pearson.sam.bridgeapi.model.Sort;
import com.pearson.sam.bridgeapi.model.Voucher;
import com.pearson.sam.bridgeapi.validators.ModelValidator;
import com.pearson.sam.bridgeapi.validators.VoucherCodesValidator;

import graphql.ErrorType;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.GraphQLRootContext;
import io.leangen.graphql.util.Utils;

@Component
public class VoucherResolver {

	@Autowired
	IVoucherBridgeService voucherBridgeService;
	
	@Autowired
	private VoucherCodesValidator voucherCodesValidator;

	@Autowired
	ModelValidator validator;

	@GraphQLMutation(name = CREATE_MULTI_VOUCHER_CODES, description = "Create Bulk Voucher Codes")
	public List<Voucher> generateMultipleVoucherCodes(@GraphQLArgument(name = "data") Voucher voucher) {
		validator.validateModel(voucher, MethodType.CREATE.toString());
		voucherCodesValidator.validateVoucherCodeQuantity(voucher);
		return voucherBridgeService.createMultiple(voucher);
	}

	/**
	 * generateLargeNumberOfVoucherCodes.
	 * 
	 * @return
	 */
	@GraphQLMutation(name = CREATE_LARGENUMBER_VOUCHER_CODES, description = "Create Bulk Voucher Codes")
	public Voucher generateLargeNumberOfVoucherCodes(@GraphQLArgument(name = "data") Voucher voucher) {
		validator.validateModel(voucher, MethodType.CREATE.toString());
		voucherCodesValidator.validateVoucherCodeQuantity(voucher);
		Voucher createMultiple = voucherBridgeService.createMultiple(voucher).get(0);
		if (createMultiple != null) {
			return createMultiple;
		} else {
			throw new BridgeApiGraphqlException(ErrorType.ValidationError, "Problem while generating Voucher Code");
		}
	}

	@GraphQLMutation(name = CREATE_VOUCHER_CODE, description = "Create  Voucher Codes")
	public Voucher generateVouchers(@GraphQLArgument(name = "data") Voucher voucher) {
		validator.validateModel(voucher, MethodType.CREATE.toString());
		voucherCodesValidator.validateVoucherCodeQuantity(voucher);
		return voucherBridgeService.create(voucher);
	}

	/**
	 * getVoucher.
	 * 
	 * @return
	 */
	@GraphQLQuery(name = GET_VOUCHER_CODE, description = "Get  Voucher Codes")
	public Voucher getVoucher(@GraphQLArgument(name = "voucherId") String data) {
		Voucher voucher = new Voucher();
		voucher.setVoucherCode(data);
		return voucherBridgeService.fetchOne(voucher);
	}

	@GraphQLMutation(name = UPDATE_VOUCHER_CODE, description = "Update  Voucher Codes")
	public Voucher updateVoucher(@GraphQLArgument(name = "voucherCode") String code,
			@GraphQLArgument(name = "data") Voucher voucher) {
		voucher.setVoucherCode(code);
		validator.validateModel(voucher, MethodType.CREATE.toString());
		if(Utils.isNotEmpty(voucher.getType()))
			voucherCodesValidator.isValidVoucherCodeType(voucher.getType());
		return voucherBridgeService.update(voucher);
	}

	@GraphQLQuery(name = PGE_GRAPHQL_QUERY_GET_VOUCHER)
	public Page<Voucher> getPaginatedVoucherCodeTableData(
			@GraphQLArgument(name = "offset", defaultValue = "0") Integer pageNumber,
			@GraphQLArgument(name = "limit", defaultValue = "15") Integer pageLimit,
      @GraphQLArgument(name = "filter", defaultValue = "{}") Voucher filter,
      @GraphQLArgument(name = "filterPattern", defaultValue = "5") StringMatcher sm,
			@GraphQLArgument(name = "sort", defaultValue = "{\"field\":\"accessCode\",\"order\":\"ASC\"}") Sort sort,
			@GraphQLRootContext AuthContext context) {

		// donot allow to sort by voucherId
		if ("voucherId".equals(sort.getField())) {
			sort.setField("createdDate");
		}

    Pageable pageable = PageRequest.of(pageNumber, pageLimit,
        Direction.valueOf(sort.getOrder().toString()), sort.getField());
    UserSearch query = new UserSearch();
    copyNonNullProperties(filter,query);
    Page<VoucherCodeSearch> oldPage = voucherBridgeService.pageIt(pageable,query,sm);
    return new PageImpl<>(oldPage.getContent().stream().map(voucherSrch -> new Voucher(voucherSrch)).collect(Collectors.toList()), pageable,
        oldPage.getTotalElements());

		
	}
	
	/**
	 * This Method will give the paginated getVoucherCodeSearch as response.
	 * 
	 * @param pageNumber
	 * @param pageLimit
	 * @param filter
	 * @param context
	 * @return Page<VoucherCodeSearch>
	 */
	@GraphQLQuery(name = PGE_GRAPHQL_QUERY_GET_VOUCHERSEARCH, description = "Search VoucherCode in elastic search")
	public Page<VoucherCodeSearch> getVoucherCodeSearch(@GraphQLArgument(name = "offset", defaultValue = "0") Integer pageNumber,
			@GraphQLArgument(name = "limit", defaultValue = "15") Integer pageLimit,
			@GraphQLArgument(name = "filter", defaultValue = "{}") String query,
			@GraphQLRootContext AuthContext context) {
		Pageable pageable = PageRequest.of(pageNumber, pageLimit);
		return voucherBridgeService.search(pageable, query);
	}

}
