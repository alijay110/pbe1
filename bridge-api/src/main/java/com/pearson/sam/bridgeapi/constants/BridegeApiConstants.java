package com.pearson.sam.bridgeapi.constants;

public final class BridegeApiConstants {
  private BridegeApiConstants() {
    // restrict instantiation
  }

  public static final String SAM_AUTH_URL = "SAM_AUTH_URL";
  public static final String SAM_CONSENT_URL = "SAM_CONSENT_URL";
  public static final String SAM_SCHOOL_URL = "SAM_SCHOOL_URL";
  public static final String GRAPHQL_SCHEMA = "GRAPHQL_SCHEMA";
  public static final String MUTATION = "Mutation";
  public static final String QUERY = "Query";
  public static final String CREATE_USER = "addUserDetailsViaSignup";

  public static final String CREATE_BULK_USER = "userUpload";
  public static final String ADD_USER = "addUser";
  public static final String UPDATE_USER = "updateUser";
  public static final String UPDATE_FULLNAME_BY_USER_ID = "updateViewerFullNameByUserId";
  public static final String CHANGE_PASS_WORD = "changepassword";
  public static final String FORGOT_PASS_WORD = "forgotPassword";
  public static final String USER_REPO = "userRepo";
  public static final String SCHOOL_REPO = "schoolRepo";
  public static final String PRODUCT_REPO = "productRepo";
  public static final String COURSE_REPO = "courseRepo";
  public static final String PANEL_REPO = "panelRepo";
  public static final String AUTH_REPO = "authRepo";
  public static final String USER = "user";
  public static final String CREATE_USER_ROLE = "CREATE_USER_ROLE";
  public static final String USER_ROLES = "USER_ROLES";
  public static final String SLASH = "/";
  public static final String ROLES = "ROLES";
  public static final String PATH_VAR = "{pathvar}";
  public static final String GRAPHQL_QUERY_USERS = "Users";
  public static final String GRAPHQL_QUERY_USER = "User";
  public static final String GRAPHQL_QUERY_USER_PAGINATED = "getPaginatedUserTableData";
  public static final String GRAPHQL_QUERY_USER_PAGINATED_ASC = 
      "getPaginatedUserByFiltersSortVariableAscending";
  public static final String GRAPHQL_QUERY_USER_PAGINATED_DESC = 
      "getPaginatedUserByFiltersSortVariableDescending";
  public static final String GRAPHQL_QUERY_USER_BY_USER_ID = "getUserByUserId";
  public static final String GRAPHQL_QUERY_USER_SCHOOL_BY_USER_ID = "getUserSchoolByUserId";
  public static final String GRAPHQL_QUERY_SCHOOLS = "Schools";
  public static final String GRAPHQL_QUERY_SCHOOLS_BY_USER_ID = "SchoolsByUserId";
  public static final String GRAPHQL_QUERY_SCHOOL_ID_BY_CODE = "SchoolIdByCode";
  public static final String GRAPHQL_QUERY_SCHOOL = "School";
  public static final String GRAPHQL_QUERY_SCHOOLSBYID = "SchoolById";
  public static final String GRAPHQL_QUERY_SCHOOLS_PAGINATED = "getPaginatedSchoolTableData";
  public static final String GRAPHQL_QUERY_SCHOOLS_ALL = "getAllSchoolsIdAndNameByString";
  public static final String GRAPHQL_QUERY_SCHOOLS_ALL_SELECTED = 
      "getAllSelectedSchoolsIdAndNameByString";

  public static final String GRAPHQL_QUERY_PRODUCTS = "Products";
  public static final String GRAPHQL_QUERY_GET_ALL_PRODUCTS = "getAllProducts";
  public static final String GRAPHQL_QUERY_PRODUCT_PAGINATED = "getPaginatedProductTableData";
  public static final String GRAPHQL_QUERY_GET_MULTIPLE_PRODUCTS = "getMultipleProducts";
  public static final String GRAPHQL_QUERY_GET_PRODUCTS_BY_IDS = "getProductsByIds";
  public static final String GRAPHQL_QUERY_GET_MULTIPLE_PRODUCTS_BY_ID = "getMultipleProductsById";
  public static final String GRAPHQL_QUERY_GET_PRODUCT = "getProduct";
  public static final String GRAPHQL_QUERY_GET_PRODUCT_BY_ID = "getProductById";
  public static final String GET_PRODUCT_DATA_BY_PRODUCT_ID = "getProductDataByProductId";
  public static final String GRAPHQL_QUERY_GET_PRODUCT_BY_ACCESS_CODE = "getProductsByAccessCode";
  public static final String GRAPHQL_QUERY_PRODUCT = "Product";
  public static final String GRAPHQL_QUERY_PANELS = "Panels";
  public static final String GRAPHQL_QUERY_PANEL = "Panel";
  public static final String GRAPHQL_QUERY_CLASSROOMS = "Classrooms";
  public static final String GRAPHQL_QUERY_CLASSROOMS_BY_PRODUCTID = "ClassroomsByProductId";
  public static final String GRAPHQL_QUERY_GET_SCHOOL_ID_BY_TEACHER_CODE = 
      "getSchoolIdByTeacherCode";
  public static final String GRAPHQL_QUERY_GET_SCHOOL_ID_BY_STUDENT_CODE = 
      "getSchoolIdByStudentCode";
  public static final String GRAPHQL_QUERY_CLASSROOMS_BY_LIST = "getMultipleClassroomsById";

  public static final String GRAPHQL_QUERY_CHECK_AVAILABLE_USERNAME = "checkUsernameUniqueness";
  public static final String GRAPHQL_AUTHENTICATE = "authenticate";
  public static final String GRAPHQL_QUERY_GETUSERBYUSERNAME = "UserLoginUsername";
  public static final String GRAPHQL_QUERY_GETUSERBYEMAIL = "UserLoginEmail";
  public static final String GRAPHQL_QUERY_GETUSERFROMSAM = "getUser";
  public static final String GRAPHQL_QUERY_EMAILEXISTS = "emailExists";
  public static final String GRAPHQL_QUERY_CLASSROOM = "Classroom";
  public static final String SCHOOL_SETUP = "schoolSetup";
  public static final String IAM_URI = "iamUri";
  public static final String SAM_URI = "samUri";
  public static final String LOGOUT_URI = "logout";

  public static final String GRAPHQL_QUERY_VIEWER = "getViewerDataByUserId";
  public static final String GRAPHQL_QUERY_VIEWER_PRODUCT = "getViewerProductsByUserId";
  public static final String CREATE_VIEWER = "addViewerDetailsViaSignup";
  public static final String CHANGE_VIEWER_PASS_WORD = "updateViewerPassByUserId";
  public static final String GRAPHQL_QUERY_CHECK_AVAILABLE_VIEWER_USERNAME = 
      "checkUsernameUniqueness";
  public static final String UPDATE_EMAIL_BY_USERID = "updateViewerEmailByUserId";
  public static final String UPDATE_TEACHERPROTAL_BY_USERID = "updateTeachersPortalPreferences";
  public static final String GRAPHQL_QUERY_GETROLE_BY_ID = "getViewerRoleById";
  public static final String GRAPHQL_QUERY_ISSCHOOLSSO = "isSchoolSSO";
  public static final String GRAPHQL_QUERY_GET_VIEWERPRODUCTS_BY_ID = "getViewerProductsByUserId";
  public static final String GRAPHQL_QUERY_GET_CLASSROOM = "getClassroomById";
  public static final String GRAPHQL_QUERY_GET_CLASSROOM_BY_PRODID_ID = "getClassroomByIdandProdId";
  public static final String GRAPHQL_QUERY_GET_CLASSROOM_BY_CODE = "getClassroomByCode";

  public static final String GRAPHQL_MUTATION_ADD_CLASSROOM = "addClassroom";
  public static final String GRAPHQL_MUTATION_REMOVE_CLASSROOM = "removeClassroom";
  public static final String GRAPHQL_MUTATION_UPDATE_CLASSROOM = "updateClassroom";

  public static final String GRAPHQL_MUTATION_ADD_PRODUCT = "addProduct";
  public static final String GRAPHQL_MUTATION_REMOVE_PRODUCT = "removeProduct";
  public static final String GRAPHQL_MUTATION_UPDATE_PRODUCT = "updateProduct";

  public static final String GRAPHQL_MUTATION_ADD_PANEL = "addPanel";
  public static final String GRAPHQL_MUTATION_REMOVE_PANEL = "removePanel";
  public static final String GRAPHQL_MUTATION_UPDATE_PANEL = "updatePanel";

  public static final String GRAPHQL_MUTATION_ADD_SCHOOL = "addSchool";
  public static final String GRAPHQL_MUTATION_REMOVE_SCHOOL = "removeSchool";
  public static final String GRAPHQL_MUTATION_DEACTIVATE_SCHOOL = "deactivateSchool";
  public static final String GRAPHQL_MUTATION_UPDATE_SCHOOL = "updateSchool";
  public static final String GRAPHQL_MUTATION_UPDATE_USER_BY_SCHOOL_ID = 
      "updateUserSchoolBySchoolId";
  public static final String GRAPHQL_MUTATION_UPDATE_STUDENET_PREFERENCE = 
      "updateStudentsPortalPreferences";
  public static final String GRAPHQL_MUTATION_UPDATE_MULTIPLE_USER_STATUS = 
	      "updateMultipleUsersStatusById";

  public static final String SPEC_IGNORE_FIRST_LEVEL = "IgnoreFirstLevel";
  public static final String SPEC_IGNORE_TWO_LEVELS = "IgnoreTwoLevels";
  public static final String SPEC_OPERATIONS_TO_QUERY = "OperationsToQuery";
  public static final String QUERIES_INFO = "queries_info";
  public static final String GUEST_OPERATIONS="guestOperations";
  public static final String GRAPHQL_QUERY_SCHOOL_DATA_BY_SCHOOL_ID = "getSchoolDataBySchoolId";

  public static final String GRAPHQL_MUTATION_ADD_LICENCE = "addLicence";
  public static final String GRAPHQL_MUTATION_GET_LICENCE = "getLicence";
  public static final String UPDATE_LICENCE = "updateLicence";
  public static final String DELETE_LICENCE = "deleteLicence";
  public static final String GRAPHQL_MUTATION_ADD_LICENCE_HISTORY = "addLicenceHistory";
  public static final String PGE_GRAPHQL_QUERY_GET_LICENCE = "getPaginatedLicenceTableData";
  public static final String PGE_GRAPHQL_QUERY_GET_LICENCE_HISTORY = "getPaginatedLicenceHistoryTableData";
  
  public static final String GRAPHQL_MUTATION_ADD_LICENCE_HOLDER = "addLicenceHolder";
  public static final String GRAPHQL_MUTATION_GET_LICENCE_HOLDER = "getLicenceHolder";
  public static final String UPDATE_LICENCE_HOLDER = "updateLicenceHolder";
  public static final String PGE_GRAPHQL_QUERY_GET_LICENCE_HOLDER = "getPaginatedHolderLicenceTableData";
  public static final String UPDATE_LICENCE_HOLDER_BY_ORGID = "updateLicenceHolderByOrganisationId";
  public static final String UPDATE_LICENCE_HOLDER_BY_SCHOOLID = "updateLicenceHolderBySchoolId";
  public static final String ALLOCATE_LICENCE_TO_LICENCE_HOLDER_FOR_SCHOOL = "allocateLicenceToLicenceHolderForSchool";
  public static final String ALLOCATE_LICENCE_TO_LICENCE_HOLDER_FOR_ORG = "allocateLicenceToLicenceHolderForOrganisation";
  
  public static final String GRAPHQL_MUTATION_ADD_LICENCE_HOLDER_HISTORY = "addLicenceHolderHistory";
  public static final String GRAPHQL_MUTATION_GET_LICENCE_HOLDER_HISTORY = "getLicenceHolderHistory";
  public static final String UPDATE_LICENCE_HOLDER_HISTORY = "updateLicenceHolderHistory";
  public static final String PGE_GRAPHQL_QUERY_GET_LICENCE_HOLDER_HISTORY = "getPaginatedLicenceHolderHistoryTableData";
  

  public static final String SAM_MAP = "samMap";
  public static final String MONGO_MAP = "mongoMap";
  
  
  public static final String CREATE_MULTI_ACCESS_CODES = "generateMultipleAccessCodes";
  public static final String CREATE_LARGER_ACCESS_CODES = "generateLargeNumberOfAccessCodes";	
  public static final String CREATE_ACCESS_CODE = "generateAccessCode";
  public static final String GET_ACCESS_CODE = "getAccessCode";
  public static final String UPDATE_ACCESS_CODE = "updateAccessCode";
  public static final String VOUCHER_REPO = "voucher_repo";
  public static final String ACCESS_CODE_REPO = "access_code_repo";
  public static final String SUBSCRIPTION_REPO = "subscription_repo";
  public static final String PGE_GRAPHQL_QUERY_GET_ACCESS_CODE = "getPaginatedAccessCodeTableData";
  public static final String PGE_GRAPHQL_QUERY_GET_ACCESS_CODE_HISTORY = "getPaginatedAccessCodeHistory";
  public static final String PGE_GRAPHQL_QUERY_GET_CLASSROOM = "getPaginatedClassroomTableData";
  public static final String LICENCE_REPO = "licence_repo";
  public static final String LICENCE_HISTORY_REPO = "licence_history_repo";
  
  public static final String CREATE_ACCESS_CODE_SUBSCRIPTION = "generateAccessCodeSubscription";
  public static final String CREATE_VOUCHER_CODE_SUBSCRIPTION = "generateVoucherCodeSubscription";
  public static final String GET_ACCESS_CODE_SUBSCRIPTION = "getAccessCodeSubscription";
  public static final String UPDATE_ACCESS_CODE_SUBSCRIPTION = "updateAccessCodeSubscription";
  
  public static final String LICENCE_HOLDER_REPO = "licence_holder_repo";
  public static final String LICENCE_HOLDER_HISTORY_REPO = "licence_holder_history_repo";
  
  public static final String CREATE_VOUCHER_CODE = "generateVoucherCode";
  public static final String GET_VOUCHER_CODE = "getVoucherCode";
  public static final String UPDATE_VOUCHER_CODE = "updateVoucherCode";
  public static final String CREATE_MULTI_VOUCHER_CODES = "generateMultipleVoucherCodes";
  public static final String CREATE_LARGENUMBER_VOUCHER_CODES = "generateLargeNumberOfVoucherCodes";
  public static final String GRAPHQL_QUERY_GET_PRODUCT_BY_VOUCHER_CODE = "getProductsByVoucherCode";
  public static final String ALLOCATE_LINCENCE_TO_USER = "allocateLicenceToUser";
  public static final String REDEEM_VOUCHER_CODE = "redeemVoucherCode";
  public static final String PGE_GRAPHQL_QUERY_GET_VOUCHER = "getPaginatedVoucherCodeTableData";
  public static final String PGE_GRAPHQL_QUERY_GET_VOUCHER_HISTORY = "getPaginatedVoucherCodeHistory";
  public static final String UPDATE_ACCESS_CODE_HISTORY = "updateAccessCodeHistory";
  public static final String ACCESS_CODE_HISTORY_REPO = "access_code_history_repo";
  public static final String VOUCHER_CODE_HISTORY_REPO = "voucher_code_history_repo";
  public static final String UPDATE_VOUCHER_CODE_HISTORY = "updateVoucherCodeHistory";
  public static final String REQUEST_ID = "requestId";
  public static final String CURRENT_QUERY = "currentQuery";

  public static final String PGE_GRAPHQL_QUERY_GET_VOUCHERSEARCH = "voucherCodeSearch";
  public static final String PGE_GRAPHQL_QUERY_GET_ACCESSCODESEARCH = "accessCodeSearch";
  public static final String PGE_GRAPHQL_QUERY_GET_SCHOOLSEARCH = "schoolSearch";
  public static final String PGE_GRAPHQL_QUERY_GET_USERSEARCH = "userSearch";
  public static final String PGE_GRAPHQL_QUERY_GET_CLASSROOMSEARCH = "classroomSearch";
  public static final String PGE_GRAPHQL_QUERY_GET_PRODUCTSEARCH="productSearch";
  
  public static final String ORGANISATION_REPO = "organisationRepo";
  public static final String GRAPHQL_QUERY_ORGANISATION_PAGINATED = "getPaginatedOrganisationTableData";
  public static final String GRAPHQL_MUTATE_UPDATE_ORGANISATION = "updateOrganisation";
  public static final String GRAPHQL_QUERY_GET_MULTIPLE_ORGANISATION_BY_ID = "getMultipleOrganisationByIds";
  public static final String UPDATE_PRODUCT_DETAILS = "update_product_details";
  public static final String GET_USER_SUBSCRIBED_ACCESS_CODES = "get_user_subscribed_access_codes";
  
  public static final String PAGINATION_GRAPHQL_QUERY_GET_APP_NOTIFICATION = "getAppNotifications";
  public static final String APP_NOTIFICATION_REPO = "appNotificationRepo";
  
  public static final String RESOURCE_REPO = "resourceRepo";
  public static final String GET_PAGINATED_RESOURCE_TABLE_DATA = "getPaginatedResourceTableData";
  public static final String CREATE_RESOURCE = "createResource"; 
  public static final String UPDATE_RESOURCE = "updateResource";
  public static final String GET_RESOURCE_DATA_BY_RESOURCE_ID = "getResourceDataByResourceId";

  //Bulk Upload
  public static final String ADD_BULK_UPLOAD_USER = "addBulkUploadUserJob";
  public static final String GET_BULK_UPLOAD_JOB_DETAILS = "getbulkUploadJobDetails";
  public static final String GET_PAGINATED_BULK_UPLOAD_JOB_TABLE = "getPaginatedBulkUploadJobTable";
  
  //Authentication Login
  public static final String LOGIN = "login";
  public static final String LOGOUT = "logout";
  
  public static final String HYPHEN = "-";

}