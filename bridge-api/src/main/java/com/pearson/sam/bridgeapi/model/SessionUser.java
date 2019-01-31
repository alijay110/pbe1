package com.pearson.sam.bridgeapi.model;

import java.io.Serializable;
import java.util.Set;

/**
 * SessionUser class.
 * 
 * @author VKuma09
 *
 */
public class SessionUser extends User implements Serializable {

	private static final long serialVersionUID = 1L;

	private String userId;
	private String token;
	private Set<String> permissions;
	private String ssoToken;
	private String accessToken;
	private String refreshToken;

	public SessionUser() {
		super();
	}

	/**
	 * public constructor.
	 * 
	 * @param userId user
	 * @param email  email
	 * @param roles  roles
	 * @param token  token
	 */
	public SessionUser(String userId, String email, Set<String> roles, String token, Set<String> permissions) {
		super();
		this.userId = userId;
		this.token = token;
		this.permissions = permissions;
		this.setEmail(email);
		this.setRoles(roles);
	}

	/**
	 * public constructor.
	 * 
	 * @param userId user
	 * @param token  token
	 */
	public SessionUser(String userId, String email, Set<String> roles, String token, Set<String> permissions,
			User user) {
		super(user);
		this.userId = userId;
		this.token = token;
		this.permissions = permissions;
		this.setEmail(email);
		this.setRoles(roles);
	}

	/**
	 * public constructor.
	 */
	public SessionUser(SessionUser sessionUser, User user) {
		super(user);
		this.userId = sessionUser.getUserId();
		this.token = sessionUser.getToken();
		this.setEmail(sessionUser.getEmail());
		this.setRoles(sessionUser.getRoles());
		this.setPermissions(sessionUser.getPermissions());
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Set<String> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<String> permissions) {
		this.permissions = permissions;
	}

	public String getSsoToken() {
		return ssoToken;
	}

	public void setSsoToken(String ssoToken) {
		this.ssoToken = ssoToken;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	@Override
	public String toString() {
		return "SessionUser [userId=" + userId + ", email=" + super.getEmail() + ", roles=" + super.getRoles()
				+ ", token=" + token + "]";
	}

}
