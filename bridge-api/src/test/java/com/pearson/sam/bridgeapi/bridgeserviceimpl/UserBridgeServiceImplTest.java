package com.pearson.sam.bridgeapi.bridgeserviceimpl;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anySet;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import com.pearson.sam.bridgeapi.BridgeApiApplicationTest;
import com.pearson.sam.bridgeapi.iservice.IAuthService;
import com.pearson.sam.bridgeapi.iservice.IUserService;
import com.pearson.sam.bridgeapi.model.Product;
import com.pearson.sam.bridgeapi.model.SessionUser;
import com.pearson.sam.bridgeapi.model.Sort;
import com.pearson.sam.bridgeapi.model.User;
import com.pearson.sam.bridgeapi.repository.LicenceRespository;
import com.pearson.sam.bridgeapi.repository.ProductRepository;
import com.pearson.sam.bridgeapi.repository.UserRepository;
import com.pearson.sam.bridgeapi.samclient.AccessCodeSamClient;
import com.pearson.sam.bridgeapi.security.ISessionFacade;

import graphql.GraphQLException;

import io.leangen.graphql.execution.SortField.Direction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
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
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

@EnableAutoConfiguration
@RunWith(SpringRunner.class)
public class UserBridgeServiceImplTest extends BridgeApiApplicationTest {

  @Value("${SPEC_FILE}")
  String specJsonFile;

  @InjectMocks
  private UserBridgeServiceImpl underTest = new UserBridgeServiceImpl();

  @Mock
  private IUserService userService;

  @Mock
  private IAuthService authService;

  @Mock
  private AccessCodeSamClient accessCodeSamClient;

  @Mock
  private UserRepository userRepository;

  @Mock
  private ProductRepository productRepository;

  @Mock
  protected ISessionFacade sessionFacade;

  @Mock
  MongoOperations mongo;

  @Mock
  private LicenceRespository licenceRespository;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    ReflectionTestUtils.setField(underTest, "specJsonFile", specJsonFile);
  }

  // @Test
  // public void testGetLoggedInUser() {
  // when(sessionFacade.getLoggedInUser(true)).thenReturn(new SessionUser());
  // SessionUser result = underTest.getLoggedInUser(true);
  // Assert.assertNotNull(result);
  // }
  //
  // @Test
  // public void testCreateUser() {
  // when(userService.create(any(User.class), anyMap())).thenReturn(getUserMap());
  // when(authService.updateUserrole(anyString(), anyString())).thenReturn(getRolesMap());
  // when(userService.saveIntoMongoAndGetMergedData(anyMap(), anyMap(),
  // anyString())).thenReturn(getUser());
  // when(userService.getUser(any(User.class))).thenReturn(getUser());
  // User result = underTest.createUser(getUser());
  // Assert.assertNotNull(result);
  // }

  @Test
  public void testGetUserById() {
    Set<String> roles = new HashSet<>();
    roles.add("teacher");
    roles.add("student");
    List<Product> prodList = Arrays.asList(getProduct(), getProduct());
    when(userService.getUser(any(User.class))).thenReturn(getUser());
    when(authService.getRoles(anyString())).thenReturn(roles);
    when(accessCodeSamClient.getAllSubscriptionDetailsofUser(anyString())).thenReturn(getUserMap());
    when(productRepository.findAllByProductIdIn(anySet())).thenReturn(prodList);
    User user = underTest.getUserById("mlsreekanth");
    Assert.assertNotNull(user);

  }

  @Test
  public void testGetUserByUserName() {
    when(userService.getUserName(anyString())).thenReturn(getUser());
    Set<String> roles = new HashSet<>();
    roles.add("teacher");
    roles.add("student");
    when(userService.getUser(any(User.class))).thenReturn(getUser());
    when(authService.getRoles(anyString())).thenReturn(roles);
    when(accessCodeSamClient.getAllSubscriptionDetailsofUser(anyString())).thenReturn(getUserMap());
    User user = underTest.getUserByUserName("mlsreekanth");
    Assert.assertNotNull(user);
  }

  // @Test
  // public void testUpdateUser() {
  // when(authService.updateUserrole(anyString(), anyString())).thenReturn(getUserMap());
  // when(userService.updateUser(any(User.class), anyMap())).thenReturn(getUserMap());
  // when(userService.saveIntoMongoAndGetMergedData(anyMap(), anyMap(),
  // anyString())).thenReturn(getUser());
  // when(userService.getUser(any(User.class))).thenReturn(getUser());
  // User user = underTest.updateUser(getUser());
  // Assert.assertNotNull(user);
  // }

  @Test
  public void testUpdateUserSchoolId() {
    when(userService.updateUserSchoolId(any(User.class))).thenReturn(new User());
    User result = underTest.updateUserSchoolId(getUser());
    Assert.assertNotNull(result);
  }

  @Test
  public void testUpdateSubjectPreference() {
    when(userService.updateSubjectPreference(any(User.class))).thenReturn(new User());
    User result = underTest.updateSubjectPreference(getUser());
    Assert.assertNotNull(result);
  }

  @Test
  public void testUpdateMultipleUsersStatusById() {
    List<String> userIdList = new ArrayList<String>();
    underTest.updateMultipleUsersStatusById(userIdList, "ACTIVE");
  }

  @Test
  public void testCheckAvailability() {
    when(userService.checkAvailability(any(String.class))).thenReturn(true);
    Boolean result = underTest.checkAvailability("vvaijpa");
    Assert.assertNotNull(result);
  }

  @Test
  public void testChangePassword() {
    when(userService.changePassword(any(User.class))).thenReturn(new User());
    User result = underTest.changePassword(getUser());
    Assert.assertNotNull(result);
  }

  @Test(expected = GraphQLException.class)
  public void testPageItthrowException() {
    Set<String> sessionRoles = new HashSet<>();
    sessionRoles.add("teacher");
    sessionRoles.add("Admin");
    sessionRoles.add("Student");
    SessionUser sessionUser = new SessionUser();
    sessionUser.setRoles(sessionRoles);
    when(sessionFacade.getLoggedInUser(true)).thenReturn(sessionUser);
    Sort sort = new Sort();
    sort.setField("userId");
    io.leangen.graphql.execution.SortField.Direction d = Direction.ASC;
    sort.setOrder(d);
    Pageable pageable = PageRequest.of(0, 2,
        org.springframework.data.domain.Sort.Direction.valueOf(sort.getOrder().toString()),
        sort.getField());
    Example<User> e = Example.of(new User(),
        ExampleMatcher.matching().withStringMatcher(StringMatcher.DEFAULT));

    when((userService.pageIt(any(Pageable.class), any(Example.class)))).thenReturn(getUserPage());
    Page<User> user = underTest.pageIt(pageable, e);
  }

  @Test
  public void testPageItWithOutRoles() {
    List<String> lstSchool = Arrays.asList("SCH00001", "SCH00002");
    List<String> lstSubject = Arrays.asList("MATHS", "SCIENCE");

    Set<String> sessionRoles = new HashSet<>();
    sessionRoles.add("Admin");
    sessionRoles.add("Student");
    SessionUser sessionUser = new SessionUser();
    sessionUser.setRoles(sessionRoles);
    sessionUser.setSchool(lstSchool);
    when(sessionFacade.getLoggedInUser(true)).thenReturn(sessionUser);
    Sort sort = new Sort();
    sort.setField("userId");
    io.leangen.graphql.execution.SortField.Direction d = Direction.ASC;
    sort.setOrder(d);
    Pageable pageable = PageRequest.of(0, 2,
        org.springframework.data.domain.Sort.Direction.valueOf(sort.getOrder().toString()),
        sort.getField());
    Example<User> e = Example.of(new User(),
        ExampleMatcher.matching().withStringMatcher(StringMatcher.DEFAULT));

    when((userService.pageIt(any(Pageable.class), any(Example.class)))).thenReturn(getUserPage());
    Page<User> user = underTest.pageIt(pageable, e);
    Assert.assertNotNull(user);
  }

  @Test
  public void testPageIt() {
    List<String> lstSchool = Arrays.asList("SCH00001", "SCH00002");
    List<String> lstSubject = Arrays.asList("MATHS", "SCIENCE");

    Set<String> sessionRoles = new HashSet<>();
    sessionRoles.add("teacher");
    sessionRoles.add("Admin");
    sessionRoles.add("Student");
    SessionUser sessionUser = new SessionUser();
    sessionUser.setRoles(sessionRoles);
    sessionUser.setSchool(lstSchool);
    when(sessionFacade.getLoggedInUser(true)).thenReturn(sessionUser);
    Sort sort = new Sort();
    sort.setField("userId");
    io.leangen.graphql.execution.SortField.Direction d = Direction.ASC;
    sort.setOrder(d);
    Pageable pageable = PageRequest.of(0, 2,
        org.springframework.data.domain.Sort.Direction.valueOf(sort.getOrder().toString()),
        sort.getField());
    Example<User> e = Example.of(new User(),
        ExampleMatcher.matching().withStringMatcher(StringMatcher.DEFAULT));

    when((userService.pageIt(any(Pageable.class), any(Example.class)))).thenReturn(getUserPage());
    Page<User> user = underTest.pageIt(pageable, e);
    Assert.assertNotNull(user);
  }

  // @Test
  // public void testLoadUser() {
  // Set<String> roles = new HashSet<>();
  // roles.add("teacher");
  // roles.add("student");
  // List<Product> prodList = Arrays.asList(getProduct(),getProduct());
  // when(userService.getUser(any(User.class))).thenReturn(getUser());
  // when(authService.getRoles(anyString())).thenReturn(roles);
  // when(accessCodeSamClient.getAllSubscriptionDetailsofUser(anyString())).thenReturn(getUserMap());
  // when(productRepository.findAllByProductIdIn(anySet())).thenReturn(prodList);
  // User user = underTest.loadUser(getUser());
  // Assert.assertNotNull(user);
  // }

  private Page<User> getUserPage() {
    User user = new User();
    user.setUserName("mlsreekanth");
    List<User> listUser = new ArrayList<User>();
    listUser.add(user);
    Page<User> result = new PageImpl<User>(listUser);
    return result;
  }

  private User getUser() {
    User user = new User();
    user.setUid("mlsreekanth");
    user.setUserName("mlsreekanth");
    user.setPasswd("Test123$");
    user.setFullName("mlsreekanth");
    user.setEmail("mlsreekanth@gmail.com");
    user.setStatus("active");
    user.setIdentifier("mlsreekanth");
    List<String> lstSchool = Arrays.asList("SCH00001", "SCH00002");
    List<String> lstSubject = Arrays.asList("MATHS", "SCIENCE");
    user.setSchool(lstSchool);
    user.setSubjectPreference(lstSubject);
    Set<String> roles = new HashSet<>();
    roles.add("teacher");
    roles.add("student");
    user.setRoles(roles);
    return user;
  }

  private Map<String, Object> getUserMap() {
    Map<String, Object> userMap = new HashMap<String, Object>();
    userMap.put("userName", "mlsreekanth");
    userMap.put("email", "test@test.in");
    userMap.put("uid", "test@test.in");
    userMap.put("email", "test@test.in");
    return userMap;
  }

  private Map<String, Object> getRolesMap() {
    Map<String, Object> rolesMap = new HashMap<String, Object>();
    rolesMap.put("userName", "roleBody");
    return rolesMap;
  }

  private final Product getProduct() {
    Product product = new Product();
    product.setProductId("PP");
    product.setProductName("MyProduct");
    product.setId("333333");
    product.setCourseUrl("google.com");
    product.setProductModel("PP");
    product.setIsTeacherProduct(true);
    return product;
  }

}
