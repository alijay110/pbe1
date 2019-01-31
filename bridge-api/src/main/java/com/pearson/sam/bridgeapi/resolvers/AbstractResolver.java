/*package com.pearson.sam.bridgeapi.resolvers;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGeneralException;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGraphqlException;
import com.pearson.sam.bridgeapi.model.BaseModel;
import com.pearson.sam.bridgeapi.model.KeyType;
import com.pearson.sam.bridgeapi.model.MethodType;
import com.pearson.sam.bridgeapi.serviceimpl.AbstractService;

import graphql.ErrorType;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.client.HttpClientErrorException;

public abstract class AbstractResolver<T extends BaseModel> {

  private static final Logger logger = LoggerFactory.getLogger(AbstractResolver.class);

  public Page<T> pageIt(AbstractService<T> abstractService, Pageable pageable, Example<T> e) {
    return abstractService.pageIt(pageable, e);
  }

  public Page<T> pageIt(AbstractService<T> abstractService, Pageable pageable, String inputString,
      List<String> inputList) {
    return abstractService.pageIt(pageable, inputString, inputList);
  }

  protected List<T> callExecute(AbstractService<T> abstractService, KeyType key, List<String> ids) {
    //logger.info("Request: {}", MethodType.FETCH_MULTIPLE);
    List<T> response = null;
      response = abstractService.execute(key, ids);
      //logger.info("Response : {}", response);
      return response;
  }

  protected T callExecute(MethodType method, T obj, AbstractService<T> abstractService) {
    //logger.info("Request: {}", obj);
    T response = null;
      response = abstractService.execute(obj, method);
      //logger.info("Response : {}", response);
      return response;
  }

  protected void handleHttpClientExceptions(HttpClientErrorException e) {
    if (e.getStatusCode().is4xxClientError()) {
      throw new BridgeApiGraphqlException(ErrorType.OperationNotSupported, e.getMessage());
    } else {
      throw new BridgeApiGeneralException(e.getMessage());
    }
  }
}
*/