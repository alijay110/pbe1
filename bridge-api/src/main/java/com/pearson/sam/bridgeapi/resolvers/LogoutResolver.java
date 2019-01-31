package com.pearson.sam.bridgeapi.resolvers;

import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.LOGOUT_URI;

import com.pearson.sam.bridgeapi.config.AuthContext;
import com.pearson.sam.bridgeapi.resthelper.RestClient;
import com.pearson.sam.bridgeapi.resthelper.RestRequestBuilder;
import com.pearson.sam.bridgeapi.samclient.IdamClient;
import com.pearson.sam.bridgeapi.security.ISessionFacade;

import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLRootContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LogoutResolver {
  private static final Logger logger = LoggerFactory.getLogger(LogoutResolver.class);


  @Autowired
  protected ISessionFacade sessionFacade;

  @Autowired
  IdamClient idamClient;
  
  /**
   * logout.
   * 
   * @param userName
   *          desc
   * @param context
   *          desc
   * @return
   */
//  @SuppressWarnings({ "deprecation" })
//  @GraphQLMutation(name = LOGOUT_URI, description = "Logout")
//  public String logout(@GraphQLArgument(name = "userName") String userName,
//      @GraphQLRootContext AuthContext context) {
//    //logger.info("user logsout - {}", userName);
//    Optional<HttpServletRequest> request = context.getRequest();
//    String token = sessionFacade.getLoggedInUser(false).getToken();
//    Map<String, Object> resultMap = idamClient.callLogout(token);
//
//    if (request.isPresent()) {
//      HttpSession session = request.get().getSession();
//      session.invalidate();
//    }
//    return (String) resultMap.get("result");
//  }


}
