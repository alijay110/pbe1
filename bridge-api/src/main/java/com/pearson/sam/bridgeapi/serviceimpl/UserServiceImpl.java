package com.pearson.sam.bridgeapi.serviceimpl;

import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.MONGO_MAP;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.SAM_MAP;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.DATA_NOT_FOUND;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.JOB_AREADY_EXISTS;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.JOB_DOES_NOT_EXISTS;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.THIS_EMAIL_ALREADY_EXISTS;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.UID_ALREADY_EXISTS;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.UID_SHOULD_PRESENT;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.USERNAME_ALREADY_EXISTS;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.USER_DOES_NOT_EXISTS;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.DATA_NOT_FOUND;

import com.bazaarvoice.jolt.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.pearson.sam.bridgeapi.annotations.MongoSource;
import com.pearson.sam.bridgeapi.annotations.SamSource;
import com.pearson.sam.bridgeapi.elasticsearch.model.UserSearch;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGeneralException;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGraphqlException;
import com.pearson.sam.bridgeapi.iservice.IUserService;
import com.pearson.sam.bridgeapi.model.BulkUploadJobDetails;
import com.pearson.sam.bridgeapi.model.BulkUploadJobSummary;
import com.pearson.sam.bridgeapi.model.KafkaUser;
import com.pearson.sam.bridgeapi.model.User;
import com.pearson.sam.bridgeapi.repository.BulkUploadJobDetailsRepository;
import com.pearson.sam.bridgeapi.repository.BulkUploadJobSummaryRepository;
import com.pearson.sam.bridgeapi.repository.UserElasticSearchRepository;
import com.pearson.sam.bridgeapi.repository.UserRepository;
import com.pearson.sam.bridgeapi.samclient.IUserSamClient;
import com.pearson.sam.bridgeapi.util.PropertyHolder;
import com.pearson.sam.bridgeapi.util.Utils;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import graphql.ErrorType;
import io.jsonwebtoken.lang.Collections;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * 
 * @author VKu99Ma
 *
 */
@Service
public class UserServiceImpl implements IUserService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	private static final String CREATE_USER = "createUser";
	private static final String PASS_WORD_STRING = "userPassword";
	private static final String UID = "uid";

	private static final String UPDATE = "update";
	private static final String USER_QUERY = "userQuery";
	private static final String DEACTIVATE = "deactivate";
	private static final String STATUS = "status";
	public static final String PASS_WORD = "userPassword";
	private static final String USERNAME = "userName";
	private static final String IDENTIFIER = "identifier";
	private static final String USER_PASSWORD = "user.password";
  private static final String TOPIC = "PP2Topic";

	@Value("${SPEC_FILE}")
	private String specJsonFile;

	@Value("${GRAPHQL_SDL_FILE}")
	private String graphqlFile;

	@Autowired
	private IUserSamClient userSamClient;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BulkUploadJobDetailsRepository bulkUploadJobDetailsRepository;

	@Autowired
	private BulkUploadJobSummaryRepository bulkUploadJobSummaryRepository;
  
	@Autowired
	private KafkaTemplate<String, BulkUploadJobSummary> kafkaTemplate;
	

	private Map<String, Object> createUserInSam(User user, Map<String, Object> samMap) {
		validateUserName(user);
		validateUid(user);

		if ((user.getUserName().length() < 8)) {
			user.setUserName(user.getUserName() + "PP");
		}

		Map<String, Object> transformedMap = Utils.transformJsonAsMap(samMap, CREATE_USER, specJsonFile);

		addPassword(transformedMap, samMap);

		//logger.info("transformedMap :{}", transformedMap);

		Map<String, Object> responseMap = userSamClient.createUser(JsonUtils.toJsonString(transformedMap));

		return responseMap;
	}

	/**
	 * This Method will update User Details.
	 * 
	 * @param user
	 * @return User
	 */
	private Map<String, Object> updateUserInSam(User user, Map<String, Object> samMap) {
		Map<String, Object> transformedMap = Utils.transformJsonAsMap(samMap, CREATE_USER, specJsonFile);
		Map<String, Object> responseMap = null;
		Map<String, Object> userMap = null;
		User userDb = null;
		//logger.info("transformedMap :{}", transformedMap);

		if (!Optional.ofNullable(transformedMap).isPresent()) {
			return null;
		}

		if (Optional.ofNullable(user.getUid()).isPresent()) {
			userDb = userRepository.findByUid(user.getUid());
		}
		if (!Optional.ofNullable(userDb).isPresent()) {
			throw new BridgeApiGraphqlException(ErrorType.ValidationError, USER_DOES_NOT_EXISTS);
		}

		if (!samMap.isEmpty()) {

			responseMap = userSamClient.updateUser(userDb.getIdentifier(), JsonUtils.toJsonString(transformedMap));

			userMap = Utils.transformJsonAsMap(responseMap, USER_QUERY, specJsonFile);
		}

		user = Utils.convert(userMap, new TypeReference<User>() {
		});
		return userMap;
	}

	@Override
	public void update(User user) {
		Map<String, Object> userMap = null;
		Map<String, Map<String, Object>> segregatedData = segregatedData(user);
		Map<String, Object> samMap = segregatedData.get(SAM_MAP);
		Map<String, Object> mongoMap = segregatedData.get(MONGO_MAP);
		//logger.info("SamMap : {}", samMap);
		//logger.info("MongoMap : {}", mongoMap);
		/** SAM Call **/
		if (!samMap.isEmpty()) {
			userMap = this.updateUserInSam(user, samMap);
		}
		updateUserMap(user, userMap);
		this.saveIntoMongo(userMap, mongoMap, false);
	}

	private Map<String, Object> updateUserMap(User user, Map<String, Object> userMap) {
		if (!Optional.ofNullable(userMap).isPresent()) {
			userMap = new HashMap<>();
			userMap.put(UID, user.getUid());
			userMap.put(USERNAME, user.getUserName());
		}
		return userMap;
	}

	/**
	 * This Method will check the User Availability.
	 * 
	 * @param userValue
	 * @return
	 */
	@Override
	public Boolean checkAvailability(String userValue) {
		return userSamClient.checkUserIdentityAvailability(userValue);
	}

	@Override
	public User getUser(User user) {
		Object obj = retrieveUser(user);
		return Utils.convert(obj, new TypeReference<User>() {
		});
	}

	/**
	 * This Method will fetch User Details based in userName.
	 * 
	 * @param user
	 * @return User
	 */
	@Override
	public User getUserName(String userName) {

		return userRepository.findByIdentifier(userName);

	}

	/**
	 * This method will count school by schoolId.
	 * 
	 * @param schoolId
	 * @return
	 */
	@Override
	public Long countBySchool(String schoolId) {
		// TODO Auto-generated method stub
		return userRepository.countBySchool(schoolId);
	}

	/**
	 * This Method will update the User School Details based on input user.
	 * 
	 * @param user
	 * @return User
	 */
	@Override
	public User updateUserSchoolId(User userInput) {
		if (userInput == null || Utils.isEmpty(userInput.getUid())) {
			throw new BridgeApiGraphqlException(ErrorType.ValidationError, UID_SHOULD_PRESENT);
		}
		User user = userRepository.findByUid(userInput.getUid());
		if (user == null) {
			throw new BridgeApiGraphqlException(ErrorType.ValidationError, USER_DOES_NOT_EXISTS);
		}
		if (Optional.ofNullable(user).isPresent()) {
			user.setSchool(userInput.getSchool());
			user = userRepository.save(user);
		}
		return user;
	}

	/**
	 * This Method will update the User SubjectPreference Details based on input
	 * user.
	 * 
	 * @param userInput
	 * @return User
	 */
	public User updateSubjectPreference(User userInput) {
		User user = userRepository.findByUid(userInput.getUid());
		if (Optional.ofNullable(user).isPresent()) {
			user.setSubjectPreference(userInput.getSubjectPreference());
			user = userRepository.save(user);
		}
		return user;
	}

	/**
	 * This Method will give the paginated Users as response.
	 * 
	 * @param pageable
	 * @param e
	 * @return Page<User>
	 */
	@Override
	public Page<User> pageIt(Pageable pageable, Example<User> e) {
		return userRepository.findAll(e, pageable);
	}

	@Override
	public Long getTotalCount() {
		return userRepository.count();
	}

	/**
	 * This Method will fetch User Details based on email.
	 * 
	 * @param user
	 * @return User
	 */
	@Override
	public User findByEmail(User userInput) {
		User user = userRepository.findByEmail(userInput.getEmail());
		if (!Optional.ofNullable(user).isPresent()) {
			throw new BridgeApiGraphqlException(ErrorType.DataFetchingException, DATA_NOT_FOUND);
		}
		User tempUser = new User();
		tempUser.setUserName(user.getIdentifier());
		return getUser(tempUser);
	}

	/**
	 * This Method will change the Password
	 * 
	 * @param user
	 * @return User
	 */
	public User changePassword(User user) {
		Map<String, Object> responseMap = null;
		Map<String, String> pwdMap = new HashMap<>();
		pwdMap.put(PASS_WORD, user.getPasswd());
		String body = JsonUtils.toJsonString(pwdMap);
		try {
			responseMap = userSamClient.changePassword(user.getUid(), body);
			user = Utils.convert(responseMap.get("message"), new TypeReference<User>() {
			});
		} catch (Exception e) {
			throw new BridgeApiGraphqlException(ErrorType.ValidationError, e.getMessage());
		}
		return user;
	}


	@Override
	public void validateUserName(User user) {
		Boolean checkAvailability = checkAvailability(user.getUserName());
		if (checkAvailability.equals(false)) {
			throw new BridgeApiGraphqlException(ErrorType.ValidationError, USERNAME_ALREADY_EXISTS);
		}
	}

	@Override
	public void signUpUser(User user) {
		Map<String, Map<String, Object>> segregatedData = segregatedData(user);
		Map<String, Object> samMap = segregatedData.get(SAM_MAP);
		//logger.info("SamMap : {}", samMap);
		Map<String, Object> responseMap = this.createUserInSam(user, samMap);
		Map<String, Object> userMap = Utils.transformJsonAsMap(responseMap, USER_QUERY, specJsonFile);
		Map<String, Object> mongoMap = segregatedData.get(MONGO_MAP);
		//logger.info("MongoMap : {}", mongoMap);
		this.saveIntoMongo(userMap, mongoMap, true);
	}

	private void validateUid(User user) {
		if (userRepository.existsByUid(user.getUid())) {
			throw new BridgeApiGraphqlException(ErrorType.ValidationError, UID_ALREADY_EXISTS);
		}
	}

	private void saveIntoMongo(Map<String, Object> userMap, Map<String, Object> mongoMap, boolean isCreate) {
		User givenUser = Utils.convert(mongoMap, new TypeReference<User>() {
		});

		User dbUser = null;
		if (isCreate) {
			dbUser = givenUser;
			if (!Optional.ofNullable(mongoMap.get(UID)).isPresent()) {
				dbUser.setUid((String) userMap.get(USERNAME));
			}
			dbUser.setIdentifier((String) userMap.get(USERNAME));
		} else {
			dbUser = userRepository.findByUid(mongoMap.get(UID).toString());
			Utils.copyNonNullProperties(givenUser, dbUser);
		}
		userRepository.save(dbUser);
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

	private void addPassword(Map<String, Object> transformedMap, Map<String, Object> samMap) {
		if (!(samMap.containsKey(PASS_WORD_STRING)
				|| StringUtils.isNotBlank((String) transformedMap.get(PASS_WORD_STRING)))) {
			String password = PropertyHolder.getStringProperty(USER_PASSWORD);
			transformedMap.put(PASS_WORD_STRING, password);
		}
	}

	private void checkUserStatusAvailability(Map<String, Object> userMap, User userDb) {
		if (null != userDb && Optional.ofNullable(userMap.get(STATUS)).isPresent()) {
			if (!StringUtils.isEmpty(userDb.getUserStatus())) {
				userMap.put(STATUS, userDb.getUserStatus());
			} else {
				userMap.put(STATUS, userMap.get(STATUS));
			}
		}
	}

	private User retrieveUser(Object obj) {
		User user = Utils.convert(obj, new TypeReference<User>() {
		});

		User userResponse = null;
		User userDb = null;
		try {
			if (Optional.ofNullable(user.getUid()).isPresent()) {
				userDb = userRepository.findByUid(user.getUid());
			} else if (Optional.ofNullable(user.getUserName()).isPresent()) {
				userDb = userRepository.findByIdentifier(user.getUserName());
			}
			if (!Optional.ofNullable(userDb).isPresent()) {
				throw new BridgeApiGraphqlException(ErrorType.ValidationError, USER_DOES_NOT_EXISTS);
			}

			Map<String, Object> responseMap = userSamClient.getUser(userDb.getIdentifier());
			Map<String, Object> userMap = Utils.transformJsonAsMap(responseMap, USER_QUERY, specJsonFile);

			Map<String, Object> userDbMap = Utils.convertToMap(userDb);
			checkUserStatusAvailability(userMap, userDb);
			//logger.info("fetchOne userDbMap:{}", Collections.isEmpty(userDbMap));
			//logger.info("fetchOne userDbMap:{}", userDbMap);
			if (!Collections.isEmpty(userDbMap))
				userMap.putAll(userDbMap);
			userResponse = Utils.convert(userMap, new TypeReference<User>() {
			});
		} catch (BridgeApiGeneralException e) {
			throw new BridgeApiGraphqlException(ErrorType.ValidationError, e.getMessage());
		}
		return userResponse;
	}

	@Override
	public BulkUploadJobSummary addBulkUploadUserJob(BulkUploadJobSummary bulkUploadJobSummary,
			BulkUploadJobDetails bulkUploadJobDetails) {
		int generateRandomNumber = Utils.generateRandomNumber(1, 999);
		BulkUploadJobSummary findByJobId = bulkUploadJobSummaryRepository.findByJobId(generateRandomNumber);
		if (Optional.ofNullable(findByJobId).isPresent()) {
			throw new BridgeApiGraphqlException(ErrorType.ValidationError, JOB_AREADY_EXISTS);
		}
		bulkUploadJobSummary.setJobId(generateRandomNumber);
		bulkUploadJobSummary.setDateCreated(Utils.generateEpochDate());
		if (bulkUploadJobSummary.getSelectedUserRole().equalsIgnoreCase("students")) {
			bulkUploadJobSummary.setUploadType("Student");
		} else if (bulkUploadJobSummary.getSelectedUserRole().equalsIgnoreCase("school-staff")) {
			bulkUploadJobSummary.setUploadType("School Staff");
		} else if (bulkUploadJobSummary.getSelectedUserRole().equalsIgnoreCase("channel-staff")) {
			bulkUploadJobSummary.setUploadType("Channel Staff");
		} else if (bulkUploadJobSummary.getSelectedUserRole().equalsIgnoreCase("pearson-staff")) {
			bulkUploadJobSummary.setUploadType("Pearson Staff");
		}
		bulkUploadJobDetails.setJobId(generateRandomNumber);
		BulkUploadJobSummary dbBulkUploadJobSummary = bulkUploadJobSummaryRepository.save(bulkUploadJobSummary);
		bulkUploadJobDetailsRepository.save(bulkUploadJobDetails);
		return dbBulkUploadJobSummary;
	}

	@Override
	public BulkUploadJobDetails getbulkUploadJobDetails(Integer jobId) {
		BulkUploadJobDetails findByJobId = bulkUploadJobDetailsRepository.findByJobId(jobId);

		if (!Optional.ofNullable(findByJobId).isPresent()) {
			throw new BridgeApiGraphqlException(ErrorType.ValidationError, JOB_DOES_NOT_EXISTS);
		}
		return findByJobId;
	}

	
	public Page<BulkUploadJobSummary> pageItBulk(Pageable pageable, Example<BulkUploadJobSummary> e) {
		return bulkUploadJobSummaryRepository.findAll(e, pageable);
	}
	
	@Override
	public boolean updateLastLogin(String userId) {
		Date dateTime = new Date();
	    User userResult = userRepository.findByIdentifier(userId);
	    String now = String.valueOf(dateTime.getTime());
	    userResult.setLastLogin(now);
	    userRepository.save(userResult);
	    //logger.info("User {} Last Login {}", userId, now);
	    return false;
	  }

}