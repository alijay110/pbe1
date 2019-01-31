package com.pearson.sam.bridgeapi.security;

import static com.pearson.sam.bridgeapi.constants.SecurityConstants.ACCESS_TOKEN;
import static com.pearson.sam.bridgeapi.constants.SecurityConstants.SESSION_EMAIL;
import static com.pearson.sam.bridgeapi.constants.SecurityConstants.SESSION_USERID;

import com.pearson.sam.bridgeapi.serviceimpl.CacheService;

import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpSessionConfig {
  private static final Logger logger = LoggerFactory.getLogger(HttpSessionConfig.class);

  @Autowired
  AuthService authService;

//  @Autowired
//  CacheService cacherService;
  
  /**
   * bean for http session listener.
   * 
   * @return
   */
  @Bean
  public HttpSessionListener httpSessionListener() {
    return new HttpSessionListener() {
      @Override
      // This method will be called when session created
      public void sessionCreated(HttpSessionEvent se) {
        logger.info("Session Created with Access Token+{}",
            se.getSession().getAttribute(ACCESS_TOKEN));
        logger.info("Session Created with session id+{}", se.getSession().getId());
      }

      /*
       * (non-Javadoc) This method will be automatically called when session destroyed.
       * 
       * @see HttpSessionListener#sessionDestroyed(javax.servlet.http.HttpSessionEvent)
       */
      @Override
      public void sessionDestroyed(HttpSessionEvent se) {
        // logout
        se.getSession().removeAttribute(ACCESS_TOKEN);
        se.getSession().removeAttribute(SESSION_USERID);
        se.getSession().removeAttribute(SESSION_EMAIL);
        se.getSession().invalidate();
        logger.info("Session Destroyed, Session id:{}", se.getSession().getId());
        logger.info("Session Destroyed, Session id:{}", se.getSession().getId());
//        cacherService.clearCache(se.getSession().getId());
      }
    };
  }

  /**
   * bean for http session attribute listener.
   * 
   * @return
   */
  @Bean
  public HttpSessionAttributeListener httpSessionAttributeListener() {
    return new HttpSessionAttributeListener() {
      /*
       * (non-Javadoc) This method will be automatically called when session attribute added.
       * 
       * @see
       * HttpSessionAttributeListener#attributeAdded(javax.servlet.http.HttpSessionBindingEvent)
       */
      @Override
      public void attributeAdded(HttpSessionBindingEvent se) {
        logger.info("Attribute Added following information");
        logger.info("Attribute Name:{}", se.getName());
        logger.info("Attribute Value:{}", se.getValue());
      }

      /*
       * (non-Javadoc) This method will be automatically called when session attribute removed.
       * 
       * @see javax.servlet.http.HttpSessionAttributeListener#attributeRemoved(javax.servlet.http.
       * HttpSessionBindingEvent)
       */
      @Override
      public void attributeRemoved(HttpSessionBindingEvent se) {
        logger.info("attributeRemoved");
      }

      /*
       * (non-Javadoc) This method will be automatically called when session attribute replace
       * 
       * @see javax.servlet.http.HttpSessionAttributeListener#attributeReplaced(javax.servlet.http.
       * HttpSessionBindingEvent)
       */
      @Override
      public void attributeReplaced(HttpSessionBindingEvent se) {
        logger.info("Attribute Replaced following information");
        logger.info("Attribute Name:{}", se.getName());
        logger.info("Attribute Old Value:{}", se.getValue());
      }
    };
  }
}