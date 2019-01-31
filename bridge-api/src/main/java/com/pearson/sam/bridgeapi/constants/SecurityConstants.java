package com.pearson.sam.bridgeapi.constants;

public class SecurityConstants {
  
  private SecurityConstants() {}
  
  public static final long EXPIRATION_TIME = 864_000_000; // 10 days
  public static final String TOKEN_PREFIX = "Bearer ";
  public static final String HEADER_STRING = "Authorization";
  public static final String INVALID_TOKEN = "Invalid Access Token.User Not logged In";
  public static final String ACCESS_TOKEN = "access_token";
  public static final String USER_ROLES = "user_roles";
  public static final String SESSION_EMAIL = "sessionEmail";
  public static final String SESSION_USERID = "sessionUserId";
}