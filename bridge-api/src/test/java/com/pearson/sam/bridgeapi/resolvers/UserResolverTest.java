package com.pearson.sam.bridgeapi.resolvers;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.pearson.sam.bridgeapi.config.AuthContext;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGraphqlException;
import com.pearson.sam.bridgeapi.ibridgeservice.IUserBridgeService;
import com.pearson.sam.bridgeapi.model.BulkUserOutput;
import com.pearson.sam.bridgeapi.model.SessionUser;
import com.pearson.sam.bridgeapi.model.Sort;
import com.pearson.sam.bridgeapi.model.User;
import com.pearson.sam.bridgeapi.security.ISessionFacade;
import com.pearson.sam.bridgeapi.validators.ModelValidator;

import io.leangen.graphql.execution.ResolutionEnvironment;
import io.leangen.graphql.execution.SortField.Direction;

@RunWith(MockitoJUnitRunner.class)
public class UserResolverTest {

	@Mock
	private AuthContext authContext;

	@Mock
	private ResolutionEnvironment env;

	@InjectMocks
	private UserResolver underTest;

	@Mock
	private ISessionFacade sessionFacade;

	@Mock
	private IUserBridgeService userBridgeService;

	@Mock
	private ModelValidator userValidator;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetUserByUserId() throws IOException, ProcessingException {
		when(sessionFacade.getLoggedInUser(false)).thenReturn(new SessionUser());
		when(userBridgeService.getUserById(anyString())).thenReturn(new User());
		User result = underTest.getUserByUserId("uid", authContext, env);
		Assert.assertNotNull(result);
	}

	@Test
	public void testGetUser() {
		when(sessionFacade.getLoggedInUser(false)).thenReturn(new SessionUser());
		when(userBridgeService.getUserById(anyString())).thenReturn(new User());
		User result = underTest.getUser("userId", authContext, env);
		Assert.assertNotNull(result);
	}

	@Test
	public void testGetPaginatedUser() {
		Sort sort = new Sort();
		sort.setField("uid");
		io.leangen.graphql.execution.SortField.Direction d = Direction.ASC;
		sort.setOrder(d);
		when(userBridgeService.getUserByUserName(anyString())).thenReturn(getUser());
		when(userBridgeService.pageIt(any(Pageable.class), any(Example.class))).thenReturn(getUsers());
		Page<User> user = underTest.getPaginatedUser(0, 15, new User(), StringMatcher.DEFAULT, sort, authContext);
		Assert.assertNotNull(user);

	}

	@Test
	public void testGetPaginatedUserAsc() {
		Sort sort = new Sort();
		sort.setField("uid");
		io.leangen.graphql.execution.SortField.Direction d = Direction.ASC;
		sort.setOrder(d);
		when(userBridgeService.pageIt(any(Pageable.class), any(Example.class))).thenReturn(getUsers());
		Page<User> result = underTest.getPaginatedUserAsc(0, 15, new User(), StringMatcher.DEFAULT, sort, authContext);
		Assert.assertNotNull(result);
	}

	@Test
	public void testGetPaginatedUserDesc() {
		Sort sort = new Sort();
		sort.setField("uid");
		io.leangen.graphql.execution.SortField.Direction d = Direction.DESC;
		sort.setOrder(d);
		when(userBridgeService.pageIt(any(Pageable.class), any(Example.class))).thenReturn(getUsers());
		Page<User> result = underTest.getPaginatedUserDesc(0, 15, new User(), StringMatcher.DEFAULT, sort, authContext);
		Assert.assertNotNull(result);
	}

	@Test
	public void testGetUserByUserName() {
		when(sessionFacade.getLoggedInUser(false)).thenReturn(new SessionUser());
		when(userBridgeService.getUserByUserName(anyString())).thenReturn(new User());
		User result = underTest.getUserByUserName("userName", "passwd", authContext, env);
		Assert.assertNotNull(result);
	}

	@Test
	public void testSignupUser() {
		doNothing().when(userValidator).validateModel(any(User.class), anyString());
		when(sessionFacade.getLoggedInUser(false)).thenReturn(new SessionUser());
		when(userBridgeService.createUser(any(User.class))).thenReturn(getUser());
		User result = underTest.signupUser(new User(), authContext, env);
		Assert.assertNotNull(result);
	}

	@Test
	public void testAddUser() {
		doNothing().when(userValidator).validateModel(any(User.class), anyString());
		when(userBridgeService.createUser(any(User.class))).thenReturn(getUser());
		User result = underTest.addUser(new User(), authContext, env);
		Assert.assertNotNull(result);
	}

	@Test
	public void testUpdateUser() {
		when(userBridgeService.updateUser(any(User.class))).thenReturn(new User());
		User result = underTest.updateUser("Id", new User(), authContext);
		Assert.assertNotNull(result);
	}
	
	@Test(expected=BridgeApiGraphqlException.class)
	public void testUpdateUserCatchException() {
		User user = getUser();
		user.setUid("PP2USER001");
		when(userBridgeService.updateUser(any(User.class))).thenReturn(user);
		underTest.updateUser("Id", user, authContext);
	}

	@Test
	public void testUpdateWithSamStatus() {
		User user = new User();
		user.setStatus("PENDING");
		when(userBridgeService.updateUser(any(User.class))).thenReturn(getUser());
		User result = underTest.updateUser("Id", user, authContext);
		Assert.assertNotNull(result);
	}
	@Test
	public void testUserUpload() {
		List<User> userList = Arrays.asList(getUser(), getUser());
		doNothing().when(userValidator).validateModel(any(User.class), anyString());
		when(sessionFacade.getLoggedInUser(false)).thenReturn(new SessionUser());
		when(userBridgeService.createUser(any(User.class))).thenReturn(new User());
		List<BulkUserOutput> usersList = underTest.userUpload(userList, authContext, env);
		Assert.assertNotNull(usersList);
	}
	
	@Test
	public void testUserUploadWithExceptionBlock() {
		User user = new User();
		user.setUid(null);
		List<User> userList = Arrays.asList(getUser(), null);
		doNothing().when(userValidator).validateModel(any(User.class), anyString());
		when(sessionFacade.getLoggedInUser(false)).thenReturn(new SessionUser());
		when(userBridgeService.createUser(any(User.class))).thenReturn(new User());
		List<BulkUserOutput> usersList = underTest.userUpload(userList, authContext, env);
		Assert.assertNotNull(usersList);
	}

	@Test
	public void testCheckUserNameAvailability() {
		when(userBridgeService.checkAvailability(anyString())).thenReturn(true);
		Boolean result = underTest.checkUserNameAvailability("userName", authContext, env);
		Assert.assertNotNull(result);
	}

	@Test
	public void testGetUserFromSam() {
		when(sessionFacade.getLoggedInUser(false)).thenReturn(new SessionUser());
		when(userBridgeService.getUserByUserName(anyString())).thenReturn(new User());
		User result = underTest.getUserFromSam("userName", authContext, env);
		Assert.assertNotNull(result);
	}

	@Test
	public void testGetUserSchoolByUserId() {
		when(sessionFacade.getLoggedInUser(false)).thenReturn(new SessionUser());
		when(userBridgeService.getUserById(anyString())).thenReturn(new User());
		User result = underTest.getUserSchoolByUserId("uid", authContext, env);
		Assert.assertNotNull(result);
	}

	@Test
	public void testUpdateUserSchoolId() {
		when(userBridgeService.updateUserSchoolId(any(User.class))).thenReturn(new User());
		User result = underTest.updateUserSchoolId(new User(), authContext, env);
		Assert.assertNotNull(result);
	}

	@Test
	public void testUpdateStudentPreferences() {
		when(userBridgeService.updateSubjectPreference(any(User.class))).thenReturn(new User());
		User result = underTest.updateStudentPreferences(new User(), authContext, env);
		Assert.assertNotNull(result);
	}

	@Test
	public void testUpdateMultipleUsersStatusById() {

		List<String> userIdList = new ArrayList<>();
		List<User> users = new ArrayList<>();
		when(userBridgeService.updateMultipleUsersStatusById(anyList(), anyString())).thenReturn(users);
		List<User> result = underTest.updateMultipleUsersStatusById(userIdList, "status", authContext, env);
		Assert.assertNotNull(result);
	}

	private final User getUser() {
		User searchUser = new User();
		searchUser.setUid("mlsreekanth");
		searchUser.setUserName("mlsreekanth");
		searchUser.setPasswd("Test123$");
		searchUser.setFullName("mlsreekanth");
		searchUser.setEmail("mlsreekanth@gmail.com");
		searchUser.setStatus("active");
		searchUser.setIdentifier("mlsreekanth");
		return searchUser;
	}

	private final Page<User> getUsers() {
		List<User> user = Arrays.asList(getUser(), getUser());
		Page<User> pageUser = new PageImpl(user);
		return pageUser;
	}

}
