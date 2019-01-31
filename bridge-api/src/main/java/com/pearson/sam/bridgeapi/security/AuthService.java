package com.pearson.sam.bridgeapi.security;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pearson.sam.bridgeapi.model.User;
import com.pearson.sam.bridgeapi.repository.UserRepository;
import com.pearson.sam.bridgeapi.samclient.IdamClient;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  @Autowired
  IdamClient idamClient;
  
  private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
  private final GsonBuilder gsonBuilder = new GsonBuilder();
  private final Gson gson = gsonBuilder.setPrettyPrinting().disableHtmlEscaping().create();

  private static AuthService authService;

  /**
   * returns singleton.
   * 
   * @return
   */
  public static AuthService getInstance() {
    if (authService == null) {
      authService = new AuthService();
    }
    return authService;
  }

  private AuthService() {

  }


  /**
   * validates token.
   * 
   * @param url
   *          url
   * @return
   */
  public ResponseEntity<Map<String,Object>> validateToken(String token) {
    return idamClient.validateToken(token);
  }

  /**
   * updateLastLogin.
   * 
   * @return
   */
  public boolean updateLastLogin(String userId, UserRepository userRepo) {
    Date dateTime = new Date();
    User userResult = userRepo.findByIdentifier(userId);
    String now = String.valueOf(dateTime.getTime());
    userResult.setLastLogin(now);
    userRepo.save(userResult);
    //logger.info("User {} Last Login {}", userId, now);
    return false;

  }

}