package com.pearson.sam.bridgeapi.serviceimpl;

import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.QUERIES_INFO;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.pearson.sam.bridgeapi.iservice.IAuthService;
import com.pearson.sam.bridgeapi.samclient.IAuthSamClient;
import com.pearson.sam.bridgeapi.util.Utils;

@Service
public class AuthServiceImpl implements IAuthService {

	@Autowired
	private IAuthSamClient authSamClient;

	@Value("${SPEC_FILE}")
	String specJsonFile;

	@Autowired
	@Qualifier(QUERIES_INFO)
	Map<String, Set<String>> quriesMap;

	/**
	 * getRoles.
	 * 
	 * @return
	 */
	@Override
	public Set<String> getRoles(String userName) {
		Map<String, String> params = new HashMap<>();
		params.put("userName", userName);
		Map<String, Object> rolesMap = authSamClient.getUserrole(userName);
		Set<String> roles = null;
		if (rolesMap != null) {
			roles = Utils.convert(rolesMap.get("roles"), new TypeReference<Set<String>>() {
			});
		}
		return roles;
	}

	/**
	 * getPermissions.
	 * 
	 * @return
	 */
	@Override
	public Set<String> getPermissions(String roleName) {
		Map<String, Object> rolesMap = authSamClient.getRole(roleName);
		return Utils.convert(Utils.transformJson(rolesMap, "Permissions", specJsonFile),
				new TypeReference<Set<String>>() {
				});
	}

	/**
	 * getPermissions.
	 * 
	 * @return
	 */
	@Override
	public Set<String> getPermissions(Set<String> roles) {
		Map<String, Object> rolesMap = new HashMap<>();
		for (String roleName : roles) {
			Map<String, Object> roleDetail = authSamClient.getRole(roleName);
			rolesMap.putAll(roleDetail);
		}

		return Utils.convert(Utils.transformJson(rolesMap, "Permissions", specJsonFile),
				new TypeReference<Set<String>>() {
				});
	}

	@Override
	public void validateRole(String role) {

		authSamClient.getRole(role);
	}

	@Override
	public Map<String, Object> updateUserrole(String userName, String body) {
		return authSamClient.updateUserrole(userName, body);
	}

	@Override
	public Map<String, Object> createUserrole(String userName, String body) {
		return authSamClient.createUserrole(userName, body);
	}

}
