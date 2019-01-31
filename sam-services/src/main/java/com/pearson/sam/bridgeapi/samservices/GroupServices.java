package com.pearson.sam.bridgeapi.samservices;

import static com.pearson.sam.bridgeapi.samservices.constants.SamEndPointConstants.ACTIVATE_GROUP;
import static com.pearson.sam.bridgeapi.samservices.constants.SamEndPointConstants.ADD_MEMBERS;
import static com.pearson.sam.bridgeapi.samservices.constants.SamEndPointConstants.CREATE_GROUP;
import static com.pearson.sam.bridgeapi.samservices.constants.SamEndPointConstants.DEACTIVATE_GROUP;
import static com.pearson.sam.bridgeapi.samservices.constants.SamEndPointConstants.GET_GROUP_DETAILS;
import static com.pearson.sam.bridgeapi.samservices.constants.SamEndPointConstants.GET_GROUP_MEMBER_DETAILS;
import static com.pearson.sam.bridgeapi.samservices.constants.SamEndPointConstants.GET_GROUP_MEMBER_INVITATIONS;
import static com.pearson.sam.bridgeapi.samservices.constants.SamEndPointConstants.GET_USER_GROUP_LIST;
import static com.pearson.sam.bridgeapi.samservices.constants.SamEndPointConstants.GROUP_SERVICES;
import static com.pearson.sam.bridgeapi.samservices.constants.SamEndPointConstants.REMOVE_MEMBERS;
import static com.pearson.sam.bridgeapi.samservices.constants.SamEndPointConstants.UPDATE_GROUPE_NAME_OR_TYPE;
import static com.pearson.sam.bridgeapi.samservices.constants.SamEndPointConstants.UPDATE_GROUP_MEMBER_INVITATIONS;

import com.pearson.sam.bridgeapi.resthelper.RestClient;
import com.pearson.sam.bridgeapi.resthelper.RestRequestBuilder;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Group Services.
 * 
 * @author VKuma09
 *
 */
@Service
public class GroupServices {

  @Autowired
  RestClient restClient;

  private static final String GROUP_IDENTIFIER = "groupIdentifier";
  private static final String USER_NAME = "userName";

  public Map<String, Object> activateGroup(String body, String groupIdentifier) {
    return restClient.putNValidate(new RestRequestBuilder(GROUP_SERVICES, ACTIVATE_GROUP)
        .withBody(body).addUrlParams(GROUP_IDENTIFIER, groupIdentifier).build());
  }

  public Map<String, Object> addMembers(String body, String groupIdentifier) {
    return restClient.putNValidate(new RestRequestBuilder(GROUP_SERVICES, ADD_MEMBERS)
        .withBody(body).addUrlParams(GROUP_IDENTIFIER, groupIdentifier).build());
  }

  public Map<String, Object> createGroup(String body) {
    return restClient
        .postNValidate(new RestRequestBuilder(GROUP_SERVICES, CREATE_GROUP).withBody(body).build());
  }

  public Map<String, Object> createGroupMemberInvitations(String body, String groupIdentifier) {
    return restClient.postNValidate(new RestRequestBuilder(GROUP_SERVICES, CREATE_GROUP)
        .withBody(body).addUrlParams(GROUP_IDENTIFIER, groupIdentifier).build());
  }

  public Map<String, Object> deactivateGroup(String body, String groupIdentifier) {
    return restClient.putNValidate(new RestRequestBuilder(GROUP_SERVICES, DEACTIVATE_GROUP)
        .withBody(body).addUrlParams(GROUP_IDENTIFIER, groupIdentifier).build());
  }

  /**
   * getGroupDetails.
   * 
   * @param groupIdentifier
   *          mandatory
   * @param productModelIdentifier
   *          optional
   * @param orgIdentifier
   *          optional
   * @param activationTimestamp
   *          optional
   * @param deactivationTimestamp
   *          optional
   * @param userName
   *          optional
   * @return
   */
  public Map<String, Object> getGroupDetails(String groupIdentifier, String productModelIdentifier,
      String orgIdentifier, String activationTimestamp, String deactivationTimestamp,
      String userName) {

    RestRequestBuilder builder = new RestRequestBuilder(GROUP_SERVICES, GET_GROUP_DETAILS)
        .addUrlParams(GROUP_IDENTIFIER, groupIdentifier);
    if (null != productModelIdentifier)
      builder.addQueryParams("productModelIdentifier", productModelIdentifier);
    else if (null != orgIdentifier)
      builder.addQueryParams("orgIdentifier", orgIdentifier);
    else if (null != activationTimestamp)
      builder.addQueryParams("activationTimestamp", activationTimestamp);
    else if (null != deactivationTimestamp)
      builder.addQueryParams("deactivationTimestamp", deactivationTimestamp);
    else if (null != userName)
      builder.addQueryParams(USER_NAME, userName);
    return restClient.getNValidate(builder.build());
  }

  public Map<String, Object> getGroupMemberDetails(String groupIdentifier) {
    return restClient.getNValidate(new RestRequestBuilder(GROUP_SERVICES, GET_GROUP_MEMBER_DETAILS)
        .addUrlParams(GROUP_IDENTIFIER, groupIdentifier).build());
  }

  public Map<String, Object> getGroupMemberInvitationDetails(String groupIdentifier,
      String userName) {
    return restClient.getNValidate(new RestRequestBuilder(GROUP_SERVICES, GET_GROUP_MEMBER_DETAILS)
        .addUrlParams(USER_NAME, userName).addUrlParams(GROUP_IDENTIFIER, groupIdentifier).build());
  }

  /**
   * getGroupMemberInvitations.
   * 
   * @return
   */
  public Map<String, Object> getGroupMemberInvitations(String groupIdentifier) {
    return restClient
        .getNValidate(new RestRequestBuilder(GROUP_SERVICES, GET_GROUP_MEMBER_INVITATIONS)
            .addUrlParams(GROUP_IDENTIFIER, groupIdentifier).build());
  }

  public Map<String, Object> getUserGroupList(String userName) {
    return restClient.getNValidate(new RestRequestBuilder(GROUP_SERVICES, GET_USER_GROUP_LIST)
        .addUrlParams(USER_NAME, userName).build());
  }

  public Map<String, Object> removeMembers(String body, String groupIdentifier) {
    return restClient.deleteNValidate(new RestRequestBuilder(GROUP_SERVICES, REMOVE_MEMBERS)
        .withBody(body).addUrlParams(GROUP_IDENTIFIER, groupIdentifier).build());
  }

  /**
   * updateGroupMemberInvitations.
   * 
   * @return
   */
  public Map<String, Object> updateGroupMemberInvitations(String body, String groupIdentifier) {
    return restClient
        .putNValidate(new RestRequestBuilder(GROUP_SERVICES, UPDATE_GROUP_MEMBER_INVITATIONS)
            .withBody(body).addUrlParams(GROUP_IDENTIFIER, groupIdentifier).build());
  }

  /**
   * updateGroupeNameOrType.
   * 
   * @return
   */
  public Map<String, Object> updateGroupeNameOrType(String body, String groupIdentifier) {
    return restClient
        .putNValidate(new RestRequestBuilder(GROUP_SERVICES, UPDATE_GROUPE_NAME_OR_TYPE)
            .withBody(body).addUrlParams(GROUP_IDENTIFIER, groupIdentifier).build());
  }

}
