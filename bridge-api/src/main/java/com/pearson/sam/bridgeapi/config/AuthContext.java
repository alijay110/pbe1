package com.pearson.sam.bridgeapi.config;

import com.pearson.sam.bridgeapi.model.SessionUser;

import graphql.servlet.GraphQLContext;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthContext extends GraphQLContext {

  private final SessionUser user;

  public AuthContext(SessionUser user, Optional<HttpServletRequest> request,
      Optional<HttpServletResponse> response) {
    super(request, response);
    this.user = user;
  }

  public SessionUser getUser() {
    return user;
  }

}