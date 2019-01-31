package com.pearson.sam.bridgeapi.iservice;

import java.util.Map;
import java.util.Set;

public interface IAuthService {

	public Set<String> getRoles(String userName);

	public Set<String> getPermissions(String roleName);

	public Set<String> getPermissions(Set<String> roles);

	public void validateRole(String role);

	public Map<String, Object> updateUserrole(String userName, String body);

	public Map<String, Object> createUserrole(String userName, String body);

}
