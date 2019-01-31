package com.pearson.sam.bridgeapi.samclient;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;

import com.pearson.sam.bridgeapi.model.User;
import com.pearson.sam.bridgeapi.samservices.IdentityServices;

@RunWith(MockitoJUnitRunner.class)
public class UserSamClientImplTest {

	@Mock
	IdentityServices identityServices;
	
	@InjectMocks
	private UserSamClientImpl underTest;
	
	@Test
	public void testcreateUser() {
		when(identityServices.createUser(anyString())).thenReturn(getUserMap());
		Map<String,Object> usermap = underTest.createUser("ppteam");
		assertNotNull(usermap);
	}

	@Test
	public void updateUser() {
		when(identityServices.updateUser(anyString(),anyString())).thenReturn(getUserMap());
		Map<String,Object> usermap = underTest.updateUser("ppteam","userdetails");
		assertNotNull(usermap);
	}

	@Test
	public void testcheckUserIdentityAvailability() {
		 when(identityServices.checkUserIdentityAvailability(anyString())).thenReturn(true);
		 assertTrue(underTest.checkUserIdentityAvailability("ppteam1"));
	}

	@Test
	public void testgetUser() {
		when(identityServices.getUser(anyString())).thenReturn(getUserMap());
		assertNotNull(underTest.getUser("ppteam"));
	}

	@Test
	public void testchangePassword() {
		when(identityServices.changePassword(anyString(),anyString())).thenReturn(getUserMap());
		assertNotNull(underTest.changePassword("ppteam","newpasssword"));
	}

	private final Map<String,Object> getUserMap(){
		Map<String,Object> usermap = new HashMap<>();
		usermap.put("ppteam", new Object());
		return usermap;
	}
	
}
