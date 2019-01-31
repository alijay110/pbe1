package com.pearson.sam.bridgeapi.resolvers;

import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.ADD_BULK_UPLOAD_USER;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.ADD_USER;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.CREATE_BULK_USER;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.CREATE_USER;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GET_BULK_UPLOAD_JOB_DETAILS;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GET_PAGINATED_BULK_UPLOAD_JOB_TABLE;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_MUTATION_UPDATE_MULTIPLE_USER_STATUS;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_MUTATION_UPDATE_STUDENET_PREFERENCE;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_MUTATION_UPDATE_USER_BY_SCHOOL_ID;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_QUERY_CHECK_AVAILABLE_USERNAME;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_QUERY_GETUSERBYEMAIL;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_QUERY_GETUSERBYUSERNAME;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_QUERY_GETUSERFROMSAM;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_QUERY_USER;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_QUERY_USER_BY_USER_ID;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_QUERY_USER_PAGINATED;
//import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_QUERY_USER_PAGINATED_ASC;
//import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_QUERY_USER_PAGINATED_DESC;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_QUERY_USER_SCHOOL_BY_USER_ID;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.LOGIN;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.LOGOUT;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.PGE_GRAPHQL_QUERY_GET_USERSEARCH;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.UPDATE_USER;
import static com.pearson.sam.bridgeapi.util.Utils.copyNonNullProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import com.pearson.sam.bridgeapi.config.AuthContext;
import com.pearson.sam.bridgeapi.elasticsearch.model.UserSearch;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGraphqlException;
import com.pearson.sam.bridgeapi.ibridgeservice.IUserBridgeService;
import com.pearson.sam.bridgeapi.model.BulkUploadJobDetails;
import com.pearson.sam.bridgeapi.model.BulkUploadJobSummary;
import com.pearson.sam.bridgeapi.model.BulkUserOutput;
import com.pearson.sam.bridgeapi.model.MethodType;
import com.pearson.sam.bridgeapi.model.Sort;
import com.pearson.sam.bridgeapi.model.StatusEnum;
import com.pearson.sam.bridgeapi.model.Token;
import com.pearson.sam.bridgeapi.model.User;
import com.pearson.sam.bridgeapi.security.ISessionFacade;
import com.pearson.sam.bridgeapi.validators.ModelValidator;

import graphql.ErrorType;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLEnvironment;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.GraphQLRootContext;
import io.leangen.graphql.execution.ResolutionEnvironment;

/**
 * 
 * @author VKu99Ma
 *
 */
@Component
@XRayEnabled
public class UserResolver {

  private static final Logger logger = LoggerFactory.getLogger(UserResolver.class);
  private static final String UID = "userId";
  private static final String USERNAME = "userName";
  private static final String EMAIL = "email";

  @Autowired
  private ISessionFacade sessionFacade;

  @Autowired
  ModelValidator userValidator;

  @Autowired
  private IUserBridgeService userBridgeService;

  /**
   * This Method will add the User.
   * 
   * @param data
   * @param context
   * @param env
   * @return User
   */
  @GraphQLMutation(name = CREATE_USER)
  public User signupUser(@GraphQLArgument(name = "data") User data,
      @GraphQLRootContext AuthContext context, @GraphQLEnvironment ResolutionEnvironment env) {

    userValidator.validateModel(data, MethodType.CREATE.toString());
    return userBridgeService.createUser(data);
  }

  /**
   * This Method will fetch User Details based on input uid.
   * 
   * @param userId
   * @param context
   * @param env
   * @return User
   */
  @GraphQLQuery(name = GRAPHQL_QUERY_USER_BY_USER_ID)
  public User getUserByUserId(@GraphQLArgument(name = "uid") String userId,
      @GraphQLRootContext AuthContext context, @GraphQLEnvironment ResolutionEnvironment env) {
    logSessionUser();
    userValidator.validateEmptyNullValue(userId, UID);
    return userBridgeService.getUserById(userId);
  }

  /**
   * This Method will fetch User Details based on input userId.
   * 
   * @param userId
   * @param context
   * @param env
   * @return User
   */
  @GraphQLQuery(name = GRAPHQL_QUERY_USER, description = "Get User Details By userId")
  public User getUser(@GraphQLArgument(name = "userId") String userId,
      @GraphQLRootContext AuthContext context, @GraphQLEnvironment ResolutionEnvironment env) {
    logSessionUser();
    userValidator.validateEmptyNullValue(userId, UID);
    return userBridgeService.getUserById(userId);
  }

  /**
   * This Method will update the User Details based on input data.
   * 
   * @param userId
   * @param data
   * @param context
   * @return User
   */
  @GraphQLMutation(name = UPDATE_USER)
  public User updateUser(@GraphQLArgument(name = "Id") String userId,
      @GraphQLArgument(name = "data") User data, @GraphQLRootContext AuthContext context) {
    validateUpdateUid(data);
    data.setUid(userId);
    if (null != data.getStatus() && Optional.ofNullable(data.getStatus()).isPresent()) {
      populateStatusField(data);
    }
    return userBridgeService.updateUser(data);
  }

  /**
   * This Method will give the paginated Users as response.
   * 
   * @param pageNumber
   * @param pageLimit
   * @param filter
   * @param sm
   * @param sort
   * @param context
   * @return Page<User>
   */
  // @GraphQLQuery(name = GRAPHQL_QUERY_USER_PAGINATED)
  public Page<User> getPaginatedUser(
      @GraphQLArgument(name = "offset", defaultValue = "0") Integer pageNumber,
      @GraphQLArgument(name = "limit", defaultValue = "15") Integer pageLimit,
      @GraphQLArgument(name = "filter", defaultValue = "{}") User filter,
      @GraphQLArgument(name = "filterPattern", defaultValue = "5") StringMatcher sm,
      @GraphQLArgument(name = "sort", defaultValue = "{\"field\":\"uid\",\"order\":\"ASC\"}") Sort sort,
      @GraphQLRootContext AuthContext context) {
    logSessionUser();
    Pageable pageable = PageRequest.of(pageNumber, pageLimit,
        Direction.valueOf(sort.getOrder().toString()), sort.getField());
    return userBridgeService.pageIt(pageable,
        Example.of(filter, ExampleMatcher.matching().withStringMatcher(sm)));
  }

  /**
   * This Method will give the paginated Users as response.
   * 
   * @param pageNumber
   * @param pageLimit
   * @param filter
   * @param sm
   * @param sort
   * @param context
   * @return Page<User>
   */
  // @GraphQLQuery(name = GRAPHQL_QUERY_USER_PAGINATED_ASC)
  public Page<User> getPaginatedUserAsc(
      @GraphQLArgument(name = "offset", defaultValue = "0") Integer pageNumber,
      @GraphQLArgument(name = "limit", defaultValue = "15") Integer pageLimit,
      @GraphQLArgument(name = "filter", defaultValue = "{}") User filter,
      @GraphQLArgument(name = "filterPattern", defaultValue = "5") StringMatcher sm,
      @GraphQLArgument(name = "sort", defaultValue = "{\"field\":\"uid\",\"order\":\"ASC\"}") Sort sort,
      @GraphQLRootContext AuthContext context) {
    logSessionUser();
    Pageable pageable = PageRequest.of(pageNumber, pageLimit,
        Direction.valueOf(sort.getOrder().toString()), sort.getField());
    return userBridgeService.pageIt(pageable,
        Example.of(filter, ExampleMatcher.matching().withStringMatcher(sm)));
  }

  /**
   * This Method will give the paginated Users as response.
   * 
   * @param pageNumber
   * @param pageLimit
   * @param filter
   * @param sm
   * @param sort
   * @param context
   * @return Page<User>
   */
  // @GraphQLQuery(name = GRAPHQL_QUERY_USER_PAGINATED_DESC)
  public Page<User> getPaginatedUserDesc(
      @GraphQLArgument(name = "offset", defaultValue = "0") Integer pageNumber,
      @GraphQLArgument(name = "limit", defaultValue = "15") Integer pageLimit,
      @GraphQLArgument(name = "filter", defaultValue = "{}") User filter,
      @GraphQLArgument(name = "filterPattern", defaultValue = "5") StringMatcher sm,
      @GraphQLArgument(name = "sort", defaultValue = "{\"field\":\"uid\",\"order\":\"DESC\"}") Sort sort,
      @GraphQLRootContext AuthContext context) {
    logSessionUser();
    Pageable pageable = PageRequest.of(pageNumber, pageLimit,
        Direction.valueOf(sort.getOrder().toString()), sort.getField());
    return userBridgeService.pageIt(pageable,
        Example.of(filter, ExampleMatcher.matching().withStringMatcher(sm)));
  }

  /**
   * This Method will fetch User Details based on input userName.
   * 
   * @param userName
   * @param passwd
   * @param context
   * @param env
   * @return User
   */
  @GraphQLQuery(name = GRAPHQL_QUERY_GETUSERBYUSERNAME)
  public User getUserByUserName(@GraphQLArgument(name = "userName") String userName,
      @GraphQLArgument(name = "passwd") String passwd, @GraphQLRootContext AuthContext context,
      @GraphQLEnvironment ResolutionEnvironment env) {
    logSessionUser();
    userValidator.validateEmptyNullValue(userName, USERNAME);
    return userBridgeService.getUserByUserName(userName);
  }

  /**
   * This Method will fetch User Details based on input email.
   * 
   * @param email
   * @param passwd
   * @param context
   * @param env
   * @return User
   */
  @GraphQLQuery(name = GRAPHQL_QUERY_GETUSERBYEMAIL)
  public User getUserByEmail(@GraphQLArgument(name = "email") String email,
      @GraphQLArgument(name = "passwd") String passwd, @GraphQLRootContext AuthContext context,
      @GraphQLEnvironment ResolutionEnvironment env) {
    logSessionUser();
    userValidator.validateEmptyNullValue(email, EMAIL);
    User user = new User();
    user.setEmail(email);
    return userBridgeService.findByEmail(user);
  }

  /**
   * This Method will add the User.
   * 
   * @param data
   * @param context
   * @param env
   * @return User
   */
  @GraphQLMutation(name = ADD_USER)
  public User addUser(@GraphQLArgument(name = "data") User data,
      @GraphQLRootContext AuthContext context, @GraphQLEnvironment ResolutionEnvironment env) {

    userValidator.validateModel(data, MethodType.CREATE.toString());
    return userBridgeService.createUser(data);
  }

  /**
   * This Method will create the bulk User.
   * 
   * @param users
   * @param context
   * @param env
   * @return List<BulkUserOutput>
   */
  @GraphQLMutation(name = CREATE_BULK_USER)
  public List<BulkUserOutput> userUpload(@GraphQLArgument(name = "data") List<User> users,
      @GraphQLRootContext AuthContext context, @GraphQLEnvironment ResolutionEnvironment env) {
    List<BulkUserOutput> usersList = new ArrayList<>();
    BulkUserOutput bulkUser = null;
    for (User user : users) {
      try {
        signupUser(user, context, env);
        bulkUser = new BulkUserOutput();
        BeanUtils.copyProperties(user, bulkUser);
        bulkUser.setUserAdded("success");
        usersList.add(bulkUser);
      } catch (Exception e) {
        bulkUser = new BulkUserOutput();
        bulkUser.setUserAdded("failed");
        bulkUser.setErrorText(e.getMessage());
        usersList.add(bulkUser);
      }
    }
    return usersList;
  }

  /**
   * This Method will check the User Availability.
   * 
   * @param userName
   * @param context
   * @param env
   * @return Boolean
   */
  @GraphQLQuery(name = GRAPHQL_QUERY_CHECK_AVAILABLE_USERNAME)
  public Boolean checkUserNameAvailability(@GraphQLArgument(name = "userName") String userName,
      @GraphQLRootContext AuthContext context, @GraphQLEnvironment ResolutionEnvironment env) {
    return userBridgeService.checkAvailability(userName);
  }

  /**
   * This Method will fetch User Details based on input userName.
   * 
   * @param userName
   * @param context
   * @param env
   * @return User
   */
  @GraphQLQuery(name = GRAPHQL_QUERY_GETUSERFROMSAM)
  public User getUserFromSam(@GraphQLArgument(name = "userName") String userName,
      @GraphQLRootContext AuthContext context, @GraphQLEnvironment ResolutionEnvironment env) {
    logSessionUser();
    userValidator.validateEmptyNullValue(userName, USERNAME);
    return userBridgeService.getUserByUserName(userName);
  }

  /**
   * This Method will fetch User Details based on input uid.
   * 
   * @param userId
   * @param context
   * @param env
   * @return User
   */
  @GraphQLQuery(name = GRAPHQL_QUERY_USER_SCHOOL_BY_USER_ID)
  public User getUserSchoolByUserId(@GraphQLArgument(name = "uid") String userId,
      @GraphQLRootContext AuthContext context, @GraphQLEnvironment ResolutionEnvironment env) {
    logSessionUser();
    userValidator.validateEmptyNullValue(userId, UID);
    return userBridgeService.getUserById(userId);
  }

  /**
   * This Method will update the User School Details based on input userData.
   * 
   * @param user
   * @param context
   * @param env
   * @return User
   */
  @GraphQLMutation(name = GRAPHQL_MUTATION_UPDATE_USER_BY_SCHOOL_ID)
  public User updateUserSchoolId(@GraphQLArgument(name = "userData") User user,
      @GraphQLRootContext AuthContext context, @GraphQLEnvironment ResolutionEnvironment env) {
    logSessionUser();
    return userBridgeService.updateUserSchoolId(user);
  }

  /**
   * This Method will update the User StudentPreferences Details based on input userData.
   * 
   * @param user
   * @param context
   * @param env
   * @return User
   */
  @GraphQLMutation(name = GRAPHQL_MUTATION_UPDATE_STUDENET_PREFERENCE)
  public User updateStudentPreferences(@GraphQLArgument(name = "userData") User user,
      @GraphQLRootContext AuthContext context, @GraphQLEnvironment ResolutionEnvironment env) {
    return userBridgeService.updateSubjectPreference(user);
  }

  /**
   * This Method will update the Multiple User "status" based on the input usersId and status
   * 
   * @param userIds
   * @param status
   * @return List<User>
   */
  @GraphQLMutation(name = GRAPHQL_MUTATION_UPDATE_MULTIPLE_USER_STATUS, description = "Update Multiple Users Status ById")
  public List<User> updateMultipleUsersStatusById(
      @GraphQLArgument(name = "usersId") List<String> usersId,
      @GraphQLArgument(name = "status") String status, @GraphQLRootContext AuthContext context,
      @GraphQLEnvironment ResolutionEnvironment env) {
    return userBridgeService.updateMultipleUsersStatusById(usersId, status);
  }

  private User populateStatusField(User user) {
    String samStatus = "";
    String mongoStatus = "";
    if (null != user && Optional.ofNullable(user).isPresent()) {
      samStatus = user.getStatus().toUpperCase();
    }
    if (samStatus.contains(StatusEnum.PENDING.toString())) {
      samStatus = StatusEnum.ACTIVE.toString();
      mongoStatus = StatusEnum.PENDING.toString();
    } else {
      samStatus = user.getStatus();
    }
    user.setStatus(samStatus.toLowerCase());
    user.setUserStatus(mongoStatus.toLowerCase());
    return user;
  }

  private void logSessionUser() {
    //logger.info("**Logged in user is : {}", sessionFacade.getLoggedInUser(false));
  }

  private void validateUpdateUid(User user) {
    if (Optional.ofNullable(user.getUid()).isPresent()) {
      throw new BridgeApiGraphqlException(ErrorType.ValidationError, "You can't update the uid");
    }
  }

  /**
   * @param bulkUploadJobSummary
   * @param bulkUploadJobDetails
   * @return List<User>
   */
  @GraphQLMutation(name = ADD_BULK_UPLOAD_USER, description = "Add Bulk User Upload")
  public BulkUploadJobSummary addBulkUploadUserJob(
      @GraphQLArgument(name = "bulkUploadJobSummary") BulkUploadJobSummary bulkUploadJobSummary,
      @GraphQLArgument(name = "bulkUploadJobDetails") BulkUploadJobDetails bulkUploadJobDetails) {
    return userBridgeService.addBulkUploadUserJob(bulkUploadJobSummary, bulkUploadJobDetails);
  }

  /**
   * @param bulkUploadJobSummary
   * @param bulkUploadJobDetails
   * @return List<User>
   */
  @GraphQLQuery(name = GET_BULK_UPLOAD_JOB_DETAILS, description = "Get Bulk Userupload  Details")
  public BulkUploadJobDetails getbulkUploadJobDetails(
      @GraphQLArgument(name = "jobId") Integer jobId) {
    return userBridgeService.getbulkUploadJobDetails(jobId);
  }

  /**
   * This Method will give the paginated BulkUploadJobSummary as response.
   * 
   * @param pageNumber
   * @param pageLimit
   * @param filter
   * @param sm
   * @param sort
   * @param context
   * @return Page<User>
   */
  @GraphQLQuery(name = GET_PAGINATED_BULK_UPLOAD_JOB_TABLE, description = "Get Paginated Bulk Upload User")
  public Page<BulkUploadJobSummary> getPaginatedBulkUploadJobTable(
      @GraphQLArgument(name = "offset", defaultValue = "0") Integer pageNumber,
      @GraphQLArgument(name = "limit", defaultValue = "15") Integer pageLimit,
      @GraphQLArgument(name = "filter", defaultValue = "{}") BulkUploadJobSummary filter,
      @GraphQLArgument(name = "filterPattern", defaultValue = "5") StringMatcher sm,
      @GraphQLArgument(name = "sort", defaultValue = "{\"field\":\"jobId\",\"order\":\"DESC\"}") Sort sort,
      @GraphQLRootContext AuthContext context) {
    logSessionUser();
    Pageable pageable = PageRequest.of(pageNumber, pageLimit,
        Direction.valueOf(sort.getOrder().toString()), sort.getField());
    return userBridgeService.pageItBulk(pageable,
        Example.of(filter, ExampleMatcher.matching().withStringMatcher(sm)));
  }

  /**
   * This Method will give the paginated BulkUploadJobSummary as response.
   * 
   * @param pageNumber
   * @param pageLimit
   * @param filter
   * @param sm
   * @param sort
   * @param context
   * @return Page<User>
   */
  @GraphQLQuery(name = PGE_GRAPHQL_QUERY_GET_USERSEARCH, description = "Search user in elastic search")
  public Page<UserSearch> getPaginatedBulkUploadJobTable(
      @GraphQLArgument(name = "offset", defaultValue = "0") Integer pageNumber,
      @GraphQLArgument(name = "limit", defaultValue = "15") Integer pageLimit,
      @GraphQLArgument(name = "filter", defaultValue = "{}") String query,
      @GraphQLRootContext AuthContext context) {
    logSessionUser();
    Pageable pageable = PageRequest.of(pageNumber, pageLimit);
    return userBridgeService.search(pageable, query);
  }

  @GraphQLQuery(name = GRAPHQL_QUERY_USER_PAGINATED, description = "paginated users (elastic search)")
  public Page<User> getUserSearch(
      @GraphQLArgument(name = "offset", defaultValue = "0") Integer pageNumber,
      @GraphQLArgument(name = "limit", defaultValue = "15") Integer pageLimit,
      @GraphQLArgument(name = "filter", defaultValue = "{}") User filter,
      @GraphQLArgument(name = "filterPattern", defaultValue = "5") StringMatcher sm,
      @GraphQLArgument(name = "sort", defaultValue = "{\"field\":\"userName\",\"order\":\"DESC\"}") Sort sort,
      @GraphQLRootContext AuthContext context) {
    logSessionUser();
    Pageable pageable = PageRequest.of(pageNumber, pageLimit,
        Direction.valueOf(sort.getOrder().toString()), sort.getField());
    UserSearch query = new UserSearch();
    copyNonNullProperties(filter,query);
    Page<UserSearch> oldPage = userBridgeService.pageIt(pageable,query,sm);
    return new PageImpl<>(oldPage.getContent().stream().map(usrSrch -> new User(usrSrch)).collect(Collectors.toList()), pageable,
        oldPage.getTotalElements());
  }

    @GraphQLQuery(name = LOGIN)
	public Token login(@GraphQLArgument(name = "userName") String userName, @GraphQLArgument(name = "password") String password,
			@GraphQLRootContext AuthContext context,
			@GraphQLEnvironment ResolutionEnvironment env) {
		
		return userBridgeService.login(userName,password,context);
	}
	
    @GraphQLQuery(name = LOGOUT)
	public String logout(@GraphQLRootContext AuthContext context,
			@GraphQLEnvironment ResolutionEnvironment env) {
		
		return userBridgeService.logout(context);
	}
	
}
