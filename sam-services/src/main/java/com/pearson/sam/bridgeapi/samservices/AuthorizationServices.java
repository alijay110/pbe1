package com.pearson.sam.bridgeapi.samservices;

import static com.pearson.sam.bridgeapi.samservices.constants.SamEndPointConstants.AUTHORIZATION_SERVICES;
import static com.pearson.sam.bridgeapi.samservices.constants.SamEndPointConstants.CREATE_ROLE;
import static com.pearson.sam.bridgeapi.samservices.constants.SamEndPointConstants.CREATE_USERROLE;
import static com.pearson.sam.bridgeapi.samservices.constants.SamEndPointConstants.DELETE_ROLE;
import static com.pearson.sam.bridgeapi.samservices.constants.SamEndPointConstants.GET_ALL_ROLES;
import static com.pearson.sam.bridgeapi.samservices.constants.SamEndPointConstants.GET_ROLE;
import static com.pearson.sam.bridgeapi.samservices.constants.SamEndPointConstants.GET_USERROLE;
import static com.pearson.sam.bridgeapi.samservices.constants.SamEndPointConstants.UPDATE_ROLE;
import static com.pearson.sam.bridgeapi.samservices.constants.SamEndPointConstants.UPDATE_USERROLE;

import com.pearson.sam.bridgeapi.resthelper.RestClient;
import com.pearson.sam.bridgeapi.resthelper.RestRequestBuilder;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * AuthorizationServices.
 * 
 * @author VKuma09
 *
 */
@Service
public class AuthorizationServices {

  @Autowired
  RestClient restClient;

  private static final String ROLE_NAME = "roleName";
  private static final String USER_NAME = "userName";

  
  /**
   * createRole.
   */
  public Map<String, Object> createRole(String body) {

    return restClient.postNValidate(
        new RestRequestBuilder(AUTHORIZATION_SERVICES, CREATE_ROLE).withBody(body).build());
  }

  public Map<String, Object> createUserrole(String body) {
    return restClient.postNValidate(
        new RestRequestBuilder(AUTHORIZATION_SERVICES, CREATE_USERROLE).withBody(body).build());
  }

  /**
   * deleteRole.
   * 
   * @return
   */
  public Map<String, Object> deleteRole(String roleName) {
    return restClient.deleteNValidate(new RestRequestBuilder(AUTHORIZATION_SERVICES, DELETE_ROLE)
        .addUrlParams(ROLE_NAME, roleName).build());
  }

  public Map<String, Object> getAllRoles() {
    return restClient
        .getNValidate(new RestRequestBuilder(AUTHORIZATION_SERVICES, GET_ALL_ROLES).build());
  }

  /**
   * getUserrole.
   * 
   * @return
   */
  public Map<String, Object> getUserrole(String userName) {
    return restClient.getNValidate(new RestRequestBuilder(AUTHORIZATION_SERVICES, GET_USERROLE)
        .addUrlParams(USER_NAME, userName).build());
  }

  /**
   * updateUserrole.
   * 
   * @return
   */
  public Map<String, Object> updateUserrole(String userName, String body) {
    return restClient.putNValidate(new RestRequestBuilder(AUTHORIZATION_SERVICES, UPDATE_USERROLE)
        .withBody(body).addUrlParams(USER_NAME, userName).build()); 
  }

  /**
   * getRole.
   * 
   * @return
   */
  public Map<String, Object> getRole(String roleName) {
    return restClient.getNValidate(new RestRequestBuilder(AUTHORIZATION_SERVICES, GET_ROLE)
        .addUrlParams(ROLE_NAME, roleName).build());

  }

  /**
   * updateRole.
   * 
   * @return
   */
  public Map<String, Object> updateRole(String roleName, String body) {
    return restClient.putNValidate(new RestRequestBuilder(AUTHORIZATION_SERVICES, UPDATE_ROLE)
        .withBody(body).addUrlParams(ROLE_NAME, roleName).build());
  }

}
