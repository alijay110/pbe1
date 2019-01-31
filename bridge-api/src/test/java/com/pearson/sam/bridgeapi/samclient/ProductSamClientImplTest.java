package com.pearson.sam.bridgeapi.samclient;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.google.gson.JsonObject;
import com.pearson.sam.bridgeapi.model.Product;
import com.pearson.sam.bridgeapi.samclient.ProductSamClientImpl;
import com.pearson.sam.bridgeapi.samservices.ProductServices;


@RunWith(SpringRunner.class)
@TestPropertySource("classpath:test-application.properties")
public class ProductSamClientImplTest {

	static {
		System.setProperty("product", "PP");
		System.setProperty("env", "Local");
		
	}
	
	@Value("${SPEC_FILE}")
	private String specJsonFile;

	@Mock
	ProductServices productServices;
	
	@InjectMocks
	private ProductSamClientImpl underTest;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		ReflectionTestUtils.setField(underTest, "specJsonFile", specJsonFile);
	}
	
	
	@Test
	public void testCreate() {
		when(productServices.createProduct(anyString())).thenReturn(getProductMap());
		Product product = underTest.create(new JsonObject());
		assertNotNull(product);
	}
	

	@Test
	public void testUpdate() {
		when(productServices.updateProduct(anyString(),anyString())).thenReturn(getProductMap());
		Map<String,String> productmap = new HashMap<>();
		productmap.put("productidentifier","");
		Product product = underTest.update(productmap,new JsonObject());
		assertNotNull(product);
		
	}
	
	@Test
	public void testfetchOne() {
		when(productServices.getProduct(anyString())).thenReturn(getProductMap());
		JsonObject productjson = new JsonObject();
		productjson.addProperty("productId", "PP2PRODUCT21");
		Product product = underTest.fetchOne(productjson);
		assertNotNull(product);
	}
	
	
	@Test
	public void testfetchMultiple() {
		when(productServices.getProducts()).thenReturn(getProductMap());
		List<Product> result = underTest.fetchMultiple(asList("PP2PRODUCT21"));
		assertNotNull(result);
	
	}
	
	private final Map<String,Object> getProductMap() {
		Map<String,String> map = new HashMap<>();
		map.put("productIdentifier","PP2PRODUCT21");
		map.put("productName","Chemistry");
		map.put("createdBy","PP");
		map.put("createdOn","");
		map.put("updatedBy", "ChandraBoseCC1");
		map.put("updatedOn", "");
		List<Map<String,String>> productlist = new ArrayList<>();
		productlist.add(map);
		Map<String,Object> productmap = new HashMap<>();
		productmap.put("product",productlist);
		productmap.put("status", false);
		return productmap;
	}

}
