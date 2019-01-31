/**
 * 
 */
package com.pearson.sam.bridgeapi.samclient;

import com.pearson.sam.bridgeapi.samservices.IdamServices;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @author VKuma09
 *
 */
@Component
public class IdamClient {

  @Autowired
  IdamServices idamServices;
  
  public Map<String, Object> callLogout(String token) {
    return idamServices.callLogout(token);
  }

  /**
   * validates token.
   * 
   * @param url
   *          url
   * @return
   */
  public ResponseEntity<Map<String,Object>> validateToken(String token) {
    return idamServices.validateToken(token);
  }

  
}
