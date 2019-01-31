package com.pearson.sam.bridgeapi.samservices.constants;

/**
 * SAM End points.
 * 
 * @author VKuma09
 *
 */
public class SamEndPointConstants {

  private SamEndPointConstants() {
    super();
  }

  public static final String SAM_BASE_URL = "SAM_BASE_URL";

  public static final String IDENTITY_SERVICES = "IDENTITY";
  public static final String GROUP_SERVICES = "GROUP";
  public static final String AUTHORIZATION_SERVICES = "AUTHORIZATION";
  public static final String PRODUCT_SERVICES = "PRODUCT";
  public static final String ORGANIZATION_SERVICES = "ORGANIZATION";
  public static final String ACCESS_CODE_SERVICE = "ACCESS_CODE";
  public static final String AUTHENTICATION_SERVICES = "AUTHENTICATION";

  public static final String ACTIVATE_GROUP = "ACTIVATE_GROUP";
  public static final String ADD_MEMBERS = "ADD_MEMBERS";
  public static final String CREATE_GROUP = "CREATE_GROUP";
  public static final String CREATE_GROUP_MEMBER_INVITATIONS = "CREATE_GROUP_MEMBER_INVITATIONS";
  public static final String DEACTIVATE_GROUP = "DEACTIVATE_GROUP";
  public static final String GET_GROUP_DETAILS = "GET_GROUP_DETAILS";
  public static final String GET_GROUP_MEMBER_DETAILS = "GET_GROUP_MEMBER_DETAILS";
  public static final String GET_GROUP_MEMBER_INVITATION_DETAILS = "GET_GROUP_MEMBER_INVITATION_DETAILS";
  public static final String GET_GROUP_MEMBER_INVITATIONS = "GET_GROUP_MEMBER_INVITATIONS";
  public static final String GET_USER_GROUP_LIST = "GET_USER_GROUP_LIST";
  public static final String REMOVE_MEMBERS = "REMOVE_MEMBERS";
  public static final String UPDATE_GROUP_MEMBER_INVITATIONS = "UPDATE_GROUP_MEMBER_INVITATIONS";
  public static final String UPDATE_GROUPE_NAME_OR_TYPE = "UPDATE_GROUPE_NAME_OR_TYPE";

  public static final String CREATE_ROLE = "CREATE_ROLE";
  public static final String CREATE_USERROLE = "CREATE_USERROLE";
  public static final String DELETE_ROLE = "DELETE_ROLE";
  public static final String GET_ALL_ROLES = "GET_ALL_ROLES";
  public static final String GET_USERROLE = "GET_USERROLE";
  public static final String UPDATE_USERROLE = "UPDATE_USERROLE";
  public static final String GET_ROLE = "GET_ROLE";
  public static final String UPDATE_ROLE = "UPDATE_ROLE";

  public static final String CHANGE_PASS_WORD = "CHANGE_PASSWORD";
  public static final String CHECK_USER_IDENTITY_AVAILABILITY = "CHECK_USER_IDENTITY_AVAILABILITY";
  public static final String CREATE_USER = "CREATE_USER";
  public static final String GET_USER = "GET_USER";
  public static final String UPDATE_USER = "UPDATE_USER";

  public static final String ACTIVATE_ORGANIZATION = "ACTIVATE_ORGANIZATION";
  public static final String CREATE_ORGANIZATION = "CREATE_ORGANIZATION";
  public static final String CREATE_ORGANIZATION_ASSOCIATIONS = "CREATE_ORGANIZATION_ASSOCIATIONS";
  public static final String DEACTIVATE_ORGANIZATION = "DEACTIVATE_ORGANIZATION";
  public static final String GET_ALL_ORGANIZATIONS = "GET_ALL_ORGANIZATIONS";
  public static final String GET_ORGANIZATION = "GET_ORGANIZATION";
  public static final String GET_ORGANIZATION_ASSOCIATIONS = "GET_ORGANIZATION_ASSOCIATIONS";
  public static final String UPDATE_ORGANIZATION = "UPDATE_ORGANIZATION";
  public static final String UPDATE_ORGANIZATION_ASSOCIATIONS = "UPDATE_ORGANIZATION_ASSOCIATIONS";

  public static final String ACTIVATE_PRODUCT = "ACTIVATE_PRODUCT";
  public static final String CREATE_PRODUCT = "CREATE_PRODUCT";
  public static final String DEACTIVATE_PRODUCT = "DEACTIVATE_PRODUCT";
  public static final String GET_PRODUCT = "GET_PRODUCT";
  public static final String GET_PRODUCTS = "GET_PRODUCTS";
  public static final String UPDATE_PRODUCT = "UPDATE_PRODUCT";
  public static final String UPDATE_PRODUCT_DETAILS = "UPDATE_PRODUCT_DETAILS";
  public static final String GET_LEARNING_MATERIAL = "GET_LEARNING_MATERIAL";

  public static final String GET_ALL_ACCESS_CODES = "GET_ALL_ACCESS_CODES";
  public static final String CREATE_ACCESS_CODE = "CREATE_ACCESS_CODE";
  public static final String GET_ACCESS_CODE_DETAILS = "GET_ACCESS_CODE_DETAILS";
  public static final String CONFIGURE_ACCESS_CODE = "CONFIGURE_ACCESS_CODE";
  public static final String GET_SUBSCRIPTION_DETAILS_OF_A_USER_BASED_ON_THE_ACCESS_CODE = "GET_SUBSCRIPTION_DETAILS_OF_A_USER_BASED_ON_THE_ACCESS_CODE";
  public static final String CREATE_AND_ACTIVATE_ALL_THE_PRODUCTS_IN_THE_SUBSCRIPTION = "CREATE_AND_ACTIVATE_ALL_THE_PRODUCTS_IN_THE_SUBSCRIPTION";
  public static final String CONFIGURE_SUBSCRIPTION_DETAILS = "CONFIGURE_SUBSCRIPTION_DETAILS";
  public static final String ADD_ACCESS_CODES = "ADD_ACCESS_CODES";
  public static final String GET_ALL_SUBSCRIPTION_DETAILS_OF_A_USER = "GET_ALL_SUBSCRIPTION_DETAILS_OF_A_USER";
  public static final String GENERATE_ACCESS_CODES_BATCH = "GENERATE_ACCESS_CODES_BATCH";
  
  public static final String AUTHENTICATE = "AUTHENTICATE";
  public static final String LOGOUT = "LOGOUT";
 
  public static final String SAM_API_KEY = "sam.api.key";
}
