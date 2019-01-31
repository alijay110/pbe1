package com.pearson.sam.bridgeapi.config;

import com.pearson.sam.bridgeapi.util.PropertyHolder;

import java.util.Optional;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    boolean isLocal = Optional.ofNullable(PropertyHolder.getBooleanProperty("authEnv"))
        .isPresent();

    if (isLocal) {
      localMockConfiguration(http);
    } else {
      serverConfiguration(http);
    }
  }

  private void serverConfiguration(HttpSecurity http) throws Exception {
    http.csrf().disable().authorizeRequests().anyRequest().permitAll();
  }

  private void localMockConfiguration(HttpSecurity http) throws Exception {
    http.csrf().disable().authorizeRequests().anyRequest().permitAll();
  }

}