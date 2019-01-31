package com.pearson.sam.bridgeapi.resolvers;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.pearson.sam.bridgeapi.config.AuthContext;
import com.pearson.sam.bridgeapi.ibridgeservice.IUserBridgeService;
import com.pearson.sam.bridgeapi.model.SessionUser;
import com.pearson.sam.bridgeapi.model.User;
import com.pearson.sam.bridgeapi.security.ISessionFacade;

import io.leangen.graphql.execution.ResolutionEnvironment;

@RunWith(MockitoJUnitRunner.class)
public class ViewerResolverTest {
	
	@Mock
	private AuthContext authContex;

	@Mock
	private ISessionFacade sessionFacade; 
	
	@Mock
	private IUserBridgeService userBridgeService;
  
    @InjectMocks
	private ViewerResolver underTest = new ViewerResolver();
    
    private ResolutionEnvironment env;
	
	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testGetUser()  {	
		Set<String> sessionRoles = new HashSet<>();
	    sessionRoles.add("Admin");
	    sessionRoles.add("Student");
	    SessionUser sessionUser = new SessionUser();
	    sessionUser.setRoles(sessionRoles);
	    when(sessionFacade.getLoggedInUser(false)).thenReturn(new SessionUser());
		when(userBridgeService.getUserById(anyString())).thenReturn(getUser());
		User user =underTest.getUser("userId", authContex, env);
		Assert.assertNotNull(user);
	}

	@Test
	public void testGetUserById()  {
		when(sessionFacade.getLoggedInUser(false)).thenReturn(new SessionUser());
		when(userBridgeService.getUserById(anyString())).thenReturn(new User());
		User user = underTest.getUserById("userId", authContex, env);
		Assert.assertNotNull(user);
	}	
	
	@Test
	public void testGetProductsByUid() {
		when(sessionFacade.getLoggedInUser(false)).thenReturn(new SessionUser());
		when(userBridgeService.getUserById(anyString())).thenReturn(new User());
		User user = underTest.getProductsByUid("userId", authContex, env);
		Assert.assertNotNull(user);
	}

	@Test
	public void testSignupUser()  {
		when(userBridgeService.createUser(any(User.class))).thenReturn(new User());
		User user = underTest.signupUser(getUser(), authContex, env);
		Assert.assertNotNull(user);
	}
	
	@Test
	public void testUpdateEmailbyUid()  {
		when(userBridgeService.updateUser(any(User.class))).thenReturn(new User());
		User user = underTest.updateEmailbyUid(getUser(), authContex);
		Assert.assertNotNull(user);
	}
	
	@Test
	public void testUpdateTeacher()  {
		when(userBridgeService.updateUser(any(User.class))).thenReturn(new User());
		User user = underTest.updateTeacher("userId", getUser(), authContex);
		Assert.assertNotNull(user);
	}
	
	@Test
	public void testChangePassword() {
		when(userBridgeService.changePassword(any(User.class))).thenReturn(new User());
		User user = underTest.changePassword( new User(), authContex);
		Assert.assertNotNull(user);
	}
	
	@Test
	public void testCheckUserNameAvailability() {
		when(userBridgeService.checkAvailability("mlsreekanth")).thenReturn(true);
		Boolean val = underTest.checkUserNameAvailability("mlsreekanth", authContex, env);
		Assert.assertTrue(val);
	}
	
	@Test
	public void testUpdateViewerFullNameByUserId()  {
		when(userBridgeService.updateUser(any(User.class))).thenReturn(new User());
		User user = underTest.updateViewerFullNameByUserId(getUser(), authContex);
		Assert.assertNotNull(user);
	}
	
	private User getUser(){
		User user  = new User();
		user.setUserName("mlsreekanth");
		user.setEmail("abc@def.com");
		return user;
	}
}
