package com.pearson.sam.bridgeapi.samclient;

import com.pearson.sam.bridgeapi.samservices.AuthorizationServices;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthSamClientImpl implements IAuthSamClient {
	
	@Autowired
	private AuthorizationServices authorizationServices;

	@Override
	public Map<String, Object> getUserrole(String userName) {
		// TODO Auto-generated method stub
		return authorizationServices.getUserrole(userName);
	}

	@Override
	public Map<String, Object> getRole(String role) {
		// TODO Auto-generated method stub
		return authorizationServices.getRole(role);
	}

	@Override
	public Map<String, Object> updateUserrole(String userName, String body) {
		return authorizationServices.updateUserrole(userName, body );
	}

	@Override
	public Map<String, Object> createUserrole(String userName, String body) {
		// TODO Auto-generated method stub
		return authorizationServices.createUserrole(body);
	}
	

}
