package com.pearson.sam.bridgeapi.resolvers;

import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.ALLOCATE_LINCENCE_TO_USER;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.DELETE_LICENCE;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_MUTATION_ADD_LICENCE;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_MUTATION_GET_LICENCE;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.PGE_GRAPHQL_QUERY_GET_LICENCE;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.UPDATE_LICENCE;

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
import com.pearson.sam.bridgeapi.ibridgeservice.ILicenceBridgeService;
import com.pearson.sam.bridgeapi.model.Licence;
import com.pearson.sam.bridgeapi.model.MethodType;
import com.pearson.sam.bridgeapi.model.Sort;
import com.pearson.sam.bridgeapi.validators.ModelValidator;

import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.GraphQLRootContext;

@Component
public class LicenceResolver {
	
  private static final String LICENCE_ID = "licenceId";

  @Autowired
  private ILicenceBridgeService licenceBridgeService;
  
  @Autowired
  ModelValidator validator;

  @GraphQLMutation(name = GRAPHQL_MUTATION_ADD_LICENCE)
  public Licence addLicence(@GraphQLArgument(name = "data") Licence licence){
	  validator.validateModel(licence, MethodType.CREATE.toString());
	  return licenceBridgeService.create(licence);
  }
  
  @GraphQLQuery(name = GRAPHQL_MUTATION_GET_LICENCE)
  public Licence getLicenceById(@GraphQLArgument(name = "licenceId") String licenceId){
	Licence licence = new Licence();
	licence.setLicenceId(licenceId);
	validator.validateEmptyNullValue(licenceId, LICENCE_ID);
	return licenceBridgeService.fetch(licence);
  }
  
  @GraphQLMutation(name = UPDATE_LICENCE, description = "Update  Licence")
  public Licence updateAccessCode(@GraphQLArgument(name = "licenceId") String licenceId,@GraphQLArgument(name = "data") Licence licence){
	  licence.setLicenceId(licenceId);
	  validator.validateModel(licence, MethodType.CREATE.toString());
	  return licenceBridgeService.update(licence);
  }
  
  @GraphQLMutation(name = DELETE_LICENCE, description = "Detele  Licence")
  public Licence deleteLicenceById(@GraphQLArgument(name = "data") Licence licence){
	  return licenceBridgeService.delete(licence);
  }
  
  
  @GraphQLMutation(name = ALLOCATE_LINCENCE_TO_USER, description = "Update  Licence")
  public Licence allocateLicenceToUser(@GraphQLArgument(name = "data") Licence licence){
	  validator.validateModel(licence, MethodType.CREATE.toString());
	  return licenceBridgeService.update(licence);
  }

  /**
   * getPaginatedLicenceTableData.
   */
  @GraphQLQuery(name = PGE_GRAPHQL_QUERY_GET_LICENCE)
  public Page<Licence> getPaginatedLicenceTableData(
      @GraphQLArgument(name = "offset", defaultValue = "0") Integer pageNumber,
      @GraphQLArgument(name = "limit", defaultValue = "15") Integer pageLimit,
      @GraphQLArgument(name = "filter", defaultValue = "{}") Licence filter,
      @GraphQLArgument(name = "filterPattern", defaultValue = "5") StringMatcher sm,
      @GraphQLArgument(name = "sort", 
        defaultValue = "{\"field\":\"licenceId\",\"order\":\"ASC\"}") Sort sort,
      @GraphQLRootContext AuthContext context) {
	  
	    Pageable pageable = PageRequest.of(pageNumber, pageLimit,
	            Direction.valueOf(sort.getOrder().toString()), sort.getField());
	    return licenceBridgeService.pageIt(pageable,  Example.of(filter, ExampleMatcher.matching().withStringMatcher(sm)));
  }
}
