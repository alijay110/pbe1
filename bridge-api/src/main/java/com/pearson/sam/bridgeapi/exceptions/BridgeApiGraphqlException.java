package com.pearson.sam.bridgeapi.exceptions;

import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

import java.util.List;

@SuppressWarnings("serial")
public class BridgeApiGraphqlException extends RuntimeException implements GraphQLError {

  private final ErrorType errorType;

  public BridgeApiGraphqlException(String message) {
    super(message, null, true, false);
    this.errorType = ErrorType.ValidationError;
  }


  public BridgeApiGraphqlException(ErrorType error, String message) {
    super(message, null, true, false);
    this.errorType = error;
  }

  @Override
  public List<SourceLocation> getLocations() {
    List<SourceLocation> returnList;
    returnList = null;
    return returnList;
  }

  @Override
  public ErrorType getErrorType() {
    return this.errorType;
  }
}