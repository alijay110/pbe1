package com.pearson.sam.bridgeapi.resolvers;

import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_MUTATE_UPDATE_ORGANISATION;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_QUERY_GET_MULTIPLE_ORGANISATION_BY_ID;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_QUERY_ORGANISATION_PAGINATED;

import java.util.List;

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
import com.pearson.sam.bridgeapi.ibridgeservice.IOrganisationBridgeService;
import com.pearson.sam.bridgeapi.model.MethodType;
import com.pearson.sam.bridgeapi.model.Organisation;
import com.pearson.sam.bridgeapi.model.Sort;
import com.pearson.sam.bridgeapi.validators.ModelValidator;

import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.GraphQLRootContext;

@Component
public class OrganisationResolver {

	private static final Logger logger = LoggerFactory.getLogger(OrganisationResolver.class);

	@Autowired
	IOrganisationBridgeService organisationBridgeService;

	@Autowired
	ModelValidator validator;

	@GraphQLMutation
	public Organisation createOrganisation(@GraphQLArgument(name = "data") Organisation data) {
		//logger.info("Creating a Organisation in resolver {}");
		validator.validateModel(data, MethodType.CREATE.toString());
		return organisationBridgeService.create(data);
	}

	@GraphQLMutation(name = GRAPHQL_MUTATE_UPDATE_ORGANISATION)
	public Organisation updateOrganisation(@GraphQLArgument(name = "organisationId") String organisationId,
			@GraphQLArgument(name = "data") Organisation data) {
		//logger.info("Updating a Organisation in resolver {}");
		validator.validateModel(data, MethodType.CREATE.toString());
		data.setOrganisationId(organisationId);
		return organisationBridgeService.update(data);
	}

	/**
	 * getPaginatedOrganisation.
	 * 
	 * @return
	 */
	@GraphQLQuery(name = GRAPHQL_QUERY_ORGANISATION_PAGINATED)
	public Page<Organisation> getPaginatedOrganisationTableData(
			@GraphQLArgument(name = "offset", defaultValue = "0") Integer pageNumber,
			@GraphQLArgument(name = "limit", defaultValue = "15") Integer pageLimit,
			@GraphQLArgument(name = "filter", defaultValue = "{}") Organisation filter,
			@GraphQLArgument(name = "filterPattern", defaultValue = "5") StringMatcher sm,
			@GraphQLArgument(name = "sort", defaultValue = "{\"field\":\"organisationId\",\"order\":\"ASC\"}") Sort sort,
			@GraphQLRootContext AuthContext context) {

		Pageable pageable = PageRequest.of(pageNumber, pageLimit, Direction.valueOf(sort.getOrder().toString()),
				sort.getField());
		return organisationBridgeService.pageIt(pageable,
				Example.of(filter, ExampleMatcher.matching().withStringMatcher(sm)));
	}

	/**
	 * getMultipleOrganisationByIds.
	 * 
	 * @return
	 */
	@GraphQLQuery(name = GRAPHQL_QUERY_GET_MULTIPLE_ORGANISATION_BY_ID)
	public Page<Organisation> getMultipleOrganisationByIds(
			@GraphQLArgument(name = "offset", defaultValue = "0") Integer pageNumber,
			@GraphQLArgument(name = "limit", defaultValue = "15") Integer pageLimit,
			@GraphQLArgument(name = "filter", defaultValue = "{}") Organisation filter,
			@GraphQLArgument(name = "sort", defaultValue = "{\"field\":\"organisationId\",\"order\":\"ASC\"}") Sort sort,
			@GraphQLArgument(name = "organisationIds") List<String> ids, @GraphQLRootContext AuthContext context) {
		Pageable pageable = PageRequest.of(pageNumber, pageLimit, Direction.valueOf(sort.getOrder().toString()),
				sort.getField());
		return organisationBridgeService.pageIt(pageable, "", ids);
	}
}
