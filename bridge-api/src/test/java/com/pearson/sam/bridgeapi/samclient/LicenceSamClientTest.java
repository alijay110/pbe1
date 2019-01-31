package com.pearson.sam.bridgeapi.samclient;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.pearson.sam.bridgeapi.model.Licence;
import com.pearson.sam.bridgeapi.samservices.AccessCodeServices;

@RunWith(SpringRunner.class)
@TestPropertySource("classpath:test-application.properties")
public class LicenceSamClientTest {
	static {
		System.setProperty("product", "PP");
		System.setProperty("env", "Local");

	}

	@InjectMocks
	private LicenceSamClient underTest = new LicenceSamClient();

	private final GsonBuilder gsonBuilder = new GsonBuilder();
	private final Gson gson = gsonBuilder.setPrettyPrinting().disableHtmlEscaping().create();

	@Value("${SPEC_FILE}")
	private String specJsonFile;

	@Mock
	private AccessCodeServices accessCodeServices;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		ReflectionTestUtils.setField(underTest, "specJsonFile", specJsonFile);
	}

	@Test
	public void testCreate() {
		when(accessCodeServices.createAccessCode(any())).thenReturn(getLicenceResponse());

		JsonObject documents = gson.toJsonTree(getLicenceRequest()).getAsJsonObject();
		Licence map = underTest.create(documents);
		assertNotNull(map);
	}

	@Test
	public void testUpdate() {
		when(accessCodeServices.configueAccessCode(anyString(), anyString())).thenReturn(getLicenceResponse());

		Map<String, String> params = new HashMap<String, String>();
		params.put("licenceId", "licenceId");
		JsonObject documents = gson.toJsonTree(getLicenceRequest()).getAsJsonObject();
		Licence map = underTest.update(params, documents);
		assertNotNull(map);
	}

	@Test
	public void testFetchOne() {
		when(accessCodeServices.getAccessCodeDetails(any())).thenReturn(getLicenceResponse());

		JsonObject documents = gson.toJsonTree(getLicenceRequest()).getAsJsonObject();
		Licence map = underTest.fetchOne(documents);
		assertNotNull(map);
	}

	@Test
	public void testFetchMultiple() {
		List<String> ids = new ArrayList<>();
		List<Licence> map = underTest.fetchMultiple(ids);
		assertTrue(map.isEmpty());
	}

	@Test
	public void testDelete() {
		Licence map = underTest.delete("id");
		assertNull(map);
	}

	@Test
	public void testCreateMultiple() {
		JsonObject documents = gson.toJsonTree(getLicenceRequest()).getAsJsonObject();
		List<Licence> map = underTest.createMultiple(documents);
		assertTrue(map.isEmpty());
	}

	private Map<String, Object> getLicenceRequest() {
		Map<String, Object> objectMap = new HashMap<>();
		objectMap.put("accessCode", "accessCode");
		objectMap.put("dateCreated", "createdOn");
		objectMap.put("createdBy", "createdBy");
		objectMap.put("updatedOn", "updatedOn");
		objectMap.put("updatedBy", "updatedBy");
		objectMap.put("dateCreated", "createdDate");

		List<String> products = new ArrayList<>();
		products.add("PRODUCT01");
		products.add("PRODUCT02");
		objectMap.put("products", products);

		List<Map<String, String>> productList = new ArrayList<>();
		Map<String, Object> prods = new HashMap<>();
		prods.put("products", productList);
		objectMap.put("scope", prods);

		return objectMap;
	}

	private Map<String, Object> getLicenceResponse() {
		Map<String, Object> objectMap = new HashMap<>();
		objectMap.put("accessCode", "accessCode");
		objectMap.put("dateCreated", "createdOn");
		objectMap.put("createdBy", "createdBy");
		objectMap.put("lastActivatedDate", "updatedOn");
		objectMap.put("lastActivatedBy", "updatedBy");
		objectMap.put("dateCreated", "createdDate");
		objectMap.put("scope", "scope");
		objectMap.put("quantity", "quantity");
		objectMap.put("userName", "userName");
		objectMap.put("subscriptionDate", "subscriptionDate");

		return objectMap;
	}
}
