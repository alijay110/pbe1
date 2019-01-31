package com.pearson.sam.bridgeapi.resthelper;

import org.springframework.http.HttpEntity;

/**
 * rest client request.
 * @author VKuma09
 *
 */
public class RestClientRequest {

  private HttpEntity<String> requestEntity;
  private String constructedUrl;

  public HttpEntity<String> getRequestEntity() {
    return requestEntity;
  }

  public void setRequestEntity(HttpEntity<String> requestEntity) {
    this.requestEntity = requestEntity;
  }

  public String getConstructedUrl() {
    return constructedUrl;
  }

  public void setConstructedUrl(String constructedUrl) {
    this.constructedUrl = constructedUrl;
  }

  @Override
  public String toString() {
    return "RestClientRequest [constructedUrl=" + constructedUrl + ", requestEntity="
        + requestEntity + " ]";
  }

}
