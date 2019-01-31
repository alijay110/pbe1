package com.pearson.sam.bridgeapi.samclient;

import java.util.Map;

public interface IAuthentication {

	public Map<String, Object> authenticate(String userName,String password);
	
	public Map<String, Object> logout(String sessionId,String token);
	
}
