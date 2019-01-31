package com.pearson.sam.bridgeapi.samclient;

import java.util.Map;

public interface IAuthSamClient {

	public Map<String, Object> getUserrole(String userName);
	public Map<String, Object> getRole(String roleName);
	public Map<String, Object> updateUserrole(String userName,String body );
	public Map<String, Object> createUserrole(String userName,String body );
	
}
