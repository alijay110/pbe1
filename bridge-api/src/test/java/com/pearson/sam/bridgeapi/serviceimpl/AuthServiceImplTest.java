package com.pearson.sam.bridgeapi.serviceimpl;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import com.pearson.sam.bridgeapi.samclient.IAuthSamClient;
import com.pearson.sam.bridgeapi.serviceimpl.AuthServiceImpl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
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


@RunWith(SpringRunner.class)
@TestPropertySource("classpath:test-application.properties")
public class AuthServiceImplTest {
	
	/*Needed for resolving placeholder*/
	static {
		System.setProperty("product", "PP");
		System.setProperty("env", "Local");
		
	}
	
	@Value("${SPEC_FILE}")
	String specJsonFile;
	
	@Mock
	private IAuthSamClient authSamClient;
	
	@InjectMocks
	private AuthServiceImpl underTest;
	
	
	/*Needed since we are using springrunner*/
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		ReflectionTestUtils.setField(underTest, "specJsonFile", specJsonFile);
	}
	
	
	@Test
	public void testgetRoles() {
		Map<String, Object> rolespresentmap = new LinkedHashMap<>();
		List<String> roleslist = new ArrayList<>();
		roleslist.add("customer-care");
		rolespresentmap.put("roles", roleslist);
		rolespresentmap.put("message", "Dummy message");
		rolespresentmap.put("status", true);
		
		when (authSamClient.getUserrole(anyString())).thenReturn(rolespresentmap);
		Set<String> roles = underTest.getRoles(anyString());
		Assert.assertNotNull(roles);
		
		when (authSamClient.getUserrole(anyString())).thenReturn(null);
		roles = underTest.getRoles(anyString());
		Assert.assertNull(roles);
	}
	
	
	@Test
	public void testgetPermissions() {
		Map<String, Object> rolesnotpresentmap = new HashMap<>();
		when (authSamClient.getRole(anyString())).thenReturn(getRolesMap());
		Set<String> permissions = underTest.getPermissions(anyString());
		Assert.assertNotNull(permissions);
		
		when (authSamClient.getRole(anyString())).thenReturn(rolesnotpresentmap);
		permissions = underTest.getPermissions(anyString());
		Assert.assertNull(permissions);
	}
	
	@Test
	public void testgetPermissionsrolesfromroles() {
		Set<String> testpremission = new HashSet<>();
		testpremission.add("customer-care");
		when (authSamClient.getRole(anyString())).thenReturn(getRolesMap());
		Set<String> permissions = underTest.getPermissions(testpremission);
		Assert.assertNotNull(permissions);
	}
	
	@Test
	public void validateRole() {
		Map<String, Object> rolespresentmap = new HashMap<>();
		List<String> roleslist = new ArrayList<>();
		roleslist.add("customer-care");
		rolespresentmap.put("roles", roleslist);
		rolespresentmap.put("message", "Dummy message");
		rolespresentmap.put("status", true);
		when(authSamClient.getRole(anyString())).thenReturn(getRolesMap());
		underTest.validateRole("customer-care");
	}
	
	@Test
	public void  testupdateUserrole() {
		when(authSamClient.updateUserrole(anyString(),anyString())).thenReturn(getRolesMap());
		Map<String,Object> map = underTest.updateUserrole("userName", "body");
		assertNotNull(map);
	}
	
	private Map<String,Object> getRolesMap() {
		Instant instant = Instant.now();
		LocalDateTime time = LocalDateTime.ofInstant(instant, ZoneId.of("Australia/Queensland"));
		
		LinkedHashMap<String, Object> rolespresentmap = new LinkedHashMap<>();
		rolespresentmap.put("message","Role Retrieved Successfully.");
		rolespresentmap.put("status",Boolean.TRUE);
		
		LinkedHashMap<String, Object> rolesmap = new LinkedHashMap<>();
		rolesmap.put("roleName","customer-care");
		rolesmap.put("displayRoleName","Customer Care");
		rolesmap.put("roleDescription","Customer Care");
		
		LinkedHashMap<String, Object> permissionmap = new LinkedHashMap<>();
		permissionmap.put("permissionName", "defaultOperation");
		permissionmap.put("description", "defaultOperation");
		List<LinkedHashMap<String,Object>> permissionlist = new ArrayList<>();
		permissionlist.add(permissionmap);
		rolesmap.put("permissions",permissionlist);
		rolesmap.put("createdBy","");
		rolesmap.put("createdOn",time.toString());
		rolesmap.put("updatedBy","");
		rolesmap.put("updatedOn","");
		rolesmap.put("identifier","customer-care");
		
		List<LinkedHashMap<String,Object>> roleslist = new ArrayList<>();
		roleslist.add(rolesmap);
		rolespresentmap.put("roles",roleslist);
		return rolespresentmap;
	}
	
	
}
