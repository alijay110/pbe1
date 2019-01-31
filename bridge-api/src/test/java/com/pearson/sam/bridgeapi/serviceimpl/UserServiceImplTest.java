package com.pearson.sam.bridgeapi.serviceimpl;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import com.pearson.sam.bridgeapi.BridgeApiApplicationTest;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGraphqlException;
import com.pearson.sam.bridgeapi.model.Sort;
import com.pearson.sam.bridgeapi.model.User;
import com.pearson.sam.bridgeapi.repository.UserRepository;
import com.pearson.sam.bridgeapi.samclient.IUserSamClient;
import com.pearson.sam.bridgeapi.serviceimpl.UserServiceImpl;

import io.leangen.graphql.execution.SortField.Direction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

@SuppressWarnings({ "rawtypes", "unchecked" })
@EnableAutoConfiguration
@RunWith(SpringRunner.class)
public class UserServiceImplTest extends BridgeApiApplicationTest {

	@Mock
	private UserRepository userRepository;

	@Value("${SPEC_FILE}")
	private String specJsonFile;

	@InjectMocks
	private UserServiceImpl underTest;

	@Mock
	private IUserSamClient userSamClient;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		ReflectionTestUtils.setField(underTest, "specJsonFile", specJsonFile);
	}

//	@Test
//	public void testAddUser() {
//		when(userRepository.existsByEmail(anyString())).thenReturn(false);
//		when(userSamClient.checkUserIdentityAvailability(anyString())).thenReturn(true);
//		when(userSamClient.createUser(any())).thenReturn(getUserMap());
//		Map<String, Object> responseMap = underTest.create(getUser(), getUserMap());
//		Assert.assertNotNull(responseMap);
//	}
//
//	@Test(expected = BridgeApiGraphqlException.class)
//	public void testUpdateUserWithNoUserId() {
//		when(userRepository.findByUid(any())).thenReturn(getUser());
//		when(userSamClient.updateUser(any(), any())).thenReturn(getUserMap());
//		Map<String, Object> responseMap = underTest.updateUser(getUser(), getUserMap());
//		Assert.assertNotNull(responseMap);
//	}
//
//	@Test
//	public void testUpdateUser() {
//		User user = getUser();
//		user.setUid("mlsreekanth");
//		when(userRepository.findByUid(anyString())).thenReturn(user);
//		when(userSamClient.updateUser(any(), any())).thenReturn(getUserMap());
//		Map<String, Object> responseMap = underTest.updateUser(user, getUserMap());
//		Assert.assertNotNull(responseMap);
//	}

	@Test
	public void testGetUserWithUserName() {
		when(userRepository.findByIdentifier(any())).thenReturn(getUser());
		when(userRepository.findByUid(any())).thenReturn(getUser());
		when(userSamClient.getUser(any())).thenReturn(getUserMap());
		User user = underTest.getUser(getUser());
		Assert.assertNotNull(user);
	}

	@Test
	public void testGetUserWithOutUserName() {
		User user = new User();
		user.setUid("mlsreekanth");
		user.setEmail("abc@def.com");
		when(userRepository.findByIdentifier(anyString())).thenReturn(user);
		when(userRepository.findByUid(any())).thenReturn(user);
		when(userSamClient.getUser(any())).thenReturn(getUserMap());
		user = underTest.getUser(user);
		Assert.assertNotNull(user);
	}

	@Test(expected = BridgeApiGraphqlException.class)
	public void testGetUserWithNoUserNameAndUid() {
		User user = new User();
		user.setEmail("abc@def.com");
		when(userRepository.findByIdentifier(anyString())).thenReturn(user);
		when(userRepository.findByUid(any())).thenReturn(user);
		when(userSamClient.getUser(any())).thenReturn(getUserMap());
		user = underTest.getUser(user);
		Assert.assertNotNull(user);
	}

	@Test
	public void testGetUserName() {
		when(userRepository.findByIdentifier(anyString())).thenReturn(getUser());
		User user = underTest.getUserName("UserName");
		Assert.assertNotNull(user);
	}

	@Test
	public void testCountBySchool() {
		when(userRepository.countBySchool(anyString())).thenReturn(10L);
		Long value = underTest.countBySchool("SCH00001");
		Assert.assertNotNull(value);
	}

	@Test
	public void testUpdateUserSchoolId() {
		when(userRepository.findByUid(any())).thenReturn(getUser());
		when(userRepository.save(any(User.class))).thenReturn(getUser());
		User user = underTest.updateUserSchoolId(getUser());
		Assert.assertNotNull(user);
	}

	@Test
	public void testUpdateSubjectPreference() {
		when(userRepository.findByUid(any())).thenReturn(getUser());
		when(userRepository.save(any(User.class))).thenReturn(getUser());
		List<String> lstSubject = Arrays.asList("MATHS", "SCIENCE");
		User user = underTest.updateSubjectPreference(getUser());
		Assert.assertNotNull(user);
	}

	@Test
	public void testPageIt() {		
		Sort sort = new Sort();
		sort.setField("organizationId");
		io.leangen.graphql.execution.SortField.Direction d = Direction.ASC;
		sort.setOrder(d);
		Pageable pageable = PageRequest.of(0, 10, org.springframework.data.domain.Sort.Direction.valueOf(sort.getOrder().toString()), sort.getField());
		Example<User> e = Example.of(new User(), ExampleMatcher.matching().withStringMatcher(StringMatcher.DEFAULT));
		when(userRepository.findAll(any(Example.class),any(Pageable.class))).thenReturn(getUserPage());
		Page<User> pageUser = underTest.pageIt(pageable,e);
		Assert.assertNotNull(pageUser);
		
	}

	private User getUser() {
		User user = new User();
		user.setUserName("userName");
		user.setEmail("abc@def.com");
		List<String> lstSchool = Arrays.asList("SCH00001", "SCH00002");
		List<String> lstSubject = Arrays.asList("MATHS", "SCIENCE");
		user.setSchool(lstSchool);
		user.setSubjectPreference(lstSubject);
		return user;
	}

	private Map<String, Object> getUserMap() {
		Map<String, Object> userMap = new HashMap<String, Object>();
		userMap.put("userName", "mlsreekanth");
		userMap.put("emailAddress", "test@test.in");
		userMap.put("uid", "mlsreekanth");
		userMap.put("userFullName", "mlsreekanth");
		userMap.put("status", "ACTIVE");
		return userMap;
	}

	private Page<User> getUserPage() {
		User user = new User();
		user.setUid("22233444KKLL");
		List<User> listUser = Arrays.asList(user);
		Page<User> pageUser = new PageImpl<User>(listUser);
		return pageUser;
	}

}
