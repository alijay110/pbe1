package com.pearson.sam.bridgeapi.samclient;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pearson.sam.bridgeapi.samservices.IdentityServices;

/**
 * 
 * @author VKu99Ma
 *
 */
@Service
public class UserSamClientImpl implements IUserSamClient {

	@Autowired
	IdentityServices identityServices;

	@Override
	public Map<String, Object> createUser(String body) {
		return identityServices.createUser(body);
	}

	@Override
	public Map<String, Object> updateUser(String userName, String body) {
		return identityServices.updateUser(userName, body);
	}

	@Override
	public Boolean checkUserIdentityAvailability(String userDetails) {
		return identityServices.checkUserIdentityAvailability(userDetails);
	}

	@Override
	public Map<String, Object> getUser(String userName) {
		return identityServices.getUser(userName);
	}

	@Override
	public Map<String, Object> changePassword(String userName, String body) {
		return identityServices.changePassword(userName, body);
	}

}
