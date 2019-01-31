package com.pearson.sam.bridgeapi.samclient;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import static java.util.Arrays.asList;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pearson.sam.bridgeapi.model.AccessCodes;
import com.pearson.sam.bridgeapi.samservices.AccessCodeServices;
import com.pearson.sam.bridgeapi.util.Utils;

@RunWith(SpringRunner.class)
@TestPropertySource("classpath:test-application.properties")
public class SubscriptionSamClientTest {
	
	static {
		System.setProperty("product", "PP");
		System.setProperty("env", "Local");
		
	}
	
	@Mock
	AccessCodeServices accessCodeServices;

	@Value("${SPEC_FILE}")
	private String specJsonFile;
	  
	@InjectMocks
	private SubscriptionSamClient underTest;
	  
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		ReflectionTestUtils.setField(underTest, "specJsonFile", specJsonFile);
	}
	
	@Test
	public void testCreate() {
		Map<String,Object> accesscodemap = getAccessCodeMap();
		JsonObject jsonproduct = new JsonObject();
		JsonArray jsonArray = new JsonArray();
		jsonArray.add("Product21");
		jsonArray.add("Product22");
		jsonproduct.add("products", jsonArray);
		
		when(accessCodeServices.createAndActivateProductAndSubscription(anyString(),anyString())).thenReturn(accesscodemap);
		JsonObject accessjsonObject = new JsonObject();
		accessjsonObject.addProperty("accessCode", "7HJ-HCG-GG3X");
		accessjsonObject.addProperty("requestParam", "");
		accessjsonObject.addProperty("","");
		accessjsonObject.add("products", jsonArray);
		AccessCodes result = underTest.create(accessjsonObject);
		assertNotNull(result);
	}
	
	@Test
	public void testCreateProductsNull() {
		Map<String,Object> accesscodemap = getAccessCodeMap();
		when(accessCodeServices.createAndActivateProductAndSubscription(anyString(),anyString())).thenReturn(accesscodemap);
		JsonObject accessjsonObject = new JsonObject();
		accessjsonObject.addProperty("accessCode", "7HJ-HCG-GG3X");
		accessjsonObject.addProperty("requestParam", "");
		accessjsonObject.addProperty("","");
		AccessCodes result = underTest.create(accessjsonObject);
		assertNotNull(result);
	}
	
	@Test
	public void testCreaterequestParamNull() {
		Map<String,Object> accesscodemap = getAccessCodeMap();
		when(accessCodeServices.createAndActivateProductAndSubscription(anyString(),anyString())).thenReturn(accesscodemap);
		JsonObject accessjsonObject = new JsonObject();
		accessjsonObject.addProperty("accessCode", "7HJ-HCG-GG3X");
		accessjsonObject.addProperty("","");
		AccessCodes result = underTest.create(accessjsonObject);
		assertNotNull(result);
	}
	
	@Test
	public void testCreaterequestParamNotNull() {
		Map<String,Object> accesscodemap = getAccessCodeMap();
		when(accessCodeServices.createAndActivateProductAndSubscription(anyString(),anyString())).thenReturn(accesscodemap);
		JsonObject accessjsonObject = new JsonObject();
		accessjsonObject.addProperty("accessCode", "7HJ-HCG-GG3X");
		accessjsonObject.addProperty("","");
		accessjsonObject.addProperty("requestParam","7HJ-HCG-GG3X");
		AccessCodes result = underTest.create(accessjsonObject);
		assertNotNull(result);
	}
	@Test
	public void testCreateMultiple() {
		List<AccessCodes> accesscodes = underTest.createMultiple(new JsonObject());
		assertNull(accesscodes);
	}
	
	@Test
	public void testUpdate() {
		AccessCodes accesscodes = underTest.update(new HashMap<String,String>(),new JsonObject());
		assertNull(accesscodes);
	}
	
	
	@Test
	public void testfetchOne() {
		Map<String,Object> accesscodemap = getAccessCodeMap();
		when(accessCodeServices.getAccessCodeDetails(anyString())).thenReturn(accesscodemap);
	    JsonObject accessjsonObject = new JsonObject();
	    accessjsonObject.addProperty("accessCode", "7HJ-HCG-GG3X");
	    AccessCodes result = underTest.fetchOne(accessjsonObject);
	    assertNotNull(result);
	}
	
	@Test
	public void testGetAccessCodeSubscription() {
		AccessCodes accessCodes = new AccessCodes();
		accessCodes.setAccessCode("ABC");
		when(accessCodeServices.getAccessCodeDetails(anyString())).thenReturn(getAccessCodeMap());
		AccessCodes result = underTest.getAccessCodeSubscription(accessCodes);
	    assertNotNull(result);
		
	}
	
	
	@Test
	public void testdelete() {
		AccessCodes accesscodes = underTest.delete("tobedelted");
		assertNull(accesscodes);
		
	}
	
	private Map<String,Object> getAccessCodeMap() {
		Map<String,Object> accesscodemap = new HashMap<>();
		AccessCodes accesscodes = new AccessCodes();
		List<AccessCodes> accesscodeslist = new ArrayList<>();
		
		accesscodes.setAccessCode("7HJ-HCG-GG3X");
		accesscodes.setCode("7HJ-HCG-GG3X");
		accesscodes.setCreatedBy("PP");
		accesscodes.setDateCreated("");
		accesscodes.setUserName("chandraBoseCC1");
		accesscodeslist.add(accesscodes);
		accesscodemap.put("accessCode","7HJ-HCG-GG3X");
		accesscodemap.put("subscriptionList",accesscodeslist);
		accesscodemap.put("startDate","");
		accesscodemap.put("endDate","");
		
	    return accesscodemap;
	}
	
}
