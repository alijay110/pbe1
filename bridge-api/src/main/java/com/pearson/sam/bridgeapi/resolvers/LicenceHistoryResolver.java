/**
 * 
 */
package com.pearson.sam.bridgeapi.resolvers;

import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_MUTATION_ADD_LICENCE_HISTORY;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.PGE_GRAPHQL_QUERY_GET_LICENCE_HISTORY;

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
import com.pearson.sam.bridgeapi.ibridgeservice.ILicenceHistoryBridgeService;
import com.pearson.sam.bridgeapi.model.LicenceHistory;
import com.pearson.sam.bridgeapi.model.MethodType;
import com.pearson.sam.bridgeapi.model.Sort;
import com.pearson.sam.bridgeapi.validators.ModelValidator;

import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.GraphQLRootContext;

/**
 * @author VGUDLSA
 *
 */
@Component
public class LicenceHistoryResolver {

	@Autowired
	private ILicenceHistoryBridgeService licenceHistoryBridgeService;

	@Autowired
	ModelValidator validator;

	@GraphQLMutation(name = GRAPHQL_MUTATION_ADD_LICENCE_HISTORY)
	public LicenceHistory addLicenceHistory(@GraphQLArgument(name = "data") LicenceHistory licenceHistory) {
		validator.validateModel(licenceHistory, MethodType.CREATE.toString());
		return licenceHistoryBridgeService.create(licenceHistory);
	}

	/**
	 * getPaginatedLicenceHistoryTableData.
	 */
	@GraphQLQuery(name = PGE_GRAPHQL_QUERY_GET_LICENCE_HISTORY)
	public Page<LicenceHistory> getPaginatedLicenceHistoryTableData(
			@GraphQLArgument(name = "offset", defaultValue = "0") Integer pageNumber,
			@GraphQLArgument(name = "limit", defaultValue = "15") Integer pageLimit,
			@GraphQLArgument(name = "filter", defaultValue = "{}") LicenceHistory filter,
			@GraphQLArgument(name = "filterPattern", defaultValue = "5") StringMatcher sm,
			@GraphQLArgument(name = "sort", defaultValue = "{\"field\":\"uid\",\"order\":\"ASC\"}") Sort sort,
			@GraphQLRootContext AuthContext context) {

		Pageable pageable = PageRequest.of(pageNumber, pageLimit, Direction.valueOf(sort.getOrder().toString()),
				sort.getField());
		return licenceHistoryBridgeService.pageIt(pageable,
				Example.of(filter, ExampleMatcher.matching().withStringMatcher(sm)));
	}

}
