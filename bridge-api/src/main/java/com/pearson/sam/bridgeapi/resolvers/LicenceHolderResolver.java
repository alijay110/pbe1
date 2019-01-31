package com.pearson.sam.bridgeapi.resolvers;

import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.ALLOCATE_LICENCE_TO_LICENCE_HOLDER_FOR_ORG;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.ALLOCATE_LICENCE_TO_LICENCE_HOLDER_FOR_SCHOOL;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_MUTATION_ADD_LICENCE_HOLDER;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_MUTATION_GET_LICENCE_HOLDER;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.PGE_GRAPHQL_QUERY_GET_LICENCE_HOLDER;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.UPDATE_LICENCE_HOLDER;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.UPDATE_LICENCE_HOLDER_BY_ORGID;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.UPDATE_LICENCE_HOLDER_BY_SCHOOLID;

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
import com.pearson.sam.bridgeapi.ibridgeservice.ILicenceHolderBridgeService;
import com.pearson.sam.bridgeapi.model.LicenceHolder;
import com.pearson.sam.bridgeapi.model.MethodType;
import com.pearson.sam.bridgeapi.model.Sort;
import com.pearson.sam.bridgeapi.util.Utils;
import com.pearson.sam.bridgeapi.validators.ModelValidator;

import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.GraphQLRootContext;

@Component
public class LicenceHolderResolver {
	private static final Logger logger = LoggerFactory.getLogger(LicenceHolderResolver.class);
	private static final String SCHOOL = "SCHOOL";
	private static final String ORGANISATION = "ORGANISATION";
	private static final String LICENCEHOLDER = "LICENCEHOLDER";

	@Autowired
	private ILicenceHolderBridgeService licenceHolderBridgeService;
	
	@Autowired
	ModelValidator validator;

	@GraphQLMutation(name = GRAPHQL_MUTATION_ADD_LICENCE_HOLDER)
	public LicenceHolder addLicenceHolder(@GraphQLArgument(name = "data") LicenceHolder licenceHolder) {
		validator.validateModel(licenceHolder, MethodType.CREATE.toString());
		return licenceHolderBridgeService.create(licenceHolder);
	}

	@GraphQLQuery(name = GRAPHQL_MUTATION_GET_LICENCE_HOLDER)
	public LicenceHolder getLicenceHolderById(@GraphQLArgument(name = "licenceHolderId") String licenceHolderId) {
		LicenceHolder licenceHolder = new LicenceHolder();
		licenceHolder.setLicenceHolderId(licenceHolderId);
		return licenceHolderBridgeService.fetch(licenceHolder);
	}

	@GraphQLMutation(name = UPDATE_LICENCE_HOLDER, description = "Update  Licence Holder")
	public LicenceHolder updateLicenceHolder(@GraphQLArgument(name = "licenceHolderId") String licenceHolderId,
			@GraphQLArgument(name = "data") LicenceHolder licenceHolder) {
		licenceHolder = Utils.getLicenceHolder(licenceHolder, LICENCEHOLDER);
		licenceHolder.setLicenceHolderId(licenceHolderId);
		validator.validateModel(licenceHolder, MethodType.CREATE.toString());
		return licenceHolderBridgeService.update(licenceHolder, LICENCEHOLDER);
	}

	@GraphQLMutation(name = UPDATE_LICENCE_HOLDER_BY_ORGID, description = "update Licence Holder By OrganisationId")
	public LicenceHolder updateLicenceHolderByOrganisationId(
			@GraphQLArgument(name = "organisationId") String organisationId,
			@GraphQLArgument(name = "data") LicenceHolder licenceHolder) {
		licenceHolder = Utils.getLicenceHolder(licenceHolder, ORGANISATION);
		licenceHolder.getHolder().getOrganisation().setOrganisationId(organisationId);
		validator.validateModel(licenceHolder, MethodType.CREATE.toString());
		return licenceHolderBridgeService.update(licenceHolder, ORGANISATION);
	}

	@GraphQLMutation(name = UPDATE_LICENCE_HOLDER_BY_SCHOOLID, description = "update Licence Holder By SchoolId")
	public LicenceHolder updateLicenceHolderBySchoolId(@GraphQLArgument(name = "schoolId") String schoolId,
			@GraphQLArgument(name = "data") LicenceHolder licenceHolder) {
		licenceHolder = Utils.getLicenceHolder(licenceHolder, SCHOOL);
		licenceHolder.getHolder().getSchool().setSchoolId(schoolId);
		validator.validateModel(licenceHolder, MethodType.CREATE.toString());
		return licenceHolderBridgeService.update(licenceHolder, SCHOOL);
	}

	@GraphQLMutation(name = ALLOCATE_LICENCE_TO_LICENCE_HOLDER_FOR_SCHOOL, description = "Allocate Licence to  Licence Holder For School")
	public LicenceHolder allocateLicenceToLicenceHolderForSchool(
			@GraphQLArgument(name = "data") LicenceHolder licenceHolder) {
		licenceHolder = Utils.getLicenceHolder(licenceHolder, SCHOOL);
		validator.validateModel(licenceHolder, MethodType.CREATE.toString());
		return licenceHolderBridgeService.update(licenceHolder, SCHOOL);
	}

	@GraphQLMutation(name = ALLOCATE_LICENCE_TO_LICENCE_HOLDER_FOR_ORG, description = "Allocate Licence to  Licence Holder for Organization")
	public LicenceHolder allocateLicenceToLicenceHolderForOrganisation(
			@GraphQLArgument(name = "data") LicenceHolder licenceHolder) {
		licenceHolder = Utils.getLicenceHolder(licenceHolder, ORGANISATION);
		validator.validateModel(licenceHolder, MethodType.CREATE.toString());
		return licenceHolderBridgeService.update(licenceHolder, ORGANISATION);
	}

	/**
	 * getPaginatedHolderLicenceTableData.
	 */
	@GraphQLQuery(name = PGE_GRAPHQL_QUERY_GET_LICENCE_HOLDER)
	public Page<LicenceHolder> getPaginatedHolderLicenceTableData(
			@GraphQLArgument(name = "offset", defaultValue = "0") Integer pageNumber,
			@GraphQLArgument(name = "limit", defaultValue = "15") Integer pageLimit,
			@GraphQLArgument(name = "filter", defaultValue = "{}") LicenceHolder filter,
			@GraphQLArgument(name = "filterPattern", defaultValue = "5") StringMatcher sm,
			@GraphQLArgument(name = "sort", defaultValue = "{\"field\":\"licenceHolderId\",\"order\":\"ASC\"}") Sort sort,
			@GraphQLRootContext AuthContext context) {

		if ("licenceHolderId".equals(sort.getField())) {
			sort.setField("createdDate");
		}

		Pageable pageable = PageRequest.of(pageNumber, pageLimit, Direction.valueOf(sort.getOrder().toString()),
				sort.getField());
		return licenceHolderBridgeService.pageIt(pageable,
				Example.of(filter, ExampleMatcher.matching().withStringMatcher(sm)));
	}

}
