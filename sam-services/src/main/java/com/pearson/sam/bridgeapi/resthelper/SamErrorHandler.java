package com.pearson.sam.bridgeapi.resthelper;

import java.io.IOException;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

public class SamErrorHandler implements ResponseErrorHandler {
  @Override
  public void handleError(ClientHttpResponse response) throws IOException{
    response.getStatusCode();
  }

  @Override
  public boolean hasError(ClientHttpResponse response){
    return true;
  }
}