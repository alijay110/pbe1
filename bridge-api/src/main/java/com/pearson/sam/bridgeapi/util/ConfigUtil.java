package com.pearson.sam.bridgeapi.util;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigUtil {

	static Logger logger = LoggerFactory.getLogger(ConfigUtil.class);

	public static final String GET_ASSET_DETAILS_URL = "http://api-aws-sin.stage.pulse.pearson.com:80/coursesection/learningmaterial?courseId=";
	public static final String USER_AUTH_TOKEN_URL = "http://auth.stage.pulse.pearson.com:80/user/authenticate";
	private static ConfigUtil instance;
	private static String authToken;
	private static final String OAUTH_CONFIG_FILE_NAME = "src/main/resources/config.properties";


	public static String getPassword() {
		PropertiesConfiguration config;
		String password = null;
		try {
			config = new PropertiesConfiguration(OAUTH_CONFIG_FILE_NAME);
			password = String.valueOf(config.getProperty("user.password"));
		} catch (org.apache.commons.configuration.ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//logger.error("Error while retrieving config details");
		}
		return password;
	}

	public static String getAssetDetailsUrlForCourse() {
		PropertiesConfiguration config;
		String assetDetailForCourseUrl = null;
		try {
			config = new PropertiesConfiguration(OAUTH_CONFIG_FILE_NAME);
			assetDetailForCourseUrl = String.valueOf(config.getProperty("course.asset.url"));
		} catch (org.apache.commons.configuration.ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//logger.error("Error while retrieving config details");
		}
		return assetDetailForCourseUrl;
	}

	public static String getUserAuthTokenGeneratorUrl() {
		PropertiesConfiguration config;
		String authTokenUrl = null;
		try {
			config = new PropertiesConfiguration(OAUTH_CONFIG_FILE_NAME);
			authTokenUrl = String.valueOf(config.getProperty("user.auth.token.url"));
		} catch (org.apache.commons.configuration.ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//logger.error("Error while retrieving config details");
		}
		return authTokenUrl;
	}
}
