package com.pearson.sam.bridgeapi.serviceimpl;

import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.QUERIES_INFO;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.ROLES;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.SAM_AUTH_URL;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.SLASH;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.pearson.sam.bridgeapi.samservices.AuthorizationServices;
import com.pearson.sam.bridgeapi.util.PropertyHolder;
//import com.pearson.sam.bridgeapi.exceptions.RestRequestBuilder;
//import com.pearson.sam.bridgeapi.samservices.RestClient;
import com.pearson.sam.bridgeapi.util.Utils;

@Component
public class RoleService {

  @Autowired
  AuthorizationServices authorizationServices;
  
  @Value("${SPEC_FILE}")
  String specJsonFile;

  @Autowired
  @Qualifier(QUERIES_INFO)
  Map<String, Set<String>> quriesMap;

  /**
   * getRoles.
   * 
   * @return
   */
  public Set<String> getRoles(String userName) {
    Map<String, String> params = new HashMap<>();
    params.put("userName", userName);
    Map<String, Object> rolesMap = authorizationServices.getUserrole(userName);
    Set<String> roles = null;
    if (rolesMap != null) {
      roles = Utils.convert(rolesMap.get("roles"), new TypeReference<Set<String>>() {
      });
    }
    return roles;
  }

  /**
   * getPermissions.
   * 
   * @return
   */
  public Set<String> getPermissions(String roleName) {
    Map<String, Object> rolesMap = authorizationServices.getRole(roleName);
    return Utils.convert(Utils.transformJson(rolesMap, "Permissions", specJsonFile),
        new TypeReference<Set<String>>() {
        });
  }

  /**
   * getPermissions.
   * 
   * @return
   */
  public Set<String> getPermissions(Set<String> roles) {

    Map<String, Object> rolesMap = new HashMap<>();

    String baseUrl = PropertyHolder.getStringProperty(SAM_AUTH_URL)
        + PropertyHolder.getStringProperty(ROLES) + SLASH;
    for (String roleName : roles) {
      String url = baseUrl + roleName;
//      Map<String,Object> nValidate = RestClient.getNValidate(new RestRequestBuilder(url).build());
      Map<String, Object> roleDetail = authorizationServices.getRole(roleName);
      rolesMap.putAll(roleDetail);
    }

    return Utils.convert(Utils.transformJson(rolesMap, "Permissions", specJsonFile),
        new TypeReference<Set<String>>() {
        });
  }

}