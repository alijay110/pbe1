package com.pearson.sam.bridgeapi.bridgeserviceimpl;

import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GET_USER_SUBSCRIBED_ACCESS_CODES;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.MONGO_MAP;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.QUERIES_INFO;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.SAM_MAP;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.ROLE_NAME_SHOULD_NOT_BE_EMPTY;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.USER_DOES_NOT_EXISTS;

import com.bazaarvoice.jolt.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.pearson.sam.bridgeapi.annotations.MongoSource;
import com.pearson.sam.bridgeapi.annotations.SamSource;
import com.pearson.sam.bridgeapi.config.AuthContext;
import com.pearson.sam.bridgeapi.elasticsearch.model.UserSearch;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGraphqlException;
import com.pearson.sam.bridgeapi.ibridgeservice.IUserBridgeService;
import com.pearson.sam.bridgeapi.iservice.IAuthService;
import com.pearson.sam.bridgeapi.iservice.IUserSearchService;
import com.pearson.sam.bridgeapi.iservice.IUserService;
import com.pearson.sam.bridgeapi.model.BulkUploadJobDetails;
import com.pearson.sam.bridgeapi.model.BulkUploadJobSummary;
import com.pearson.sam.bridgeapi.model.Licence;
import com.pearson.sam.bridgeapi.model.LicenceHolder;
import com.pearson.sam.bridgeapi.model.SessionUser;
import com.pearson.sam.bridgeapi.model.StatusEnum;
import com.pearson.sam.bridgeapi.model.Token;
import com.pearson.sam.bridgeapi.model.User;
import com.pearson.sam.bridgeapi.repository.LicenceHolderRespository;
import com.pearson.sam.bridgeapi.repository.LicenceRespository;
import com.pearson.sam.bridgeapi.samclient.AccessCodeSamClient;
import com.pearson.sam.bridgeapi.samclient.IAuthentication;
import com.pearson.sam.bridgeapi.security.ISessionFacade;
import com.pearson.sam.bridgeapi.serviceimpl.RoleService;
import com.pearson.sam.bridgeapi.util.Utils;

import graphql.ErrorType;
import graphql.GraphQLException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.dataloader.BatchLoader;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

/**
 * 
 * @author VKu99Ma
 *
 */
@Service
public class UserBridgeServiceImpl implements IUserBridgeService {

	private static final Logger logger = LoggerFactory.getLogger(UserBridgeServiceImpl.class);
	private static final String ROLES = "roles";
	private static final String DEACTIVATE = "deactivate";
	private static final String KEY = "User";
	private static final String MASTER = "MASTER";
	private static final String CREATE = "CREATE";
	private static final String UPDATE = "UPDATE";
	private static final String IDENTIFIER = "identifier";
	private static final String USERNAME = "userName";

	@Value("${SPEC_FILE}")
	private String specJsonFile;

//	@Autowired
//	private CacheService cacheService;

	@Autowired
	private IUserService userService;
	
	@Autowired
	private IUserSearchService userSearchService;

	@Autowired
	private IAuthService authService;

	@Autowired
	private AccessCodeSamClient accessCodeSamClient;

	@Autowired
	protected ISessionFacade sessionFacade;

	@Autowired
	private LicenceRespository licenceRespository;

	@Autowired
	private LicenceHolderRespository licenceHolderRespository;
	
	@Autowired
	private IAuthentication authenticationService;

	@Autowired
	MongoOperations mongo;
	
	@Autowired
	RoleService roleService;
	
	@Autowired
	@Qualifier(QUERIES_INFO)
	Map<String, Set<String>> queriesMap;

	private static Example<User> userEx = Example.of(new User());

	public void loadUsersToMaster() {
		initLoad(PageRequest.of(0, userService.getTotalCount().intValue()));
	}

	private int initLoad(Pageable pageable) {
		this.getAllUsersinList(userService.pageIt(pageable, userEx).getContent(), MASTER);
		return userService.pageIt(pageable, userEx).getTotalPages();
	}

	private SessionUser getLoggedInUser(boolean getLoggedInUser) {
		return sessionFacade.getLoggedInUser(getLoggedInUser);
	}

	/**
	 * This Method will add the User.
	 * 
	 * @param user
	 * @return User
	 */
	@Override
	public User createUser(User user) {
		validateCreateUser(user);
		userService.signUpUser(user);
		authService.createUserrole(user.getUserName(), getRoleBody(user, CREATE));
		userSearchService.save(getUserSearchObject(user));
		return getUserByUserName(user.getUserName());
	}

	private User fetchUserFromCacheOrLoad(User user, String masterKey) {
//		Object obj = cacheService.getObjectWithSessionId(user, masterKey, getUserIdentifier(user), KEY,
//				this::retrieveUser);
	  Object obj = this.retrieveUser(user);
	  user = Utils.convert(obj, new TypeReference<User>() {
	  });
	   userSearchService.insertIfNotFound(getUserSearchObject(user));
	   return user;
	}

	private void updateUserIntoCache(User user) {
	  // cacheService.update(user, getUserIdentifier(user), KEY, this::retrieveUser);
	}


	private void validateCreateUser(User user) {
		if (Optional.ofNullable(user.getStatus()).isPresent()) {
			checkStatusAvailable(user.getStatus());
		}
		populateMandatoryFields(user);
		/** Roles Validation **/
		validateRole(user);
	}

	@Override
	public User getUserById(String uid) {
		return fetchUserFromCacheOrLoad(new User(uid), getmasterKey());
	}

	private User retrieveUser(Object obj) {
		User user = Utils.convert(obj, new TypeReference<User>() {
		});
		user = userService.getUser(user);
		populateUserWithExternalData(user);
		populateUserIdentifier(user);
		return user;
	}

	/**
   * populateUserIdentifier.
   * @param user  
   */
  private void populateUserIdentifier(User user) {
    if(StringUtils.isBlank(user.getIdentifier()))
    {
        user.setIdentifier(getUserIdentifier(user));
    }
  }

  private void populateUserWithExternalData(User user) {
		Set<String> roles = authService.getRoles(user.getIdentifier());
		user.setRoles(roles);

		user.setProduct(getProducts(user));
	}

	private String getmasterKey() {
		return MASTER;
	}

	/**
	 * This Method will fetch User Details based in userName.
	 * 
	 * @param user
	 * @return User
	 */
	@Override
	public User getUserByUserName(String userName) {
		User user = userService.getUserName(userName);
		if (!Optional.ofNullable(user).isPresent()) {
			throw new BridgeApiGraphqlException(ErrorType.ValidationError, Utils.errorMsg(USER_DOES_NOT_EXISTS));
		}
		return this.getUserById(user.getUid());
	}

	/**
	 * This Method will update User Details.
	 * 
	 * @param user
	 * @return User
	 */
	@Override
	public User updateUser(User user) {

		/** Update role **/
		if (Optional.ofNullable(user.getRoles()).isPresent()) {
			validateRole(user);
			if (user.getRoles() != null) {
				authService.updateUserrole(user.getUid(), getRoleBody(user, UPDATE));
			}
		}
		userService.update(user);
		User updatedUser = retrieveUser(user);// fresh fetch
		updateUserIntoCache(updatedUser);
		userSearchService.update(getUserSearchObject(updatedUser));
		return updatedUser;
	}

	/**
	 * This Method will update the User School Details based on input user.
	 * 
	 * @param user
	 * @return User
	 */
	@Override
	public User updateUserSchoolId(User user) {
		return userService.updateUserSchoolId(user);
	}

	/**
	 * This Method will update the User SubjectPreference Details based on input
	 * user.
	 * 
	 * @param userInput
	 * @return User
	 */
	@Override
	public User updateSubjectPreference(User userInput) {
		return userService.updateSubjectPreference(userInput);
	}

	/**
	 * This Method will fetch User Details based on email.
	 * 
	 * @param user
	 * @return User
	 */
	public User findByEmail(User user) {
		user = userService.findByEmail(user);
		populateUserWithExternalData(user);
		return user;
	}

	/**
	 * This Method will update the Multiple User "status" based on the input
	 * usersId and status
	 * 
	 * @param userIds
	 * @param status
	 * @return List<User>
	 */
	@Override
	public List<User> updateMultipleUsersStatusById(List<String> userIdList, String status) {
		checkStatusAvailable(status);
		List<User> resultUserList = new ArrayList<>();

		userIdList.forEach(userId -> {
			User tempUser = new User();
			tempUser.setUid(userId);
			if (null != status && Optional.ofNullable(status).isPresent()) {
				User statusUser = populateStatusField(status);
				tempUser.setStatus(statusUser.getStatus().toLowerCase());
				tempUser.setUserStatus(statusUser.getUserStatus());
			}
			tempUser.setCountryCode("AU");
			tempUser.setPearsonCreatedBy("PP");
			tempUser.setMessage(DEACTIVATE);
			User userResponse = updateUser(tempUser);
			User responseUser = new User();
			responseUser.setUid(userResponse.getUid());
			responseUser.setStatus(userResponse.getStatus());
			resultUserList.add(responseUser);
		});
		return resultUserList;
	}

	/**
	 * This Method will check the User Availability.
	 * 
	 * @param userValue
	 * @return
	 */
	@Override
	public Boolean checkAvailability(String userValue) {
		return userService.checkAvailability(userValue);
	}

	/**
	 * This Method will change the Password
	 * 
	 * @param user
	 * @return User
	 */
	public User changePassword(User user) {
		return userService.changePassword(user);
	}

	private User populateStatusField(String status) {
		User user = new User();
		String samStatus = "";
		String mongoStatus = "";
		if (null != status && Optional.ofNullable(status).isPresent()) {
			samStatus = status.toUpperCase();
		}
		if (samStatus.contains(StatusEnum.PENDING.toString())) {
			samStatus = StatusEnum.ACTIVE.toString();
			mongoStatus = StatusEnum.PENDING.toString();
		} else {
			samStatus = status;
		}
		user.setStatus(samStatus.toLowerCase());
		user.setUserStatus(mongoStatus.toLowerCase());
		return user;
	}

	private void checkStatusAvailable(String status) {
		String tempStatus = "";
		if (StringUtils.isBlank(status) || (status == null)) {
			throw new BridgeApiGraphqlException(ErrorType.DataFetchingException, "Please provide status code!");
		}
		if (Optional.ofNullable(status).isPresent()) {
			tempStatus = status.toUpperCase();
		}
		if ((tempStatus == null) || !(tempStatus.contains(StatusEnum.ACTIVE.toString())
				|| tempStatus.contains(StatusEnum.INACTIVE.toString())
				|| tempStatus.contains(StatusEnum.PENDING.toString()))) {
			throw new BridgeApiGraphqlException(ErrorType.DataFetchingException, "Insert valid status code!");
		}
	}

	@Override
	public Page<User> pageIt(Pageable pageable, Example<User> e) {
		Page<User> oldPages = pageUser(pageable, e);
		this.getAllUsersinList(oldPages.getContent(), this.getmasterKey());
		return oldPages;
	}

	private Page<User> pageUser(Pageable pageable, Example<User> e) {
		if (getLoggedInUser(true).getRoles().stream().map(String::toLowerCase).collect(Collectors.toList())
				.contains("teacher")) {
			return pageItForTeacher(pageable, e);
		}
		return userService.pageIt(pageable, e);
	}

	private Page<User> pageItForTeacher(Pageable pageable, Example<User> e) {

		Query query = new Query().with(pageable);

		query.addCriteria(Criteria.byExample(e));
		List<String> schools = getLoggedInUser(true).getSchool();

		if (CollectionUtils.isEmpty(schools)) {
			throw new GraphQLException("No Schools associated with this teacher!");
		}

		query.addCriteria(Criteria.where("school").in(schools));

		return PageableExecutionUtils.getPage(mongo.find(query, User.class), pageable,
				() -> mongo.count(query, User.class));
	}

	/**
	 * Takes User Data and makes two maps samMap and mongoMap. samMap contains
	 * only SAM required data. Output will be Map which contains two maps.
	 * 
	 * @param user
	 * @return
	 */
	private Map<String, Map<String, Object>> segregatedData(User user) {
		Map<String, Map<String, Object>> segregatedMap = new HashMap<>();

		Map<String, Object> dataMap = Utils.convertToMap(user);

		List<String> mongoFields = Utils.extractAnnotatedFieldNames(User.class, MongoSource.class);
		List<String> samFields = Utils.extractAnnotatedFieldNames(User.class, SamSource.class);

		Map<String, Object> samMap = new HashMap<>(dataMap);
		samMap.keySet().retainAll(samFields);
		Map<String, Object> mongoMap = dataMap;
		mongoMap.keySet().retainAll(mongoFields);

		segregatedMap.put(SAM_MAP, samMap);
		segregatedMap.put(MONGO_MAP, mongoMap);

		return segregatedMap;
	}

	private String getRoleBody(User user, String operation) {
		Map<String, Object> roleModel = new HashMap<>();
		roleModel.put(ROLES, user.getRoles());
		if (StringUtils.equals(operation, CREATE)) {
			roleModel.put(IDENTIFIER, user.getUserName());
			roleModel.put(USERNAME, user.getUserName());
		}
		// roleModel.put(USERNAME, this.getUserIdentifier(user));
		return JsonUtils.toJsonString(roleModel);
	}

	private void validateRole(User user) {
		Set<String> roles = user.getRoles();
		for (String role : roles) {
			if (Utils.isEmpty(role)) {
				throw new BridgeApiGraphqlException(ErrorType.ValidationError, ROLE_NAME_SHOULD_NOT_BE_EMPTY);
			}
			authService.validateRole(role);
		}
	}

	private Set<String> getProducts(User user) {
		Map<String, Object> responseMap = accessCodeSamClient.getAllSubscriptionDetailsofUser(user.getIdentifier());
		Set<String> prods = Utils.convert(
				Utils.transformJson(responseMap, GET_USER_SUBSCRIBED_ACCESS_CODES, specJsonFile),
				new TypeReference<Set<String>>() {
				});

		boolean schoolsAvailabity = Optional.ofNullable(user.getSchool()).isPresent();
		Set<String> lProds = null;
		if (schoolsAvailabity) {
			lProds = getProductsFromLicense(user.getSchool(), user.getIdentifier());
		}

		Set<String> teacherProducts = null;
		if (user.getRoles().contains("teacher") && schoolsAvailabity) {
			teacherProducts = getProductsOfFreeTeacherAccess(user.getSchool());
		}

		if (CollectionUtils.isEmpty(prods)) {
			prods = new HashSet<>();
		}

		if (!CollectionUtils.isEmpty(lProds)) {
			prods.addAll(lProds);
		}

		if (!CollectionUtils.isEmpty(teacherProducts)) {
			prods.addAll(teacherProducts);
		}

		return prods;
	}

	private Set<String> getProductsFromLicense(List<String> schoolIds, String username) {

		List<List<Licence>> licencesList = getLicense(schoolIds, username);

		return getProducts(licencesList);
	}

	private List<List<Licence>> getLicense(List<String> schoolIds, String username) {
		List<List<Licence>> lList = new ArrayList<>();
		List<Licence> licences = null;
		for (String id : schoolIds) {
			licences = licenceRespository.findByAttachedSchoolAndAttachedUser(id, username);
			if (!licences.isEmpty()) {
				lList.add(licences);
			}

		}
		if (lList.isEmpty()) {
			lList = null;
		}

		return lList;
	}

	private Set<String> getProducts(List<List<Licence>> licencesList) {
		Set<String> products = new HashSet<String>();
		if (licencesList != null) {

			licencesList.forEach(lList -> {
				lList.forEach(

						l -> {

							l.getProducts().forEach(p -> {
								products.add(p);
							});
						});
			});
		}
		return products;
	}

	private Set<String> getProductsOfFreeTeacherAccess(List<String> schoolIds) {
		Set<String> prodSet = new HashSet<>();
		Optional<List<LicenceHolder>> licenceHolderList = Optional.ofNullable(getLicenseHolderOfSchool(schoolIds));
		if (licenceHolderList.isPresent())
			licenceHolderList.get().forEach(holder -> {
				if (Optional.ofNullable(holder.getHolder()).isPresent()) {
					prodSet.add(holder.getHolder().getProduct().getProductId());
				}
				;
			});
		return prodSet;
	}

	private List<LicenceHolder> getLicenseHolderOfSchool(List<String> schoolIds) {
		List<LicenceHolder> licenceHolderList = null;

		licenceHolderList = licenceHolderRespository.findAllByHolderSchoolSchoolIdInAndHolderTeacherAccess(schoolIds,
				true);

		if (licenceHolderList.isEmpty()) {
			licenceHolderList = null;
		}

		return licenceHolderList;
	}

	private List<List<Licence>> getLicenseOfSchool(List<String> schoolIds) {
		List<List<Licence>> lList = new ArrayList<>();
		List<Licence> licences = null;
		for (String id : schoolIds) {
			licences = licenceRespository.findByAttachedSchool(id);
			if (!licences.isEmpty()) {
				lList.add(licences);
			}
		}
		if (lList.isEmpty()) {
			lList = null;
		}

		return lList;
	}

  private void populateMandatoryFields(User data) {
    data.setPearsonCreatedBy("pearson");
	data.setCountryCode(Utils.isEmpty(data.getCountryCode()) ? "AU" : data.getCountryCode());
    if (null != data.getStatus() && Optional.ofNullable(data.getStatus()).isPresent()) {
      User tempUser = populateStatusField(data.getStatus());
      data.setStatus(tempUser.getStatus());
      data.setUserStatus(tempUser.getUserStatus());
    } else {
      data.setStatus("active");
      data.setUserStatus("active");
    }
  }

	private List<User> getAllUsersinList(List<User> usersList, String masterKey) {
		DataLoaderOptions dlo = new DataLoaderOptions();
		dlo.setBatchingEnabled(true);
		dlo.setCachingEnabled(true);
		dlo.setMaxBatchSize(Utils.batchSizeCount(usersList.size()));

		BatchLoader<User, User> batchLoader = (List<User> users) -> {
			//logger.info("calling batchLoader load...");
			return CompletableFuture.supplyAsync(() -> {
				return users.parallelStream().map(user -> this.loadUser(user, masterKey)).collect(Collectors.toList());
			});
		};
		DataLoader<User, User> usersDataLoader = new DataLoader<>(batchLoader, dlo);
		CompletableFuture<List<User>> cf = usersDataLoader.loadMany(usersList);
		usersDataLoader.dispatchAndJoin();

		List<User> l = null;
		try {
			l = cf.get();
		} catch (InterruptedException e) {
			throw new GraphQLException(e.getMessage());
		} catch (ExecutionException e) {
			throw new GraphQLException(e.getMessage());
		}

		return l;
	}

	private User loadUser(User user, String masterKey) {
		try {
			user.replaceWith(this.fetchUserFromCacheOrLoad(user, masterKey));
		} catch (Exception e) {
			e.printStackTrace();
			//logger.error("Exception on user load...{}", user);
			user.setMessage(e.getMessage());
		}
		return user;
	}

	private String getUserIdentifier(User user) {
		String userIdentifier = "";
		if (Optional.ofNullable(user.getUserName()).isPresent()) {
			userIdentifier = user.getUserName();
		} else if (Optional.ofNullable(user.getUid()).isPresent()) {
			userIdentifier = user.getUid();
		}
		return userIdentifier;
	}

	@Override
	public BulkUploadJobSummary addBulkUploadUserJob(BulkUploadJobSummary bulkUploadJobSummary,
			BulkUploadJobDetails bulkUploadJobDetails) {
		bulkUploadJobSummary.setRequesterId(getLoggedInUser(true).getUid());
		return userService.addBulkUploadUserJob(bulkUploadJobSummary, bulkUploadJobDetails);
	}

	@Override
	public BulkUploadJobDetails getbulkUploadJobDetails(Integer jobId) {
		return userService.getbulkUploadJobDetails(jobId);
	}

	@Override
	public Page<BulkUploadJobSummary> pageItBulk(Pageable pageable, Example<BulkUploadJobSummary> e) {
		return userService.pageItBulk(pageable, e);
	}
	
	@Override
	public Page<UserSearch> search(Pageable pageable,String searchable) {
		return userSearchService.search(pageable,searchable);
	}
	
	private UserSearch getUserSearchObject(User user){
		UserSearch userSearch = new UserSearch();
		Utils.copyNonNullProperties(user, userSearch);
		userSearch.setUid(user.getUserName());
		return userSearch;
	}

  /* (non-Javadoc)
   * @see com.pearson.sam.bridgeapi.ibridgeservice.IUserBridgeService#pageIt(org.springframework.data.domain.Pageable, com.pearson.sam.bridgeapi.elasticsearch.model.UserSearch)
   */
  @Override
  public Page<UserSearch> pageIt(Pageable pageable, UserSearch searchable,StringMatcher sm) {
    return userSearchService.pageIt(pageable, searchable,sm);
  }

  @Override
  public Token login(String userName, String password, AuthContext authContext) {
    Token token = new Token();
     Map<String, Object> authenticate = authenticationService.authenticate(userName, password);
     //logger.info("authenticate {}", authenticate);
     User user = getUserByUserName(userName);
     userService.updateLastLogin(userName);
     
     //SessionUser sessionUser = new SessionUser(new SessionUser(), user);
     Set<String> roles = roleService.getRoles(user.getUid());
     Set<String> permissions = roleService.getPermissions(roles).parallelStream().filter(queriesMap::containsKey)
         .flatMap(key -> queriesMap.get(key).stream()).collect(Collectors.toSet());
     SessionUser sessionUser = new SessionUser(new SessionUser(user.getUid(),user.getEmail(),user.getRoles(),null,permissions), user);
     if(authenticate.get("ssoToken") !=null) {
       sessionUser.setSsoToken(authenticate.get("ssoToken").toString());
       token.setSsoToken(authenticate.get("ssoToken").toString());
     }
     
     if(authenticate.get("access_token") !=null) {
       sessionUser.setAccessToken(authenticate.get("access_token").toString());
       token.setAccessToken(authenticate.get("access_token").toString());
     }
     if(authenticate.get("refresh_token") !=null) {
       sessionUser.setRefreshToken(authenticate.get("refresh_token").toString());
     }
     
     Optional<HttpServletRequest> request = authContext.getRequest();
     if (request.isPresent()) {
       HttpSession session = request.get().getSession(true);
       session.invalidate();
       request.get().getSession(true).removeAttribute("userObj");
       request.get().getSession(true).setAttribute("userObj", sessionUser);
       
       session = request.get().getSession();
      
        }
     
     return token;
  }

  @Override
  public String logout(AuthContext authContext) {
    String ssoTtoken = getLoggedInUser(false).getSsoToken();
     //logger.info("ssoTtoken {}", ssoTtoken);
     String accessToken = getLoggedInUser(false).getAccessToken();
     //logger.info("accessToken {}", accessToken);
     String refreshToken = getLoggedInUser(false).getRefreshToken();
     //logger.info("refreshToken {}", refreshToken);
     
     Map<String, Object> logout = authenticationService.logout(ssoTtoken, accessToken);
     //logger.info("logout {}", logout);
     
     Optional<HttpServletRequest> request = authContext.getRequest(); 
     HttpSession session = request.get().getSession(true);
     session.invalidate();
     return (String) logout.get("message");
  }
  
}
