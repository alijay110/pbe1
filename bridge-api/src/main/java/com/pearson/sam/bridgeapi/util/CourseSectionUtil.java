package com.pearson.sam.bridgeapi.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.json.simple.JSONObject;

@SuppressWarnings("deprecation")
public class CourseSectionUtil {

	static Logger logger = LoggerFactory.getLogger(CourseSectionUtil.class);

	private static CourseSectionUtil instance;
	private static String authToken;
	private static ConfigUtil configUtil;

	public static CourseSectionUtil getInstance() {
		if (instance == null) {
			instance = new CourseSectionUtil();
			configUtil = new ConfigUtil();
		}
		return instance;
	}

	public static String getAssetDetailsForCourse(String courseId, String username) {
		String jsonResponse = null;
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet getRequest = new HttpGet(ConfigUtil.GET_ASSET_DETAILS_URL + courseId);
			setHeaders(getRequest, username);
			HttpResponse response = httpClient.execute(getRequest);

			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

			String output;
			String responseBody = "";
			while ((output = br.readLine()) != null) {
				logger.info("Response from pulse {}", output);
				responseBody += "\n" + output;
			}
			jsonResponse = responseBody;
			httpClient.getConnectionManager().shutdown();
			JSONParser parser = new JSONParser();
			org.json.simple.JSONArray json = (org.json.simple.JSONArray) parser.parse(jsonResponse);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("access_token", authToken);
			json.add(jsonObject);
			jsonResponse = json.toJSONString();

		} catch (Exception e) {
			logger.error("Error while retrieving asset details for the course {} with error {}", courseId,
					e.getMessage());
			return null;
		}
		return jsonResponse;
	}

	private static void setHeaders(HttpGet request, String username) {
		request.addHeader("Content-Type", "application/json");
		request.addHeader("Accept", "application/json");
		request.addHeader("Accept-Language", "en");
		request.addHeader("appVersion", "100");
		request.addHeader("deviceid", "100");
		//String adminToken = "edc3b23f-b853-49a8-822d-9fa8d30be35c";
		// testStudentBH54
		authToken = getToken(username);
		logger.info("Generated Access Token {} " , authToken);
		String auth_token = "Bearer " + authToken;
		request.addHeader("Authorization", auth_token);
	}

	private static String getToken(String username) {
		String jsonResponse = null;
		try {

			
			HttpPost postRequest = new HttpPost(ConfigUtil.USER_AUTH_TOKEN_URL);

			JSONObject json = new JSONObject();
			json.put("username", username);
			json.put("password", "Test@123");
			StringEntity input = new StringEntity(json.toString());
			// Request header parameters and other properties.
			postRequest.addHeader("Content-Type", "application/json");
			postRequest.addHeader("Accept", "application/json");
			postRequest.addHeader("Accept-Language", "en");
			postRequest.addHeader("appVersion", "device-101");
			postRequest.addHeader("deviceid", "service-101");
			postRequest.setEntity(input);
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse response = httpClient.execute(postRequest);

			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

			String readLine;
			String responseBody = "";

			System.out.println("Output from Server .... \n");
			while ((readLine = br.readLine()) != null) {
				responseBody += "\n" + readLine;
			}

			httpClient.getConnectionManager().shutdown();

			int index = responseBody.indexOf(":");
			String extractedToken = null;
			if (index >= 0) {
				extractedToken = responseBody.substring(index + 2, responseBody.indexOf("}") - 1);
			}

			jsonResponse = extractedToken;
		} catch (Exception e) {
			logger.error("Error while retrieving user access token" + e.getMessage());
			return null;
		}

		return jsonResponse;
	}
}
