package com.pearson.sam.bridgeapi.resolvers;

import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.CHANGE_VIEWER_PASS_WORD;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.CREATE_VIEWER;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_QUERY_CHECK_AVAILABLE_VIEWER_USERNAME;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_QUERY_GETROLE_BY_ID;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_QUERY_GET_VIEWERPRODUCTS_BY_ID;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_QUERY_ISSCHOOLSSO;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_QUERY_VIEWER;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.UPDATE_EMAIL_BY_USERID;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.UPDATE_FULLNAME_BY_USER_ID;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.UPDATE_TEACHERPROTAL_BY_USERID;

import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pearson.sam.bridgeapi.config.AuthContext;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGraphqlException;
import com.pearson.sam.bridgeapi.ibridgeservice.IUserBridgeService;
import com.pearson.sam.bridgeapi.model.MethodType;
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

@Component
public class ViewerResolver {

	private static final Logger logger = LoggerFactory.getLogger(ViewerResolver.class);
	private static final String USERID = "userId";
	private static final String EMAIL = "email";

	@Autowired
	private ISessionFacade sessionFacade;

	@Autowired
	private IUserBridgeService userBridgeService;

	@Autowired
	ModelValidator userValidator;

	/**
	 * This Method will fetch User Details based on input userId.
	 * 
	 * @param userId
	 * @return User
	 */
	@GraphQLQuery(name = GRAPHQL_QUERY_VIEWER)
	public User getUser(@GraphQLArgument(name = "userId") String userId, @GraphQLRootContext AuthContext context,
			@GraphQLEnvironment ResolutionEnvironment env) {
		logSessionUser();
		userValidator.validateEmptyNullValue(userId, USERID);
		return userBridgeService.getUserById(userId);
	}

	/**
	 * This Method will fetch User Details based on input userId.
	 * 
	 * @param userId
	 * @return User
	 */
	@GraphQLQuery(name = GRAPHQL_QUERY_GETROLE_BY_ID)
	public User getUserById(@GraphQLArgument(name = "userId") String userId, @GraphQLRootContext AuthContext context,
			@GraphQLEnvironment ResolutionEnvironment env) {
		logSessionUser();
		userValidator.validateEmptyNullValue(userId, USERID);
		return userBridgeService.getUserById(userId);
	}

	/**
	 * This Method will fetch User Details based on input userId.
	 * 
	 * @param userId
	 * @return User
	 */
	@GraphQLQuery(name = GRAPHQL_QUERY_GET_VIEWERPRODUCTS_BY_ID)
	public User getProductsByUid(@GraphQLArgument(name = "userId") String userId,
			@GraphQLRootContext AuthContext context, @GraphQLEnvironment ResolutionEnvironment env) {
		logSessionUser();
		userValidator.validateEmptyNullValue(userId, USERID);
		return userBridgeService.getUserById(userId);
	}

	/**
	 * This Method will fetch User Details based on input email.
	 * 
	 * @param email
	 * @return User
	 */
	@GraphQLQuery(name = GRAPHQL_QUERY_ISSCHOOLSSO)
	public User getUserByEmail(@GraphQLArgument(name = "email") String email, @GraphQLRootContext AuthContext context,
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
	 * @return User
	 */
	@GraphQLMutation(name = CREATE_VIEWER)
	public User signupUser(@GraphQLArgument(name = "data") User data, @GraphQLRootContext AuthContext context,
			@GraphQLEnvironment ResolutionEnvironment env) {
		userValidator.validateModel(data, MethodType.CREATE.toString());
		return userBridgeService.createUser(data);
	}

	/**
	 * This Method will update the User Details based on input data.
	 * 
	 * @param data
	 * @return User
	 */
	@GraphQLMutation(name = UPDATE_EMAIL_BY_USERID)
	public User updateEmailbyUid(@GraphQLArgument(name = "data") User data, @GraphQLRootContext AuthContext context) {
		User user = new User();
		if (validateEmailId(data)) {
			user.setEmail(data.getEmail());
			user.setUid(data.getUid());
		}
		return userBridgeService.updateUser(user);
	}

	/**
	 * This Method will update the User Details based on input data.
	 * 
	 * @param userId
	 * @return User
	 */
	@GraphQLMutation(name = UPDATE_TEACHERPROTAL_BY_USERID)
	public User updateTeacher(@GraphQLArgument(name = "Id") String userId, @GraphQLArgument(name = "data") User data,
			@GraphQLRootContext AuthContext context) {
		data.setUid(userId);
		data.setUserName(userId);
		return userBridgeService.updateUser(data);
	}

	/**
	 * This method will change the Password.
	 * 
	 * @param data
	 * @return User
	 */
	@GraphQLMutation(name = CHANGE_VIEWER_PASS_WORD)
	public User changePassword(@GraphQLArgument(name = "data") User data, @GraphQLRootContext AuthContext context) {
		User tempUser = new User();
		tempUser.setPasswd(data.getPasswd());
		tempUser.setUid(data.getUid());
		return userBridgeService.changePassword(tempUser);
	}

	/**
	 * This Method will check the User Availability.
	 * 
	 * @param userName
	 * @return Boolean
	 */
	@GraphQLQuery(name = GRAPHQL_QUERY_CHECK_AVAILABLE_VIEWER_USERNAME)
	public Boolean checkUserNameAvailability(@GraphQLArgument(name = "userName") String userName,
			@GraphQLRootContext AuthContext context, @GraphQLEnvironment ResolutionEnvironment env) {
		return userBridgeService.checkAvailability(userName);
	}

	/**
	 * This Method will update the User FullName Details based on input data.
	 * 
	 * @param data
	 * @return User
	 */
	@GraphQLMutation(name = UPDATE_FULLNAME_BY_USER_ID)
	public User updateViewerFullNameByUserId(@GraphQLArgument(name = "data") User data,
			@GraphQLRootContext AuthContext context) {
		User tempUser = new User();
		tempUser.setFullName(data.getFullName());
		tempUser.setUid(data.getUid());
		return userBridgeService.updateUser(tempUser);
	}

	private void logSessionUser() {
		//logger.info("**Logged in user is : {}", sessionFacade.getLoggedInUser(false));
	}

	private boolean validateEmailId(User user) {
		if ((!Optional.ofNullable(user.getEmail()).isPresent()) || (StringUtils.isEmpty(user.getEmail()))) {
			throw new BridgeApiGraphqlException(ErrorType.DataFetchingException, "Please provide valid email id!");
		}
		return true;
	}
}
