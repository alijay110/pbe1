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
import com.pearson.sam.bridgeapi.model.Organisation;
import com.pearson.sam.bridgeapi.samservices.OrganizationServices;

@RunWith(SpringRunner.class)
@TestPropertySource("classpath:test-application.properties")
public class OrganisationSamClientTest {
	static {
		System.setProperty("product", "PP");
		System.setProperty("env", "Local");

	}
	@InjectMocks
	private OrganisationSamClient<Organisation> underTest = new OrganisationSamClient<>();

	@Mock
	private OrganizationServices organizationServices;

	private final GsonBuilder gsonBuilder = new GsonBuilder();
	private final Gson gson = gsonBuilder.setPrettyPrinting().disableHtmlEscaping().create();

	@Value("${SPEC_FILE}")
	private String specJsonFile;

	String samUrl;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		ReflectionTestUtils.setField(underTest, "specJsonFile", specJsonFile);
	}

	private Map<String, String> getOrganizationRequestMap() {
		Map<String, String> request = new HashMap<>();
		request.put("serviceType", "organization");
		request.put("organisationId", "orgIdentifier");
		request.put("name", "orgName");
		request.put("schoolStatus", "orgStatus");
		request.put("activeTimestamp", "activeTimestamp");
		request.put("productModelIdentifier", "productModelIdentifier");
		request.put("createdOn", "createdOn");
		request.put("createdBy", "createdBy");
		request.put("updatedOn", "updatedOn");
		request.put("updatedBy", "updatedBy");
		request.put("orgIdentifier", "organisationId");

		return request;
	}

	private Map<String, String> getSchoolRequestMap() {
		Map<String, String> requestSchool = new HashMap<>();
		requestSchool.put("serviceType", "organization123");
		requestSchool.put("organisationId", "orgIdentifier");
		requestSchool.put("name", "orgName");
		requestSchool.put("schoolStatus", "orgStatus");
		requestSchool.put("activeTimestamp", "activeTimestamp");
		requestSchool.put("productModelIdentifier", "productModelIdentifier");
		requestSchool.put("createdOn", "createdOn");
		requestSchool.put("createdBy", "createdBy");
		requestSchool.put("updatedOn", "updatedOn");
		requestSchool.put("updatedBy", "updatedBy");
		requestSchool.put("orgIdentifier", "organisationId");

		return requestSchool;
	}

	private Map<String, Object> getOrganizationResponseMap() {
		Map<String, Object> rolesMap = new HashMap<>();
		rolesMap.put("orgIdentifier", "organisationId");
		rolesMap.put("orgName", "name");
		rolesMap.put("productModelIdentifier", "productModelIdentifier");
		rolesMap.put("createdOn", "createdOn");
		rolesMap.put("createdBy", "createdBy");
		rolesMap.put("updatedOn", "updatedOn");
		rolesMap.put("updatedBy", "updatedBy");
		return rolesMap;
	}

	@Test
	public void testCreate() {
		when(organizationServices.createOrganization(any(), any())).thenReturn(getOrganizationResponseMap());

		JsonObject documents = gson.toJsonTree(getOrganizationRequestMap()).getAsJsonObject();
		Object map = underTest.create(documents);
		assertNotNull(map);

		JsonObject documentsSchool = gson.toJsonTree(getSchoolRequestMap()).getAsJsonObject();
		Object mapSchool = underTest.create(documentsSchool);
		assertNotNull(mapSchool);
	}

	@Test
	public void testUpdate() {
		when(organizationServices.updateOrganization(any(), any())).thenReturn(getOrganizationResponseMap());

		JsonObject documents = gson.toJsonTree(getOrganizationRequestMap()).getAsJsonObject();
		Map<String, String> params = new HashMap<String, String>();
		params.put("organisationId", "organisationId");
		Object map = underTest.update(params, documents);
		assertNotNull(map);

		JsonObject documentsSchool = gson.toJsonTree(getSchoolRequestMap()).getAsJsonObject();
		Map<String, String> paramsSchool = new HashMap<String, String>();
		paramsSchool.put("schoolId", "schoolId");
		Object mapSchool = underTest.update(paramsSchool, documentsSchool);
		assertNotNull(mapSchool);
	}

	@Test
	public void testFetchOne() {
		when(organizationServices.getOrganization(anyString())).thenReturn(getOrganizationResponseMap());

		JsonObject documents = gson.toJsonTree(getOrganizationRequestMap()).getAsJsonObject();
		Object map = underTest.fetchOne(documents);
		assertNotNull(map);

		JsonObject documentsSchool = gson.toJsonTree(getSchoolRequestMap()).getAsJsonObject();
		Object mapSchool = underTest.fetchOne(documentsSchool);
		assertNotNull(mapSchool);
	}

	@Test
	public void testDeactivate() {
		when(organizationServices.deactivateOrganization(any(), any())).thenReturn(getOrganizationResponseMap());

		JsonObject documentsSchool = gson.toJsonTree(getSchoolRequestMap()).getAsJsonObject();
		Object mapSchool = underTest.deactivate("orgIdentifier", documentsSchool);
		assertNotNull(mapSchool);
	}

	@Test
	public void testFetchMultiple() {
		List<String> ids = new ArrayList<>();
		List<Organisation> map = underTest.fetchMultiple(ids);
		assertTrue(map.isEmpty());
	}

	@Test
	public void testDelete() {
		Organisation map = underTest.delete("id");
		assertNull(map);
	}

	@Test
	public void testCreateMultiple() {
		JsonObject documents = gson.toJsonTree(getSchoolRequestMap()).getAsJsonObject();
		List<Organisation> map = underTest.createMultiple(documents);
		assertTrue(map.isEmpty());
	}
}
