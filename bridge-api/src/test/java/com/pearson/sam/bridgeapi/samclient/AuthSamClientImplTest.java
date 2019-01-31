package com.pearson.sam.bridgeapi.samclient;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.pearson.sam.bridgeapi.samservices.AuthorizationServices;


@RunWith(MockitoJUnitRunner.class)
public class AuthSamClientImplTest {
	
	@InjectMocks
	private AuthSamClientImpl underTest = new AuthSamClientImpl();
	
	@Mock
	private AuthorizationServices authorizationServices;
	
	@Test
	public void testGetUserrole() {
		Map<String, Object> rolesMap = new HashMap<>();
		when(authorizationServices.getUserrole(any(String.class))).thenReturn(rolesMap);
		Map<String,Object> map = underTest.getUserrole("userName");
		assertNotNull(map);
	}
	
	@Test
	public void testGetRole() {
		Map<String, Object> rolesMap = new HashMap<>();
		when(authorizationServices.getRole(any(String.class))).thenReturn(rolesMap);
		Map<String,Object> map = underTest.getRole("role");
		assertNotNull(map);
	}
	
	@Test
	public void testUpdateUserrole() {
		Map<String, Object> rolesMap = new HashMap<>();
		when(authorizationServices.updateUserrole(any(String.class),any(String.class))).thenReturn(rolesMap);
		Map<String,Object> map = underTest.updateUserrole("userName", "body");
		assertNotNull(map);
	}
	
	@Test
	public void testCreateUserrole() {
		Map<String, Object> rolesMap = new HashMap<>();
		when(authorizationServices.createUserrole(any(String.class))).thenReturn(rolesMap);
		Map<String,Object> map = underTest.createUserrole("userName", "body");
		assertNotNull(map);
	}
	

}
