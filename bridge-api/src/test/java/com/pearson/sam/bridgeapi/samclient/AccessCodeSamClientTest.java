package com.pearson.sam.bridgeapi.samclient;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.pearson.sam.bridgeapi.BridgeApiApplicationTest;
import com.pearson.sam.bridgeapi.model.AccessCodes;
import com.pearson.sam.bridgeapi.samservices.AccessCodeServices;

@EnableAutoConfiguration
@RunWith(SpringRunner.class)
public class AccessCodeSamClientTest extends BridgeApiApplicationTest {

	private final GsonBuilder gsonBuilder = new GsonBuilder();
	private final Gson gson = gsonBuilder.setPrettyPrinting().disableHtmlEscaping().create();
	protected static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	@Value("${SPEC_FILE}")
	String specJsonFile;

	@Mock
	AccessCodeServices accessCodeServices;

	@InjectMocks
	private AccessCodeSamClient underTest = new AccessCodeSamClient();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		ReflectionTestUtils.setField(underTest, "specJsonFile", specJsonFile);
	}

	@Test
	public void testCreate() {
		Map<String, String> request = new HashMap<>();
		request.put("userName", "userName");
		request.put("accessCodesId", "ACCESSCODES01");
		JsonObject documents = gson.toJsonTree(request).getAsJsonObject();
		when(accessCodeServices.createAccessCode(any(String.class))).thenReturn(getAccessCodesMap());
		AccessCodes accessCodes = underTest.create(documents);
		Assert.assertNotNull(accessCodes);
	}

	@Test
	public void testFetchOne() {
		Map<String, String> request = new HashMap<>();
		request.put("userName", "userName");
		request.put("quantity", "20");
		request.put("code", "test");
		request.put("accessCode", "accessCode");
		request.put("accessCodeId", "ACCESSCODES01");
		JsonObject documents = gson.toJsonTree(request).getAsJsonObject();
		when(accessCodeServices.getAccessCodeDetails(anyString())).thenReturn(getAccessCodesMap());
		AccessCodes accessCodes = underTest.fetchOne(documents);
		Assert.assertNotNull(accessCodes);
	}

	@Test
	public void testaddAccessCodes() {
		when(accessCodeServices.addAccessCodes(anyString())).thenReturn(getAccessCodesMap());
		Map<String, Object> requestMap = underTest.addAccessCodes("Values");
		Assert.assertNotNull(requestMap);
	}

	@Test
	public void testGetAllSubscriptionDetailsofUser() {
		when(accessCodeServices.getAllSubscriptionDetailsofUser(anyString())).thenReturn(getAccessCodesMap());
		Map<String, Object> requestMap = underTest.getAllSubscriptionDetailsofUser("userName");
		Assert.assertNotNull(requestMap);
	}

	private Map<String, Object> getAccessCodesMap() {
		Map<String, Object> accessCodesMap = new HashMap<String, Object>();
		// accessCodesMap.put("accessCodeId", "ACCESSCODES01");
		accessCodesMap.put("userName", "userName");
		accessCodesMap.put("quantity", "20");
		accessCodesMap.put("scope", "test");
		return accessCodesMap;
	}

	private AccessCodes getAccessCodes() {
		AccessCodes accesscodes = new AccessCodes();
		accesscodes.setAccessCode("HPB-6JF-RGJT");
		return accesscodes;
	}
}
