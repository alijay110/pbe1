package com.pearson.sam.bridgeapi.exceptions;

@SuppressWarnings("serial")
public class BridgeApiGeneralException extends RuntimeException {

  public BridgeApiGeneralException(String message) {
    super(message, null, true, false);
  }

}