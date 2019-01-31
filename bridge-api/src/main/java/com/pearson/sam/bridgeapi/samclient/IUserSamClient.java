package com.pearson.sam.bridgeapi.samclient;

import java.util.Map;

/**
 * 
 * @author VKu99Ma
 *
 */
public interface IUserSamClient {
	/**
	 * 
	 * @param body
	 * @return
	 */
	Map<String, Object> createUser(String body);

	/**
	 * 
	 * @param userName
	 * @param body
	 * @return
	 */
	Map<String, Object> updateUser(String userName, String body);

	/**
	 * 
	 * @param userDetails
	 * @return
	 */
	Boolean checkUserIdentityAvailability(String userDetails);

	/**
	 * 
	 * @param userName
	 * @return
	 */
	Map<String, Object> getUser(String userName);
	
	/**
	 * 
	 * @param userName
	 * @param body
	 * @return
	 */
	public Map<String, Object> changePassword(String userName, String body);
}
