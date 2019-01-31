package com.pearson.sam.bridgeapi.model;

public class Token extends BaseModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ssoToken;
	private String accessToken;
	
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

}
