package com.pearson.sam.bridgeapi.bridgeserviceimpl;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import com.pearson.sam.bridgeapi.bridgeserviceimpl.ClassRoomBridgeServiceImpl;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGraphqlException;
import com.pearson.sam.bridgeapi.iservice.IClassRoomService;
import com.pearson.sam.bridgeapi.iservice.IProductService;
import com.pearson.sam.bridgeapi.iservice.IUserService;
import com.pearson.sam.bridgeapi.model.Classroom;
import com.pearson.sam.bridgeapi.model.KeyType;
import com.pearson.sam.bridgeapi.model.Members;
import com.pearson.sam.bridgeapi.model.SessionUser;
import com.pearson.sam.bridgeapi.model.Sort;
import com.pearson.sam.bridgeapi.model.User;
import com.pearson.sam.bridgeapi.security.ISessionFacade;

import io.leangen.graphql.execution.SortField.Direction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


@RunWith(MockitoJUnitRunner.class)
public class ClassRoomBridgeServiceImplTest {

	@Mock
	private IProductService productService;
	
	@Mock
	private IUserService userService;
	
	@Mock
	private ISessionFacade sessionFacade;
	
	@Mock
	private IClassRoomService classRoomService;
	
	@InjectMocks
	private ClassRoomBridgeServiceImpl underTest;
	
	@Test
	public void testcreate() {
		Classroom classroomtocreate = getClassRoom();
		when(sessionFacade.getLoggedInUser(true)).thenReturn(getSessionUser());
		when(classRoomService.create(any(Classroom.class))).thenReturn(classroomtocreate);
		Classroom classroom = underTest.create(classroomtocreate);
		assertNotNull(classroom);
	}
	
	@Test
	public void testupdate() {
		when(sessionFacade.getLoggedInUser(true)).thenReturn(getSessionUser());
		when(classRoomService.update(any(Classroom.class))).thenReturn(getClassRoom());
		Classroom classroom = underTest.update(getClassRoom());
		assertNotNull(classroom);
	}
	
	@Test
	public void testfetch() {
		Classroom classroom = getClassRoom();
		when(classRoomService.findClassRoomById(anyString())).thenReturn(classroom);
		when(userService.getUser(any(User.class))).thenReturn(getUser());
		classroom = underTest.fetch(classroom);
		assertNotNull(classroom);
	}
	
	@Test
	public void testfetchwithoutProductandmembers() {
		Classroom fetchclassroom = new Classroom();
		fetchclassroom.setClassroomId("C7Z8SF");
		when(classRoomService.findClassRoomById(anyString())).thenReturn(fetchclassroom);
		when(userService.getUser(any(User.class))).thenReturn(getUser());
		fetchclassroom = underTest.fetch(fetchclassroom);
		assertNotNull(fetchclassroom);
		
		fetchclassroom.setProduct(Collections.emptyList());
		fetchclassroom = underTest.fetch(fetchclassroom);
		assertNotNull(fetchclassroom);
		
		fetchclassroom.setMembers(Collections.emptyList());
		fetchclassroom = underTest.fetch(fetchclassroom);
		assertNotNull(fetchclassroom);
		
		
		Members members = new Members();
		members.setStudents(Collections.emptyList());
		members.setTeachers(Collections.emptyList());
		fetchclassroom.setMembers(asList(members));
		
		fetchclassroom = underTest.fetch(fetchclassroom);
		assertNotNull(fetchclassroom);
		
	}
	
	@Test
	public void testfetchUserDetailsGraphqlException() {
		Classroom classroom = getClassRoom();
		when(classRoomService.findClassRoomById("C7Z8SE")).thenReturn(classroom);
		when(userService.getUser(any(User.class))).thenThrow(new BridgeApiGraphqlException("Unable to fetch User for User id"));
		classroom = underTest.fetch(classroom);
		assertNotNull(classroom);
	}
	
	
	@Test(expected = BridgeApiGraphqlException.class)
	public void testfetchClassroomIDNotfoundGraphqlException() {
		Classroom classroom = getClassRoom();
		classroom.setClassroomId("ClassIDNotPresent");
		when(classRoomService.findClassRoomById(anyString())).thenThrow(new BridgeApiGraphqlException("Unable to find the Classroom Id.."));
		when(userService.getUser(any(User.class))).thenReturn(getUser());
		classroom = underTest.fetch(classroom);
	}
	
	@Test(expected = Exception.class)
	public void testfetchClassroomIDNotfoundException() {
		Classroom classroom = getClassRoom();
		classroom.setClassroomId("ClassIDNotPresent");
		when(classRoomService.findClassRoomById(anyString())).thenThrow(new Exception("Unable to find the Classroom Id.."));
		when(userService.getUser(any(User.class))).thenReturn(getUser());
		classroom = underTest.fetch(classroom);
	}
	
	@Test
	public void testfetchMultiple() {
		when(classRoomService.fetchMultiple(any(),any())).thenReturn(asList(getClassRoom()));
		when(sessionFacade.getLoggedInUser(true)).thenReturn(getSessionUser());
		when(classRoomService.findClassRoomById(anyString())).thenReturn(getClassRoom());
		List<Classroom> clasroomlist = underTest.fetchMultiple(KeyType.COURSE_ID, asList("1"));
		assertNotNull(clasroomlist);
		clasroomlist = underTest.fetchMultiple(KeyType.USER_ID, asList("1"));
		assertNotNull(clasroomlist);
		clasroomlist = underTest.fetchMultiple(KeyType.MULTIPLE_COURSES, asList("1"));
		assertNotNull(clasroomlist);
		clasroomlist = underTest.fetchMultiple(KeyType.PRODUCT_ID, asList("1"));
		assertNotNull(clasroomlist);
		
		//TODO BRANCH COVERAGE FOR Switch - DEFAULT IS MISSING since below causing NPE, to be fixed in code
		//clasroomlist = underTest.fetchMultiple(KeyType.DEFAULT, asList("1"));
		//assertNotNull(clasroomlist);
	}
	
	@Test
	public void testdelete() {
		Classroom classroomtodelete = getClassRoom();
		when(classRoomService.findClassRoomById(any())).thenReturn(classroomtodelete);
		when(classRoomService.delete(any())).thenReturn(classroomtodelete);
		Classroom classroom = underTest.delete(classroomtodelete);
		assertNotNull(classroom);
	}
	
	@Test(expected = BridgeApiGraphqlException.class)
	public void testdeleteClassroomIdNotFound() {
		Classroom classroomtodelete = getClassRoom();
		when(classRoomService.findClassRoomById(anyString())).thenReturn(null);
		when(classRoomService.delete(any(Classroom.class))).thenReturn(classroomtodelete);
		underTest.delete(getClassRoom());
	}
	
	@Test
	public void testfindClassRoomByValidationCode() {
		when(classRoomService.findClassRoomByValidationCode(anyString())).thenReturn(getClassRoom());
		Classroom classroom = underTest.findClassRoomByValidationCode(anyString());
		assertNotNull(classroom);
	}
	
	@Test(expected = BridgeApiGraphqlException.class)
	public void testfindClassRoomByValidationCodeNotFound() {
		when(classRoomService.findClassRoomByValidationCode(anyString())).thenReturn(null);
		underTest.findClassRoomByValidationCode(anyString());
	}
	
	@Test
	public void testpageIt() {
		Sort sort = new Sort();
		sort.setField("classRoomId");
		io.leangen.graphql.execution.SortField.Direction d = Direction.ASC;
		sort.setOrder(d);
		Pageable pageable = PageRequest.of(0, 2,
				org.springframework.data.domain.Sort.Direction.valueOf(sort.getOrder().toString()), sort.getField());
		Example<Classroom> e = Example.of(new Classroom(),
								ExampleMatcher.matching().withStringMatcher(StringMatcher.DEFAULT));
		when(classRoomService.pageIt(any(),any())).thenReturn(getClassRoomPage());
		Page<Classroom> page = underTest.pageIt(pageable, e);
		Assert.assertNotNull(page);
	}
	
	private Classroom getClassRoom(){
		Classroom classroom = new Classroom();
		classroom.setClassroomId("C7Z8SE");
		classroom.setProduct(asList("PP2PRODUCT40","PP2PRODUCT41"));
		classroom.setOwner("PP");
		
		List<Members> memberList = new ArrayList<>();

		List<String> studentList = new ArrayList<>();
		studentList.add("Student1");

		List<String> teacherList = new ArrayList<>();
		teacherList.add("Teacher1");

		Members member = new Members();
		member.setStudents(studentList);
		member.setTeachers(teacherList);

		memberList.add(member);
		classroom.setMembers(memberList);
		return classroom;
	}
	
	private Page<Classroom> getClassRoomPage() {
		Classroom classroom1 = getClassRoom();
		Classroom classroom2 = getClassRoom();
		classroom2.setClassroomId("C7Z8SF");
		List<Classroom> listClassRoom = asList(classroom1,classroom2);
		Page<Classroom> pageClassRoom = new PageImpl<Classroom>(listClassRoom);
		return pageClassRoom;
	}
	
	private User getUser() {
		User user = new User();
		user.setUserName("UserName");
		return user;
	}
	private SessionUser getSessionUser() {
		SessionUser sessionuser = new SessionUser();
		sessionuser.setUid("chandraBoseCC1");
		return sessionuser;
	}

}
