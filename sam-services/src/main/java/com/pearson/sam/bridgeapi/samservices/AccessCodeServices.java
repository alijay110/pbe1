package com.pearson.sam.bridgeapi.samservices;

import static com.pearson.sam.bridgeapi.samservices.constants.SamEndPointConstants.ACCESS_CODE_SERVICE;
import static com.pearson.sam.bridgeapi.samservices.constants.SamEndPointConstants.ADD_ACCESS_CODES;
import static com.pearson.sam.bridgeapi.samservices.constants.SamEndPointConstants.CONFIGURE_ACCESS_CODE;
import static com.pearson.sam.bridgeapi.samservices.constants.SamEndPointConstants.CREATE_ACCESS_CODE;
import static com.pearson.sam.bridgeapi.samservices.constants.SamEndPointConstants.CREATE_AND_ACTIVATE_ALL_THE_PRODUCTS_IN_THE_SUBSCRIPTION;
import static com.pearson.sam.bridgeapi.samservices.constants.SamEndPointConstants.GET_ACCESS_CODE_DETAILS;
import static com.pearson.sam.bridgeapi.samservices.constants.SamEndPointConstants.GET_ALL_ACCESS_CODES;
import static com.pearson.sam.bridgeapi.samservices.constants.SamEndPointConstants.GET_ALL_SUBSCRIPTION_DETAILS_OF_A_USER;
import static com.pearson.sam.bridgeapi.samservices.constants.SamEndPointConstants.GET_SUBSCRIPTION_DETAILS_OF_A_USER_BASED_ON_THE_ACCESS_CODE;
import static com.pearson.sam.bridgeapi.samservices.constants.SamEndPointConstants.GENERATE_ACCESS_CODES_BATCH;
import com.pearson.sam.bridgeapi.resthelper.RestClient;
import com.pearson.sam.bridgeapi.resthelper.RestRequestBuilder;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class AccessCodeServices {

  @Autowired
  RestClient restClient;

  private static final String ACCESS_CODE = "accessCode";
  private static final String USER_NAME = "userName";

  
  // query param
  public Map<String, Object> getAllAccessCode() {
    return restClient
        .getNValidate(new RestRequestBuilder(ACCESS_CODE_SERVICE, GET_ALL_ACCESS_CODES).build());
  }

  public Map<String, Object> createAccessCode(String body) {
    return restClient.postNValidate(
        new RestRequestBuilder(ACCESS_CODE_SERVICE, CREATE_ACCESS_CODE).withBody(body).build());
  }

  public Map<String, Object> createAccessCodeInBatch(String requestJson) {
	  return restClient.postNValidate(
		        new RestRequestBuilder(ACCESS_CODE_SERVICE, GENERATE_ACCESS_CODES_BATCH).withBody(requestJson).build());
  }

  /**
   * getAccessCodeDetails.
   * 
   * @return
   */
  public Map<String, Object> getAccessCodeDetails(String accessCode) {
    return restClient
        .getNValidate(new RestRequestBuilder(ACCESS_CODE_SERVICE, GET_ACCESS_CODE_DETAILS)
            .addUrlParams(ACCESS_CODE, accessCode).build());
  }

  /**
   * configueAccessCode.
   * 
   * @return
   */
  public Map<String, Object> configueAccessCode(String accessCode, String body) {
    return restClient
        .patchNValidate(new RestRequestBuilder(ACCESS_CODE_SERVICE, CONFIGURE_ACCESS_CODE)
            .addUrlParams(ACCESS_CODE, accessCode).withBody(body).build());
  }

  // query param
  /**
   * getSubscriptionDetailsofAccessCode.
   * 
   * @return
   */
  public Map<String, Object> getSubscriptionDetailsofAccessCode(String accessCode) {
    return restClient.getNValidate(new RestRequestBuilder(ACCESS_CODE_SERVICE,
        GET_SUBSCRIPTION_DETAILS_OF_A_USER_BASED_ON_THE_ACCESS_CODE)
            .addUrlParams(ACCESS_CODE, accessCode).build());
  }

  // query param
  /**
   * getSubscriptionDetailsofAccessCode.
   * 
   * @return
   */
  public Map<String, Object> getAllSubscriptionDetailsofUser(String userName) {
    return restClient.getNValidate(
        new RestRequestBuilder(ACCESS_CODE_SERVICE, GET_ALL_SUBSCRIPTION_DETAILS_OF_A_USER)
            .addUrlParams(USER_NAME, userName).build());
  }

  /**
   * createAndActivateProductAndSubscription.
   * 
   * @return
   */
  public Map<String, Object> createAndActivateProductAndSubscription(String accessCode,
      String body) {
    return restClient.postNValidate(new RestRequestBuilder(ACCESS_CODE_SERVICE,
        CREATE_AND_ACTIVATE_ALL_THE_PRODUCTS_IN_THE_SUBSCRIPTION)
            .addUrlParams(ACCESS_CODE, accessCode).withBody(body).build());
  }

  /**
   * configureSubscriptionDetails.
   * 
   * @return
   */
  public Map<String, Object> configureSubscriptionDetails(String accessCode, String productCode,
      String body) {
    return restClient.putNValidate(new RestRequestBuilder(ACCESS_CODE_SERVICE,
        CREATE_AND_ACTIVATE_ALL_THE_PRODUCTS_IN_THE_SUBSCRIPTION)
            .addUrlParams(ACCESS_CODE, accessCode).addUrlParams("productCode", productCode)
            .withBody(body).build());
  }

  public Map<String, Object> addAccessCodes(String body) {
    return restClient.postNValidate(
        new RestRequestBuilder(ACCESS_CODE_SERVICE, ADD_ACCESS_CODES).withBody(body).build());
  }

}
