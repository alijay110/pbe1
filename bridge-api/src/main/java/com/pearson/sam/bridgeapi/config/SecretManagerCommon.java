package com.pearson.sam.bridgeapi.config;

import java.security.InvalidParameterException;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.DecryptionFailureException;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.amazonaws.services.secretsmanager.model.InternalServiceErrorException;
import com.amazonaws.services.secretsmanager.model.InvalidRequestException;
import com.pearson.sam.bridgeapi.util.Utils;

@Configuration
public class SecretManagerCommon {

	private static final Logger LOGGER = LoggerFactory.getLogger(SecretManagerCommon.class);

	public static Map<String, Object> secretMap = null;
	public static String localRedisPassword = null;

	public static void getSecret() throws ParseException {

		AWSSecretsManager client = AWSSecretsManagerClientBuilder.standard()
				.withRegion(Regions.getCurrentRegion() != null ? Regions.getCurrentRegion().getName() : null).build();

		String secret;
		GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest()
				.withSecretId("pp2-bridge-common-secret");
		GetSecretValueResult getSecretValueResult = null;

		try {
			getSecretValueResult = client.getSecretValue(getSecretValueRequest);
		} catch (DecryptionFailureException | InternalServiceErrorException | InvalidParameterException
				| InvalidRequestException | ResourceNotFoundException e) {
			LOGGER.error(e.getMessage());
			throw e;
		}

		if (getSecretValueResult != null && getSecretValueResult.getSecretString() != null) {
			secret = getSecretValueResult.getSecretString();
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(secret);
			secretMap = Utils.convertToMap(json);
		} else {
			LOGGER.error("No Value found from AWS secret-manager");
		}
	}
}
