package com.pearson.sam.bridgeapi.security;

import com.pearson.sam.bridgeapi.model.SessionUser;

public interface ISessionFacade {
  SessionUser getLoggedInUser(boolean needFullDetails);
}