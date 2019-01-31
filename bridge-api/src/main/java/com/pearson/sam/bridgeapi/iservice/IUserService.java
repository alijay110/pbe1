package com.pearson.sam.bridgeapi.iservice;

import com.pearson.sam.bridgeapi.model.BulkUploadJobDetails;
import com.pearson.sam.bridgeapi.model.BulkUploadJobSummary;
import com.pearson.sam.bridgeapi.model.User;
import com.pearson.sam.bridgeapi.repository.UserRepository;

import java.util.Map;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 
 * @author VKu99Ma
 *
 */
public interface IUserService {

	/**
	 * This method will validate the UserName
	 * 
	 * @param user
	 */
	public void validateUserName(User user);

	/**
	 * This Method will fetch User Details.
	 * 
	 * @param user
	 * @return User
	 */
	public User getUser(User user);

	/**
	 * This method will count school by schoolId.
	 * 
	 * @param schoolId
	 * @return
	 */
	Long countBySchool(String schoolId);

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
	public User getUserName(String user);

	/**
	 * addBulkUploadUserJob.
	 * 
	 * @param bulkUploadJobSummary
	 * @param bulkUploadJobDetails
	 * @return
	 */
	public BulkUploadJobSummary addBulkUploadUserJob(BulkUploadJobSummary bulkUploadJobSummary,
			BulkUploadJobDetails bulkUploadJobDetails);

	/**
	 * signUpUser.
	 * 
	 * @param user
	 */
	public void signUpUser(User user);

	/**
	 * update.
	 * 
	 * @param user
	 * @return
	 */
	public void update(User user);

	/**
	 * getTotalCount.
	 * 
	 * @return
	 */
	Long getTotalCount();

	public BulkUploadJobDetails getbulkUploadJobDetails(Integer jobId);

	public Page<BulkUploadJobSummary> pageItBulk(Pageable pageable, Example<BulkUploadJobSummary> e);

	/**
	 * findByEmail.
	 * 
	 * @param userInput
	 * @param sessionId
	 * @return
	 */
	User findByEmail(User userInput);
	
	public boolean updateLastLogin(String userId);
}
