package com.pearson.sam.bridgeapi.resthelper;

import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestClient {

  public RestClient() {
    this.restTemplate = new RestTemplateBuilder().errorHandler(new SamErrorHandler()).build();
  }

  private RestTemplate restTemplate;
  private final Logger logger = LoggerFactory.getLogger(RestClient.class);

  public <T> ResponseEntity<T> get(RestClientRequest restReq) {
    return execute(restReq, HttpMethod.GET);
  }

  public <T> ResponseEntity<T> put(RestClientRequest restReq) {
    return execute(restReq, HttpMethod.PUT);
  }

  public <T> ResponseEntity<T> post(RestClientRequest restReq) {
    return execute(restReq, HttpMethod.POST);
  }

  public <T> ResponseEntity<T> delete(RestClientRequest restReq) {
    return execute(restReq, HttpMethod.DELETE);
  }

  public <T> ResponseEntity<T> patch(RestClientRequest restReq) {
    return execute(restReq, HttpMethod.PATCH);
  }

  public <T> T getResponse(RestClientRequest restReq) {
    return validateAndGetBody(execute(restReq, HttpMethod.GET));
  }

  public <T> T getNValidate(RestClientRequest restReq) {
    return validateAndGetBody(execute(restReq, HttpMethod.GET));
  }

  public <T> T putNValidate(RestClientRequest restReq) {
    return validateAndGetBody(execute(restReq, HttpMethod.PUT));
  }

  public <T> T postNValidate(RestClientRequest restReq) {
    return validateAndGetBody(execute(restReq, HttpMethod.POST));
  }

  public <T> T deleteNValidate(RestClientRequest restReq) {
    return validateAndGetBody(execute(restReq, HttpMethod.DELETE));
  }

  public <T> T patchNValidate(RestClientRequest restReq) {
    return validateAndGetBody(execute(restReq, HttpMethod.PATCH));
  }

  private <T> ResponseEntity<T> execute(RestClientRequest restReq, HttpMethod method) {
    ResponseEntity<T> response = restTemplate.exchange(restReq.getConstructedUrl(), method,
        restReq.getRequestEntity(), new ParameterizedTypeReference<T>() {
        });
    logResponse(response);
    return response;
  }

  private <T> void logResponse(ResponseEntity<T> response) {
    logger.info("Response {}", response.getStatusCode().is2xxSuccessful());
    logger.info("responseEntity: {}", response);
  }

  /**
   * validateAndGetBody.
   * 
   * @return
   */
  private <T> T validateAndGetBody(ResponseEntity<T> response) {
    if (response.getStatusCode().is2xxSuccessful()) {
    	Map<String, Object> responseMap = (Map<String, Object>) response.getBody();
    	if(responseMap.containsKey("failure")) {
    		StringTokenizer tokens = new StringTokenizer(responseMap.get("failure").toString(), ",[{}]");    
    		while (tokens.hasMoreTokens())
    		{
	    		String errMsg = tokens.nextToken();
				if(errMsg.contains("message"))
					throw new SAMServiceException(StringUtils.EMPTY + errMsg.replace("message=", ""));    	
    		}
    	} 
      return response.getBody();
    }

    throw new SAMServiceException(StringUtils.EMPTY + response.getBody());
  }

}
