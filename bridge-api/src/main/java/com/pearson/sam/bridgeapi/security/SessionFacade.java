package com.pearson.sam.bridgeapi.security;

import com.pearson.sam.bridgeapi.model.SessionUser;
import com.pearson.sam.bridgeapi.model.User;
import com.pearson.sam.bridgeapi.repository.UserRepository;

import java.util.Collections;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.annotation.SessionScope;

@Component
public class SessionFacade implements ISessionFacade {

  @Autowired
  private UserRepository userRepository;

  /*
   * (non-Javadoc)
   * 
   * @see com.pearson.sam.bridgeapi.security.IUserFacade#getLoggedInUser()
   */
  @Override
  public SessionUser getLoggedInUser(boolean needFullDetails) {
    Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    SessionUser sessionUser = null;
    if (obj instanceof String) {
      sessionUser = new SessionUser((String) obj, null, Collections.emptySet(), StringUtils.EMPTY,
          Collections.emptySet());
    } else {
      sessionUser = (SessionUser) SecurityContextHolder.getContext().getAuthentication()
          .getPrincipal();
    }

    if (needFullDetails) {
      User user = userRepository.findByIdentifier(sessionUser.getUserId());
      return new SessionUser(sessionUser, user);
    }
    return sessionUser;
  }

}