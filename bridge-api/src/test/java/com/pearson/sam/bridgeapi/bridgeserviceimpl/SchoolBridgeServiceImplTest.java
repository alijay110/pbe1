package com.pearson.sam.bridgeapi.bridgeserviceimpl;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import com.pearson.sam.bridgeapi.bridgeserviceimpl.SchoolBridgeServiceImpl;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGeneralException;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGraphqlException;
import com.pearson.sam.bridgeapi.iservice.IClassRoomService;
import com.pearson.sam.bridgeapi.iservice.ISchoolService;
import com.pearson.sam.bridgeapi.iservice.IUserService;
import com.pearson.sam.bridgeapi.model.KeyType;
import com.pearson.sam.bridgeapi.model.School;
import com.pearson.sam.bridgeapi.model.SessionUser;
import com.pearson.sam.bridgeapi.model.Sort;
import com.pearson.sam.bridgeapi.model.User;
import com.pearson.sam.bridgeapi.security.ISessionFacade;

import io.leangen.graphql.execution.SortField.Direction;

import java.util.ArrayList;
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

/**
 * School Bridge Service Unit Test Cases
 * 
 * @author VVaijPa
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class SchoolBridgeServiceImplTest {

	@InjectMocks
	private SchoolBridgeServiceImpl underTest = new SchoolBridgeServiceImpl();

	@Mock
	private ISchoolService schoolService;

	@Mock
	private IClassRoomService classRoomService;

	@Mock
	private IUserService userService;

	@Mock
	private ISessionFacade sessionFacade;

	@Test
	public void testCreate() {
		when(schoolService.create(any(School.class))).thenReturn(new School());
		when(sessionFacade.getLoggedInUser(true)).thenReturn(new SessionUser());

		School result = underTest.create(getSchool());
		Assert.assertNotNull(result);
	}
	
	@Test
	public void testCreateCreatedBy() {
		School school = getSchool();
		school.setCreatedBy("PP");
		when(schoolService.create(any(School.class))).thenReturn(school);
		when(sessionFacade.getLoggedInUser(true)).thenReturn(getSessionUser());
		School result = underTest.create(school);
		Assert.assertNotNull(result);
	}

	@Test
	public void testUpdate() {
		when(schoolService.update(any(School.class))).thenReturn(new School());
		when(sessionFacade.getLoggedInUser(true)).thenReturn(new SessionUser());
		School result = underTest.update(getSchool());
		Assert.assertNotNull(result);
	}
	
	@Test
	public void testUpdateUpdatedBy() {
		School school = getSchool();
		school.setUpdatedBy("PP");
		when(schoolService.update(any(School.class))).thenReturn(school);
		when(sessionFacade.getLoggedInUser(true)).thenReturn(getSessionUser());
		School result = underTest.update(school);
		Assert.assertNotNull(result);
	}

	@Test(expected=BridgeApiGraphqlException.class)
	public void testFetchException() {
		School school = getSchool();
		when(schoolService.getSchoolDetails(any(School.class))).thenReturn(school);
		when(userService.countBySchool(anyString())).thenReturn(new Long(1));
		when(classRoomService.countBySchoolId(anyString())).thenThrow(new BridgeApiGeneralException("Data Fetching Exception.."));
		underTest.fetch(school);
	}
	
	@Test
	public void testFetch() {
		when(schoolService.getSchoolDetails(any(School.class))).thenReturn(new School());
		School result = underTest.fetch(getSchool());
		Assert.assertNotNull(result);
	}

	@Test
	public void testDelete() {
		when(schoolService.delete(any(School.class))).thenReturn(new School());
		when(sessionFacade.getLoggedInUser(true)).thenReturn(new SessionUser());
		School result = underTest.delete(getSchool());
		Assert.assertNotNull(result);
	}
	
	@Test
	public void testDeleteUpdatedBy() {
		School school = getSchool();
		school.setUpdatedBy("PP");
		when(schoolService.delete(any(School.class))).thenReturn(school);
		when(sessionFacade.getLoggedInUser(true)).thenReturn(getSessionUser());
		School result = underTest.delete(school);
		Assert.assertNotNull(result);
	}

	@Test
	public void testPageIt() {
		Sort sort = new Sort();
		sort.setField("schoolId");
		io.leangen.graphql.execution.SortField.Direction d = Direction.ASC;
		sort.setOrder(d);
		Pageable pageable = PageRequest.of(0, 10,
				org.springframework.data.domain.Sort.Direction.valueOf(sort.getOrder().toString()), sort.getField());
		Example<School> e = Example.of(new School(),
				ExampleMatcher.matching().withStringMatcher(StringMatcher.DEFAULT));
		when(schoolService.pageIt(pageable, e)).thenReturn(getSchoolPage());
		Page<School> page = underTest.pageIt(pageable, e);
		Assert.assertNotNull(page);
	}

	@Test
	public void testPageItList() {
		Sort sort = new Sort();
		sort.setField("schoolId");
		io.leangen.graphql.execution.SortField.Direction d = Direction.ASC;
		sort.setOrder(d);
		Pageable pageable = PageRequest.of(0, 10,
				org.springframework.data.domain.Sort.Direction.valueOf(sort.getOrder().toString()), sort.getField());
		String schoolName = "SchoolName";
		List<String> schoolList = new ArrayList<>();

		when(schoolService.pageIt(any(Pageable.class), anyString(), anyList())).thenReturn(getSchoolPage());

		Page<School> page = underTest.pageIt(pageable, schoolName, schoolList);
		Assert.assertNotNull(page);
	}

	@Test
	public void testFindSchoolByTeacherCode() {
		when(schoolService.findSchoolByTeacherCode(any(String.class))).thenReturn(new School());
		School result = underTest.findSchoolByTeacherCode("TeacherCode");
		Assert.assertNotNull(result);
	}

	@Test
	public void getFindSchoolByStudentCode() {
		when(schoolService.findSchoolByStudentCode(any(String.class))).thenReturn(new School());
		School result = underTest.findSchoolByStudentCode("StudentCode");
		Assert.assertNotNull(result);
	}
	
	@Test(expected=BridgeApiGraphqlException.class)
	public void getFindSchoolByStudentCodeandValidate() {
		when(schoolService.findSchoolByStudentCode(anyString())).thenReturn(null);
		underTest.findSchoolByStudentCode("StudentCode");
		
	}

	@Test
	public void testgetFindSchoolByCode() {
		when(schoolService.findSchoolByCode(any(String.class))).thenReturn(new School());
		School result = underTest.findSchoolByCode("SChoolCode");
		Assert.assertNotNull(result);
	}

	@Test
	public void testFetchMultiple() {
		List<School> schools = new ArrayList<>();
		List<String> schoolList = new ArrayList<>();
		when(schoolService.fetchMultiple(KeyType.SCHOOL_ID, schoolList)).thenReturn(schools);

		List<School> result = underTest.fetchMultiple(KeyType.SCHOOL_ID, schoolList);
		Assert.assertNotNull(result);

		when(sessionFacade.getLoggedInUser(true)).thenReturn(new SessionUser());
		when(schoolService.fetchMultiple(KeyType.USER_ID, new User().getSchool())).thenReturn(schools);

		List<School> data = underTest.fetchMultiple(KeyType.USER_ID, schoolList);
		Assert.assertNotNull(data);
		
		
		data = underTest.fetchMultiple(KeyType.DEFAULT, schoolList);
		Assert.assertNull(data);
	}

	private final School getSchool() {
		School school = new School();
		school.setSchoolId("schoolId");
		school.setTeacherCode("teacherCode");
		return school;
	}
	
	private final SessionUser getSessionUser() {
		SessionUser sessionuser = new SessionUser();
		sessionuser.setUid("chandraBoseCC1");
		return sessionuser;
	}

	private Page<School> getSchoolPage() {
		School school = new School();
		school.setSchoolId("schoolId");
		List<School> listSchool = new ArrayList<School>();
		listSchool.add(school);
		Page<School> result = new PageImpl<School>(listSchool);
		return result;
	}

}
