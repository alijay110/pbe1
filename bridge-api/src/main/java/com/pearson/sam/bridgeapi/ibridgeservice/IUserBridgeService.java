package com.pearson.sam.bridgeapi.ibridgeservice;

import com.pearson.sam.bridgeapi.config.AuthContext;
import com.pearson.sam.bridgeapi.elasticsearch.model.UserSearch;
import com.pearson.sam.bridgeapi.model.BulkUploadJobDetails;
import com.pearson.sam.bridgeapi.model.BulkUploadJobSummary;
import com.pearson.sam.bridgeapi.model.Token;
import com.pearson.sam.bridgeapi.model.User;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 
 * @author VKu99Ma
 *
 */
public interface IUserBridgeService {

	/**
	 * This Method will add the User.
	 * 
	 * @param user
	 * @return User
	 */
	public User createUser(User user);

	/**
	 * This Method will fetch User Details.
	 * 
	 * @param user
	 * @return User
	 */
	public User getUserById(String uid);

	/**
	 * This Method will update User Details.
	 * 
	 * @param uid
	 * @return User
	 */
	public User updateUser(User user);

	/**
	 * This Method will update the User School Details based on input user.
	 * 
	 * @param user
	 * @return User
	 */
	public User updateUserSchoolId(User user);

	/**
	 * This Method will update the User SubjectPreference Details based on input
	 * user.
	 * 
	 * @param userInput
	 * @return User
	 */
	public User updateSubjectPreference(User userInput);

	/**
	 * This Method will update the Multiple User "status" based on the input usersId
	 * and status
	 * 
	 * @param userIdList
	 * @param status
	 * @return List<User>
	 */
	public List<User> updateMultipleUsersStatusById(List<String> userIdList, String status);

	/**
	 * This Method will check the User Availability.
	 * 
	 * @param userValue
	 * @return
	 */
	public Boolean checkAvailability(String userValue);

	/**
	 * This Method will give the paginated Users as response.
	 * 
	 * @param pageable
	 * @param e
	 * @return Page<User>
	 */
	public Page<User> pageIt(Pageable pageable, Example<User> e);

	/**
	 * This Method will change the Password
	 * 
	 * @param user
	 * @return User
	 */
	public User changePassword(User user);

	/**
	 * This Method will fetch User Details based in userName.
	 * 
	 * @param user
	 * @return User
	 */
	public User getUserByUserName(String user);

	/**
	 * addBulkUploadUserJob.
	 * 
	 * @param bulkUploadJobSummary
	 * @param bulkUploadJobDetails
	 * @return
	 */
	public BulkUploadJobSummary addBulkUploadUserJob(BulkUploadJobSummary bulkUploadJobSummary,
			BulkUploadJobDetails bulkUploadJobDetails);

	public BulkUploadJobDetails getbulkUploadJobDetails(Integer jobId);

	/**
	 * This Method will give the paginated BulkUploadJobSummary as response.
	 * 
	 * @param pageable
	 * @param e
	 * @return Page<User>
	 */
	public Page<BulkUploadJobSummary> pageItBulk(Pageable pageable, Example<BulkUploadJobSummary> e);

	Page<UserSearch> search(Pageable pageable, String searchable);
	
	/**
	 * This Method will fetch User Details based on email.
	 * 
	 * @param user
	 * @return User
	 */
	public User findByEmail(User user);

  /**
   * pageIt.
   * @param pageable
   * @param query
   * @return  
   */
  public Page<UserSearch> pageIt(Pageable pageable, UserSearch query,StringMatcher sm);

	public Token login(String userName, String password, AuthContext authContext);

	public String logout(AuthContext authContext);

}
