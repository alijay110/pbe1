package com.pearson.sam.bridgeapi.samservices;

import static com.pearson.sam.bridgeapi.samservices.constants.SamEndPointConstants.CHANGE_PASS_WORD;
import static com.pearson.sam.bridgeapi.samservices.constants.SamEndPointConstants.CHECK_USER_IDENTITY_AVAILABILITY;
import static com.pearson.sam.bridgeapi.samservices.constants.SamEndPointConstants.CREATE_USER;
import static com.pearson.sam.bridgeapi.samservices.constants.SamEndPointConstants.GET_USER;
import static com.pearson.sam.bridgeapi.samservices.constants.SamEndPointConstants.IDENTITY_SERVICES;
import static com.pearson.sam.bridgeapi.samservices.constants.SamEndPointConstants.UPDATE_USER;

import com.pearson.sam.bridgeapi.resthelper.RestClient;
import com.pearson.sam.bridgeapi.resthelper.RestRequestBuilder;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * IdentityServices.
 * @author VKuma09
 *
 */
@Service
public class IdentityServices {
  
	private static final String USER_NAME = "userName";

  @Autowired
  RestClient restClient;

  public Map<String, Object> changePassword(String userName, String body) {
	  return restClient.patchNValidate(
		        new RestRequestBuilder(IDENTITY_SERVICES, CHANGE_PASS_WORD)
		        .withBody(body).addUrlParams(USER_NAME, userName).build());
  }

  public Boolean checkUserIdentityAvailability(String userName) {
	  return restClient.getNValidate(new RestRequestBuilder(IDENTITY_SERVICES, CHECK_USER_IDENTITY_AVAILABILITY)
		        .addUrlParams(USER_NAME, userName).build());
  }

  public Map<String, Object> createUser(String body) {
	  return restClient.postNValidate(
		        new RestRequestBuilder(IDENTITY_SERVICES, CREATE_USER).withBody(body).build());
  }

  public Map<String, Object> getUser(String userName) {
	  return restClient.getNValidate(new RestRequestBuilder(IDENTITY_SERVICES, GET_USER)
			  .addUrlParams(USER_NAME, userName).build());
  }

  public Map<String, Object> updateUser(String userName, String body) {
	  return restClient.patchNValidate(new RestRequestBuilder(IDENTITY_SERVICES, UPDATE_USER)
		        .withBody(body).addUrlParams(USER_NAME, userName).build());
  }
}
