package com.pearson.sam.bridgeapi.samservices;

import static com.pearson.sam.bridgeapi.samservices.constants.SamEndPointConstants.ACTIVATE_ORGANIZATION;
import static com.pearson.sam.bridgeapi.samservices.constants.SamEndPointConstants.CREATE_ORGANIZATION;
import static com.pearson.sam.bridgeapi.samservices.constants.SamEndPointConstants.CREATE_ORGANIZATION_ASSOCIATIONS;
import static com.pearson.sam.bridgeapi.samservices.constants.SamEndPointConstants.DEACTIVATE_ORGANIZATION;
import static com.pearson.sam.bridgeapi.samservices.constants.SamEndPointConstants.GET_ALL_ORGANIZATIONS;
import static com.pearson.sam.bridgeapi.samservices.constants.SamEndPointConstants.GET_ORGANIZATION;
import static com.pearson.sam.bridgeapi.samservices.constants.SamEndPointConstants.GET_ORGANIZATION_ASSOCIATIONS;
import static com.pearson.sam.bridgeapi.samservices.constants.SamEndPointConstants.ORGANIZATION_SERVICES;
import static com.pearson.sam.bridgeapi.samservices.constants.SamEndPointConstants.UPDATE_ORGANIZATION;
import static com.pearson.sam.bridgeapi.samservices.constants.SamEndPointConstants.UPDATE_ORGANIZATION_ASSOCIATIONS;

import com.pearson.sam.bridgeapi.resthelper.RestClient;
import com.pearson.sam.bridgeapi.resthelper.RestRequestBuilder;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * OrganizationServices.
 * @author VKuma09
 *
 */
@Service
public class OrganizationServices {

  @Autowired
  RestClient restClient;

  private static final String ORG_IDENTIFIER = "orgIdentifier";  

  public Map<String, Object> activateOrganization(String orgIdentifier, String body) {
	  return restClient.patchNValidate(
		        new RestRequestBuilder(ORGANIZATION_SERVICES, ACTIVATE_ORGANIZATION)
		        .withBody(body).addUrlParams(ORG_IDENTIFIER, orgIdentifier).build());
  }

  public Map<String, Object> createOrganization(String orgIdentifier, String body) {
	  return restClient.postNValidate(
		        new RestRequestBuilder(ORGANIZATION_SERVICES, CREATE_ORGANIZATION)
		        .withBody(body).addUrlParams(ORG_IDENTIFIER, orgIdentifier).build());
  }

  public Map<String, Object> createOrganizationAssociations(String orgIdentifier, String body) {
	  return restClient.postNValidate(
		        new RestRequestBuilder(ORGANIZATION_SERVICES, CREATE_ORGANIZATION_ASSOCIATIONS)
		        .withBody(body).addUrlParams(ORG_IDENTIFIER, orgIdentifier).build());
  }

  public Map<String, Object> deactivateOrganization(String orgIdentifier, String body) {
	  return restClient.patchNValidate(
		        new RestRequestBuilder(ORGANIZATION_SERVICES, DEACTIVATE_ORGANIZATION)
		        .withBody(body).addUrlParams(ORG_IDENTIFIER, orgIdentifier).build());
  }

  public Map<String, Object> getAllOrganizations() {
	  return restClient.getNValidate(
		        new RestRequestBuilder(ORGANIZATION_SERVICES, GET_ALL_ORGANIZATIONS)
		        .build());
  }

  public Map<String, Object> getOrganization(String orgIdentifier) {
	  return restClient.getNValidate(
		        new RestRequestBuilder(ORGANIZATION_SERVICES, GET_ORGANIZATION)
		        .addUrlParams(ORG_IDENTIFIER, orgIdentifier).build());
  }

  public Map<String, Object> getOrganizationAssociations(String orgIdentifier) {
	  return restClient.getNValidate(
		        new RestRequestBuilder(ORGANIZATION_SERVICES, GET_ORGANIZATION_ASSOCIATIONS)
		        .addUrlParams(ORG_IDENTIFIER, orgIdentifier).build());
  }

  public Map<String, Object> updateOrganization(String orgIdentifier, String body) {
	  return restClient.putNValidate(
		        new RestRequestBuilder(ORGANIZATION_SERVICES, UPDATE_ORGANIZATION)
		        .withBody(body).addUrlParams(ORG_IDENTIFIER, orgIdentifier).build());
  }

  public Map<String, Object> updateOrganizationAssociations(String orgIdentifier, String body) {
	  return restClient.putNValidate(
		        new RestRequestBuilder(ORGANIZATION_SERVICES, UPDATE_ORGANIZATION_ASSOCIATIONS)
		        .withBody(body).addUrlParams(ORG_IDENTIFIER, orgIdentifier).build());
  }

}
