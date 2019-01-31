package com.pearson.sam.bridgeapi;

import org.springframework.boot.SpringApplication;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import com.pearson.sam.bridgeapi.config.AppConfig;

@ContextConfiguration(classes = { BridgeApiApplication.class, AppConfig.class,MockHttpServletRequest.class })
@TestPropertySource(locations = { "classpath:test-application.properties" })
public class BridgeApiApplicationTest extends BridgeApiApplication {
	
	static {
		System.setProperty("product", "PP");
		System.setProperty("env", "Local");
	    System.setProperty("authEnv", "true");
	}
	
	public static void main(String[] args) {
        SpringApplication.run(BridgeApiApplicationTest.class, args);
    }

}
