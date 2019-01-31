package com.pearson.sam.bridgeapi.samclient;

import static org.junit.Assert.*;

import static java.util.Arrays.asList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pearson.sam.bridgeapi.model.Voucher;
import com.pearson.sam.bridgeapi.samservices.AccessCodeServices;


@RunWith(SpringRunner.class)
@TestPropertySource("classpath:test-application.properties")
public class VoucherSamClientTest {
	
	static {
		System.setProperty("product", "PP");
		System.setProperty("env", "Local");
		
	}

	@Mock
	AccessCodeServices accessCodeServices;

	@Value("${SPEC_FILE}")
	private String specJsonFile;
	
	@InjectMocks
	private VoucherSamClient underTest;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		ReflectionTestUtils.setField(underTest, "specJsonFile", specJsonFile);
	}
	

	@Test
	public void testCreate() {
		when(accessCodeServices.createAccessCode(anyString())).thenReturn(getVoucherMap());
		JsonArray jsonArray = new JsonArray();
		jsonArray.add("PP2VOUCHER01");
		JsonObject documents = new JsonObject();
		documents.add("products", jsonArray);
		Voucher voucher = underTest.create(documents);
		assertNotNull(voucher);
	}
	
	@Test
	public void testCreateproductNull() {
		when(accessCodeServices.createAccessCode(anyString())).thenReturn(getVoucherMap());
		JsonObject documents = new JsonObject();
		Voucher voucher = underTest.create(documents);
		assertNotNull(voucher);
	}
	
	@Test
	public void testUpdate() {
		when(accessCodeServices.configueAccessCode(anyString(),anyString())).thenReturn(getVoucherMap());
		Map<String,String> params = new HashMap<>();
		params.put("accessCode", "WVC-DQ4-86DJ");
		
		JsonObject documents = new JsonObject();
		JsonArray jsonarray = new JsonArray();
		jsonarray.add("PRODUCT1");jsonarray.add("PRODUCT2");
		documents.add("products",jsonarray);
		documents.addProperty("createdBy", "PP");
		documents.addProperty("updatedBy","chandraBoseCC1");
		
		Voucher result = underTest.update(params, documents);
		assertNotNull(result);
	}
	
	@Test
	public void testUpdateProductNull() {
		when(accessCodeServices.configueAccessCode(anyString(),anyString())).thenReturn(getVoucherMap());
		Map<String,String> params = new HashMap<>();
		params.put("accessCode", "WVC-DQ4-86DJ");
		
		JsonObject documents = new JsonObject();
		
		documents.addProperty("createdBy", "PP");
		documents.addProperty("updatedBy","chandraBoseCC1");
		
		Voucher result = underTest.update(params, documents);
		assertNotNull(result);
	}
	
	@Test
	public void testfetchOne() {

	    Map<String,Object> vouchermap = getVoucherMap();
	    JsonObject documents = new JsonObject();
	    documents.addProperty("accessCodeId", "PP2VOUCHER00");
	    when(accessCodeServices.getAccessCodeDetails(anyString())).thenReturn(vouchermap);
	    Voucher result = underTest.fetchOne(documents);
	    assertNotNull(result);
		
	}
	
	@Test
	public void testfetchMultiple() {
		List<Voucher> voucherlist = underTest.fetchMultiple(asList("PP2VOUCHER40","PP2VOUCHER20"));
		assertTrue(voucherlist.isEmpty());
	}
	
	@Test
	public void delete() {
		Voucher voucher = underTest.delete("PP2VOUCHER40");
		assertNull(voucher);
	}
	
	@Test
	public void testcreateMultiple() {
		List<Voucher> voucherlist = underTest.createMultiple(new JsonObject());
		assertTrue(voucherlist.isEmpty());
	}
	 
	private final Map<String,Object> getVoucherMap() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -7);
		Map<String,Object> vouchermap = new HashMap<>();
		vouchermap.put("voucherId", "PP2VOUCHER00");
		vouchermap.put("voucherCode", "PP2VOUCHER00");
		vouchermap.put("createdBy", "PP");
		vouchermap.put("createdOn", cal.getTime().toString());
		vouchermap.put("updatedBy","chandraBoseCC1");
		cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -4);
		vouchermap.put("updatedOn",cal.getTime().toString());
		return vouchermap;
	}
	
}
