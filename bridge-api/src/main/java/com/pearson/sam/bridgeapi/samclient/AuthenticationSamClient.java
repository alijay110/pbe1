package com.pearson.sam.bridgeapi.samclient;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pearson.sam.bridgeapi.samservices.AuthenticationServices;

@Service
public class AuthenticationSamClient implements IAuthentication{

	@Autowired
	private AuthenticationServices authenticationServices;
	
	@Override
	public Map<String, Object> authenticate(String userName, String password) {
		return authenticationServices.authenticate(userName, password);
	}

	@Override
	public Map<String, Object> logout(String sessionId, String token) {
		return authenticationServices.logout(sessionId, token);
	}

}
