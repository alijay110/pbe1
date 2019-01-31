package com.pearson.sam.bridgeapi.samservices;

import static com.pearson.sam.bridgeapi.samservices.constants.SamEndPointConstants.AUTHENTICATION_SERVICES;
import static com.pearson.sam.bridgeapi.samservices.constants.SamEndPointConstants.AUTHENTICATE;
import static com.pearson.sam.bridgeapi.samservices.constants.SamEndPointConstants.LOGOUT;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pearson.sam.bridgeapi.resthelper.RestClient;
import com.pearson.sam.bridgeapi.resthelper.RestRequestBuilder;

/**
 * AuthorizationServices.
 * 
 * @author VNathMa
 *
 */
@Service
public class AuthenticationServices {

	@Autowired
	RestClient restClient;

	private static final String PASSWORD = "password";
	private static final String USER_NAME = "userName";
	private static final String SESSIONID = "sessionId";
	private static final String TOKEN = "token";

	
	public Map<String, Object> authenticate(String userName, String password) {
		Map<String, String> headersMap = new HashMap<>();
		headersMap.put(USER_NAME, userName);
		headersMap.put(PASSWORD, password);
		return restClient.postNValidate(new RestRequestBuilder(AUTHENTICATION_SERVICES, AUTHENTICATE)
				.withAdditionalHeaders(headersMap).build());
	}
	
	public Map<String, Object> logout(String sessionId, String token) {
		Map<String, String> headersMap = new HashMap<>();
		headersMap.put(SESSIONID, sessionId);
		headersMap.put(TOKEN, token);
		return restClient.postNValidate(new RestRequestBuilder(AUTHENTICATION_SERVICES, LOGOUT)
				.withAdditionalHeaders(headersMap).build());
	}

}
