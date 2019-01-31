package com.pearson.sam.bridgeapi.resolvers;

import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_MUTATION_ADD_LICENCE_HOLDER_HISTORY;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_MUTATION_GET_LICENCE_HOLDER_HISTORY;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.PGE_GRAPHQL_QUERY_GET_LICENCE_HOLDER_HISTORY;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.UPDATE_LICENCE_HOLDER_HISTORY;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import com.github.wnameless.json.flattener.JsonFlattener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pearson.sam.bridgeapi.config.AuthContext;
import com.pearson.sam.bridgeapi.ibridgeservice.ILicenceHolderHistoryBridgeService;
import com.pearson.sam.bridgeapi.model.LicenceHolderHistory;
import com.pearson.sam.bridgeapi.model.MethodType;
import com.pearson.sam.bridgeapi.model.Sort;
import com.pearson.sam.bridgeapi.validators.ClientValidationStrategy;
import com.pearson.sam.bridgeapi.validators.ModelValidator;
import com.pearson.sam.bridgeapi.validators.ValidationContext;
import com.pearson.sam.bridgeapi.validators.ValidationStrategy;

import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.GraphQLRootContext;

@Component
public class LicenceHolderHistoryResolver {

  @Autowired
  private ILicenceHolderHistoryBridgeService licenceHolderHistoryBridgeService;
  
  @Autowired
  private ValidationContext validationContext;
  
  @Autowired
  ModelValidator validator;
  

  @GraphQLMutation(name = GRAPHQL_MUTATION_ADD_LICENCE_HOLDER_HISTORY)
  public LicenceHolderHistory addLicenceHolderHistory(@GraphQLArgument(name = "data") LicenceHolderHistory licenceHolderHistory){
	  //setValidationContextforCreate(licenceHolderHistory);
	  validator.validateModel(licenceHolderHistory, MethodType.CREATE.toString());
	  return licenceHolderHistoryBridgeService.create(licenceHolderHistory);
  }
  
  @GraphQLQuery(name = GRAPHQL_MUTATION_GET_LICENCE_HOLDER_HISTORY)
  public LicenceHolderHistory getLicenceHolderHistoryById(@GraphQLArgument(name = "licenceHolderHistoryId") String licenceHolderHistoryId){
	LicenceHolderHistory licenceHolderHistory = new LicenceHolderHistory();
	licenceHolderHistory.setLicenceHolderHistoryId(licenceHolderHistoryId);
	return licenceHolderHistoryBridgeService.fetch(licenceHolderHistory);
  }
  
  @GraphQLMutation(name = UPDATE_LICENCE_HOLDER_HISTORY, description = "Update  Licence Holder History")
  public LicenceHolderHistory updateLicenceHolderHistory(@GraphQLArgument(name = "licenceHolderHistoryId") String licenceHolderHistoryId,@GraphQLArgument(name = "data") LicenceHolderHistory licenceHolderHistory){
	  licenceHolderHistory.setLicenceHolderHistoryId(licenceHolderHistoryId);
	  validator.validateModel(licenceHolderHistory, MethodType.CREATE.toString());
	  return licenceHolderHistoryBridgeService.update(licenceHolderHistory);
  }
  
  /**
   * getPaginatedLicenceHolderHistoryTableData.
   */
  @GraphQLQuery(name = PGE_GRAPHQL_QUERY_GET_LICENCE_HOLDER_HISTORY)
  public Page<LicenceHolderHistory> getPaginatedLicenceHolderHistoryTableData(
      @GraphQLArgument(name = "offset", defaultValue = "0") Integer pageNumber,
      @GraphQLArgument(name = "limit", defaultValue = "15") Integer pageLimit,
      @GraphQLArgument(name = "filter", defaultValue = "{}") LicenceHolderHistory filter,
      @GraphQLArgument(name = "filterPattern", defaultValue = "5") StringMatcher sm,
      @GraphQLArgument(name = "sort",
        defaultValue = "{\"field\":\"licenceHolderHistoryId\",\"order\":\"ASC\"}") Sort sort,
      @GraphQLRootContext AuthContext context) {

    Pageable pageable = PageRequest.of(pageNumber, pageLimit,
        Direction.valueOf(sort.getOrder().toString()), sort.getField());
    return licenceHolderHistoryBridgeService.pageIt(pageable, Example.of(filter, ExampleMatcher.matching().withStringMatcher(sm)));
  }
  
  
  //VALIDATION logic should be handled in client.. Temp fix for now
  private void setValidationContextforCreate(LicenceHolderHistory licenceHolderHistory) {
		Map<String,ValidationStrategy> validationStrategies = new HashMap<>();
		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(licenceHolderHistory);
		Map<String, Object> flattenJson = JsonFlattener.flattenAsMap(json);
		flattenJson.entrySet().stream().filter(entry-> (entry.getValue()!=null && !entry.getValue().toString().trim().isEmpty())).forEach(entry->{
			
			if(entry.getKey().endsWith("Date")) {
				validationStrategies.put(entry.getKey()+ClientValidationStrategy.TIMESTAMP_VALIDATION+entry.getValue(),ClientValidationStrategy.TIMESTAMP_VALIDATION);
			}
			else if(entry.getKey().endsWith("quantity")) {
				validationStrategies.put(entry.getKey()+ClientValidationStrategy.NUMERIC_VALIDATION+entry.getValue(),ClientValidationStrategy.NUMERIC_VALIDATION);
			}
			else if(!entry.getKey().toLowerCase().endsWith("name")){
				validationStrategies.put(entry.getKey()+ClientValidationStrategy.ALPHANUMERIC_VALIDATION+entry.getValue(),ClientValidationStrategy.ALPHANUMERIC_VALIDATION);
			}
				
		});
		validationContext.setValidationContext(validationStrategies);
		validationContext.executeValidationStrategy(licenceHolderHistory);
  }

}
