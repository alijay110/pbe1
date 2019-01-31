/**
 * 
 */
package com.pearson.sam.bridgeapi.samservices;

import com.pearson.sam.bridgeapi.resthelper.RestClient;
import com.pearson.sam.bridgeapi.resthelper.RestRequestBuilder;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * @author VKuma09
 *
 */
@Service
public class IdamServices {

  
  @Autowired
  RestClient restClient;
  
  @Value("${FR_URL}")
  private String url;

  @Value("${FR_LOGOUT_URI}")
  private String uri;

  @Value("${iamUri}")
  private String IAM_URI;
  
  /**
   * callLogout.
   * @param token
   * @return  
   */
  public Map<String, Object> callLogout(String token) {

    Map<String, String> headersMap = new HashMap<>();
    headersMap.put("PearsonExtSSOSession", token);
    headersMap.put("Content-Type", "application/json");
    Map<String, Object> resultMap = restClient
        .postNValidate(new RestRequestBuilder(url + uri).withAdditionalHeaders(headersMap).build());

    return null;
  }

  /**
   * validateToken.
   * @param token
   * @return  
   */
  public ResponseEntity<Map<String, Object>> validateToken(String token) {
    // TODO as part of refactoring
    return restClient.get(new RestRequestBuilder(IAM_URI+token).build());
  }

  
  
}
