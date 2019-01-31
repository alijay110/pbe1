package com.pearson.sam.bridgeapi.samclient;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.pearson.sam.bridgeapi.model.Classroom;
import com.pearson.sam.bridgeapi.samservices.GroupServices;

@RunWith(MockitoJUnitRunner.class)
public class ClassRoomSamClientTest {
	
	@InjectMocks
	private ClassRoomSamClient underTest = new ClassRoomSamClient();

	@Mock
	private GroupServices groupServices;
	
	private final GsonBuilder gsonBuilder = new GsonBuilder();
	private final Gson gson = gsonBuilder.setPrettyPrinting().disableHtmlEscaping().create();

	@Test
	public void testCreate() {
		Map<String, String> request = new HashMap<>();
	    request.put("classRoomId", "CLASSROOM01");
	    JsonObject documents = gson.toJsonTree(request).getAsJsonObject();
	    
		Map<String, Object> rolesMap = new HashMap<>();
		when(groupServices.createGroup(any(String.class))).thenReturn(rolesMap);
		Classroom map = underTest.create(documents);
		assertNotNull(map);
	}
	
	@Test
	public void testGetGroupDetails() {
		Map<String, Object> rolesMap = new HashMap<>();
		when(groupServices.getGroupDetails(any(String.class),any(String.class),any(String.class),any(String.class),any(String.class),any(String.class))).thenReturn(rolesMap);
		Map<String, Object> map = underTest.getGroupDetails("groupIdentifier", "productModelIdentifier", "orgIdentifier", "activationTimestamp", "deactivationTimestamp", "userName");
		assertNotNull(map);
	}
	
	@Test
	public void testUpdate() {
		Map<String, String> request = new HashMap<>();
	    request.put("classRoomId", "CLASSROOM01");
	    JsonObject documents = gson.toJsonTree(request).getAsJsonObject();
	    
		Map<String, Object> rolesMap = new HashMap<>();
		when(groupServices.updateGroupeNameOrType(any(String.class),any(String.class))).thenReturn(rolesMap);
		
		Classroom map = underTest.update(request, documents);
		assertNotNull(map);
	}
	
	@Test
	public void testFetchOne() {
		Map<String, String> request = new HashMap<>();
	    request.put("classRoomId", "CLASSROOM01");
	    JsonObject documents = gson.toJsonTree(request).getAsJsonObject();
	    
		Classroom map = underTest.fetchOne(documents);
		assertNull(map);
	}

	@Test
	public void testFetchMultiple() {
		List<String> ids = new ArrayList<>();
		List<Classroom> map = underTest.fetchMultiple(ids);
		assertTrue(map.isEmpty());
	}

	@Test
	public void testDelete() {
		Classroom map = underTest.delete("id");
		assertNull(map);
	}
}
