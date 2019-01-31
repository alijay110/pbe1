package com.pearson.sam.bridgeapi.samservices;

import static com.pearson.sam.bridgeapi.samservices.constants.SamEndPointConstants.ACTIVATE_PRODUCT;
import static com.pearson.sam.bridgeapi.samservices.constants.SamEndPointConstants.CREATE_PRODUCT;
import static com.pearson.sam.bridgeapi.samservices.constants.SamEndPointConstants.DEACTIVATE_PRODUCT;
import static com.pearson.sam.bridgeapi.samservices.constants.SamEndPointConstants.GET_PRODUCT;
import static com.pearson.sam.bridgeapi.samservices.constants.SamEndPointConstants.GET_PRODUCTS;
import static com.pearson.sam.bridgeapi.samservices.constants.SamEndPointConstants.PRODUCT_SERVICES;
import static com.pearson.sam.bridgeapi.samservices.constants.SamEndPointConstants.UPDATE_PRODUCT;
import static com.pearson.sam.bridgeapi.samservices.constants.SamEndPointConstants.UPDATE_PRODUCT_DETAILS;
import static com.pearson.sam.bridgeapi.samservices.constants.SamEndPointConstants.GET_LEARNING_MATERIAL;

import com.pearson.sam.bridgeapi.resthelper.RestClient;
import com.pearson.sam.bridgeapi.resthelper.RestRequestBuilder;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ProductServices.
 * @author VKuma09
 *
 */
@Service
public class ProductServices {
  
  @Autowired
  RestClient restClient;

  private static final String PRODUCT_IDENTIFIER = "productIdentifier";  
  private static final String ORG_IDENTIFIER = "orgIdentifier";  
  private static final String COURSE_ID = "courseId";  
  private static final String USER_ID = "userId"; 
  
	
  public Map<String, Object> activateProduct(String productIdentifier, String body) {
	  return restClient.putNValidate(new RestRequestBuilder(PRODUCT_SERVICES, ACTIVATE_PRODUCT)
		        .withBody(body).addUrlParams(PRODUCT_IDENTIFIER, productIdentifier).build());
  }

  public Map<String, Object> createProduct(String body) {
	  return restClient.postNValidate(
		        new RestRequestBuilder(PRODUCT_SERVICES, CREATE_PRODUCT).withBody(body).build());
  }

  public Map<String, Object> deactivateProduct(String productIdentifier, String body) {
	  return restClient.putNValidate(new RestRequestBuilder(PRODUCT_SERVICES, DEACTIVATE_PRODUCT)
		        .withBody(body).addUrlParams(PRODUCT_IDENTIFIER, productIdentifier).build());
  }

  public Map<String, Object> getProduct(String productIdentifier) {
	  return restClient.getNValidate(new RestRequestBuilder(PRODUCT_SERVICES, GET_PRODUCT)
		        .addUrlParams(PRODUCT_IDENTIFIER, productIdentifier).build());
  }

  public Map<String, Object> getProducts() {
	  return restClient.getNValidate(new RestRequestBuilder(PRODUCT_SERVICES, GET_PRODUCTS)
		        .build());
  }

  public Map<String, Object> updateProduct(String productIdentifier, String body) {
    return restClient.putNValidate(new RestRequestBuilder(PRODUCT_SERVICES, UPDATE_PRODUCT_DETAILS)
			  .withBody(body).addUrlParams(PRODUCT_IDENTIFIER, productIdentifier).build());
  }
  
  public Map<String, Object> getLearningMaterial(String orgIdentifier, String courseId, String userId) {
    return restClient.getNValidate(new RestRequestBuilder(PRODUCT_SERVICES, GET_LEARNING_MATERIAL)
			  .addUrlParams(ORG_IDENTIFIER, orgIdentifier).addUrlParams(COURSE_ID, courseId)
			  .addUrlParams(USER_ID,userId).build());
  }
}
