package com.pearson.sam.bridgeapi.security;

import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.QUERIES_INFO;
import static com.pearson.sam.bridgeapi.constants.SecurityConstants.HEADER_STRING;
import static com.pearson.sam.bridgeapi.constants.SecurityConstants.INVALID_TOKEN;
import static com.pearson.sam.bridgeapi.constants.SecurityConstants.TOKEN_PREFIX;

import com.pearson.sam.bridgeapi.model.SessionUser;
import com.pearson.sam.bridgeapi.repository.UserRepository;
import com.pearson.sam.bridgeapi.serviceimpl.RoleService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class GraphqlBridgeApiAuthorizationFilter {

  private static final Logger logger = LoggerFactory
      .getLogger(GraphqlBridgeApiAuthorizationFilter.class);

  private static final String USER_OBJ = "userObj";

  @Autowired
  AuthService authService;

  @Autowired
  RoleService roleService;

  @Autowired
  @Qualifier(QUERIES_INFO)
  Map<String, Set<String>> queriesMap;

  /**
   * getAuthentication.
   * 
   * @return
   * @throws IOException 
   */
  public UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request,
      HttpServletResponse res, UserRepository userRepo) throws IOException{
    String token = request.getHeader(HEADER_STRING);
    SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(USER_OBJ);
    SessionUser user = null;
    if (StringUtils.isNotBlank(token)) {
      if (sessionUser == null) {
        try {
          addHeaders(res);
          ResponseEntity<Map<String, Object>> resultObject = authService
              .validateToken(token.replaceAll(TOKEN_PREFIX, ""));
          int statusCode = resultObject.getStatusCodeValue();
          if (statusCode == HttpStatus.SC_OK) {
            Map<String, Object> result = resultObject.getBody();
            //logger.info(" Logged In User Detail {}", result);
            Set<String> roles = roleService.getRoles((String) result.get("uid"));
            Set<String> permissions = roleService.getPermissions(roles).parallelStream().filter(queriesMap::containsKey)
                .flatMap(key -> queriesMap.get(key).stream()).collect(Collectors.toSet());
            user = new SessionUser((String) result.get("uid"), (String) result.get("mail"), roles,
                token, permissions);
            request.getSession().setAttribute(USER_OBJ, user);
            authService.updateLastLogin((String) result.get("uid"), userRepo);

            //logger.info(" Logged In After Updating last Login {}", result);

            return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
          } else {
            errorResponse(res, INVALID_TOKEN);
          }
        } catch (Exception e) {
          errorResponse(res, INVALID_TOKEN);
        }
        return null;
      } else {
        return new UsernamePasswordAuthenticationToken(sessionUser, null, new ArrayList<>());
      }
    }
    return null;
  }

  /**
   * getMockAuthentication.
   * 
   * @return
   */
  public UsernamePasswordAuthenticationToken getMockAuthentication(HttpServletRequest request) {
    String token = request.getHeader(HEADER_STRING);
    String uid = request.getHeader("usrId");
    //uid = StringUtils.isNoneBlank(uid) ? uid : "mlsreekanth";
    uid = StringUtils.isNoneBlank(uid) ? uid : "chandraBoseCC1";
    Set<String> roles = roleService.getRoles(uid);
    Set<String> permissions = roleService.getPermissions(roles).parallelStream().filter(queriesMap::containsKey)
        .flatMap(key -> queriesMap.get(key).stream()).collect(Collectors.toSet());
    SessionUser user = new SessionUser(uid, "arun@email,com", roles, token, permissions);
    return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
  }

  private void addHeaders(HttpServletResponse res) {
    res.setHeader("Access-Control-Allow-Origin", "*");
    res.setHeader("Access-Control-Allow-Credentials", "true");
    res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
    res.setHeader("Access-Control-Max-Age", "3600");
    res.setHeader("Access-Control-Allow-Headers",
        "Content-Type, Accept, X-Requested-With, remember-me");
  }

  private void errorResponse(HttpServletResponse res, String errorMessage) throws IOException{
    res.sendError(HttpServletResponse.SC_FORBIDDEN, errorMessage);
  }
}
