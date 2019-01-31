package com.pearson.sam.bridgeapi.serviceimpl;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import com.google.gson.JsonObject;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGeneralException;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGraphqlException;
import com.pearson.sam.bridgeapi.model.KeyType;
import com.pearson.sam.bridgeapi.model.School;
import com.pearson.sam.bridgeapi.model.SessionUser;
import com.pearson.sam.bridgeapi.model.Sort;
import com.pearson.sam.bridgeapi.repository.SchoolRepository;
import com.pearson.sam.bridgeapi.samclient.IOrganisationSamClient;
import com.pearson.sam.bridgeapi.security.ISessionFacade;
import com.pearson.sam.bridgeapi.serviceimpl.SchoolServiceImpl;

import io.leangen.graphql.execution.SortField.Direction;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(SpringRunner.class)
@TestPropertySource("classpath:test-application.properties")
public class SchoolServiceImplTest {

	static {
		System.setProperty("product", "PP");
		System.setProperty("env", "Local");
		
	}
	
	@Value("${SPEC_FILE}")
	private String specJsonFile;

	@Mock
	private SchoolRepository schoolRepository;

	@Mock
	private IOrganisationSamClient<School> schoolIntegration;
	
	@Mock
	private ISessionFacade sessionFacade;
	
	@InjectMocks
	private SchoolServiceImpl underTest;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		ReflectionTestUtils.setField(underTest, "specJsonFile", specJsonFile);
		
	}
	
	@Test
	public void testcreateschool() {
		School school = getSchool();
		when(schoolIntegration.create(any(JsonObject.class))).thenReturn(school);
		school = underTest.create(school);
		assertNotNull(school);
	}
	
	@Test(expected=BridgeApiGeneralException.class)
	public void testcreateschoolemptyschoolName() {
		School school = getSchool();
		school.setName("");
		when(schoolIntegration.create(any(JsonObject.class))).thenThrow(new BridgeApiGeneralException("Exception in Creating access codes"));
		underTest.create(school);
	}
	
	
	@Test
	public void testcreategenerateSchoolNameFirstandLast() {
		School school = getSchool();
		school.setSchoolId("");
		school.setName("Pearson Learning");
		when(schoolIntegration.create(any(JsonObject.class))).thenReturn(school);
		school = underTest.create(school);
		assertNotNull(school);
	}
	
	@Test
	public void testcreategenerateSchoolFirstnLastName() {
		School school = getSchool();
		school.setSchoolId("");
		school.setName(" pe");
		when(schoolIntegration.create(any(JsonObject.class))).thenReturn(school);
		school = underTest.create(school); 
		assertNotNull(school);
		
		school.setSchoolId("");
		school.setName(" p  p ");
		when(schoolIntegration.create(any(JsonObject.class))).thenReturn(school);
		school = underTest.create(school);
		assertNotNull(school);
		
		
		school.setSchoolId("");
		school.setName(" p p ");
		when(schoolIntegration.create(any(JsonObject.class))).thenReturn(school);
		school = underTest.create(school);
		assertNotNull(school);
		
		school.setSchoolId("");
		school.setName(" pp p ");
		when(schoolIntegration.create(any(JsonObject.class))).thenReturn(school);
		school = underTest.create(school);
		assertNotNull(school);
		
		school.setSchoolId("");
		school.setName(" pp  p ");
		when(schoolIntegration.create(any(JsonObject.class))).thenReturn(school);
		school = underTest.create(school);
		assertNotNull(school);
	}
	
	@Test
	public void testcreategenerateSchoolNamefixedFirstNameLength() {
		School school = getSchool();
		school.setSchoolId("");
		school.setName(" p  e  a  r  s  o  n");
		when(schoolIntegration.create(any(JsonObject.class))).thenReturn(school);
		school = underTest.create(school);
		assertNotNull(school);
	}
	
	
	
	@Test
	public void testcreategenerateSchoolNamefixedFirstNameLengthOne() {
		School school = getSchool();
		school.setSchoolId("");
		school.setName(" s  ");
		when(schoolIntegration.create(any(JsonObject.class))).thenReturn(school);
		school = underTest.create(school);
		assertNotNull(school);
	}
	
	@Test
	public void testcreateschoolemptyschoolId() {
		School school = getSchool();
		school.setSchoolId("");
		when(schoolIntegration.create(any(JsonObject.class))).thenReturn(school);
		school = underTest.create(school);
		assertNotNull(school);
	}
	
	@Test
	public void testcreateschoolemptyCreatedOn() {
		School school = getSchool();
		school.setCreatedOn("");
		when(schoolIntegration.create(any(JsonObject.class))).thenReturn(school);
		school = underTest.create(school);
		String createdon = school.getCreatedOn();
		assertNotNull(createdon);
		assertNotNull(school);
	}
	
	@Test
	public void testcreateschoolemptyProductIdentifier() {
		School school = getSchool();
		school.setProductModelIdentifier("");;
		when(schoolIntegration.create(any(JsonObject.class))).thenReturn(school);
		school = underTest.create(school);
		String productModelIdentifier = school.getProductModelIdentifier();
		assertNotNull(productModelIdentifier);
		assertNotNull(school);
		
		school.setProductModelIdentifier("PP");;
		when(schoolIntegration.create(any(JsonObject.class))).thenReturn(school);
		school = underTest.create(school);
		productModelIdentifier = school.getProductModelIdentifier();
		assertNotNull(productModelIdentifier);
		assertNotNull(school);
		
	}
	
	@Test
	public void testcreateschoolProductIdentifier() {
		School school = getSchool();
		school.setProductModelIdentifier(null);;
		when(schoolIntegration.create(any(JsonObject.class))).thenReturn(school);
		school = underTest.create(school);
		String productModelIdentifier = school.getProductModelIdentifier();
		assertNotNull(productModelIdentifier);
		assertNotNull(school);
	}
	
	@Test
	public void testUpdate() {
		School school = getSchool();
		when(schoolIntegration.update(Matchers.<Map<String,String>>any(),any(JsonObject.class))).thenReturn(school);
		when(schoolRepository.findBySchoolId(anyString())).thenReturn(school);
		when(sessionFacade.getLoggedInUser(true)).thenReturn(getSessionUser());
		when(schoolIntegration.fetchOne(any(JsonObject.class))).thenReturn(school);
		school = underTest.update(school);
		assertNotNull(school);
	}
	
	@Test
	public void testfindSchoolById() {
		School school = getSchool();
		when(schoolRepository.findBySchoolId(anyString())).thenReturn(school);
		school = underTest.findSchoolById("anyschoolid");
		assertNotNull(school); 
	}
	
	@Test
	public void testfetchandHandleException() {
		School school = getSchool();
		when(schoolIntegration.fetchOne(any(JsonObject.class))).thenThrow(new BridgeApiGeneralException("Exception in Fetching.."));
		when(schoolRepository.findBySchoolId(anyString())).thenReturn(school);
		school = underTest.getSchoolDetails(school);
		assertNull(school);
	}

	@Test
	public void testfetch() {
		School school = getSchool();
		when(schoolIntegration.fetchOne(any(JsonObject.class))).thenReturn(school);
		when(schoolRepository.findBySchoolId(anyString())).thenReturn(school);
		school = underTest.getSchoolDetails(school);
		assertNotNull(school);
	}
	
	@Test
	public void testfetchMultipleSchoolId() {
		when(schoolRepository.findAll()).thenReturn(asList(getSchool()));
		when(schoolRepository.findAllBySchoolIdIn(Matchers.<List<String>>any())).thenReturn(asList(getSchool()));
		List<School> schoollist = underTest.fetchMultiple(KeyType.SCHOOL_ID, asList("School14112018"));
		assertNotNull(schoollist);
	}
	
	@Test
	public void testfetchMultipleUserId() {
		when(schoolRepository.findAll()).thenReturn(asList(getSchool()));
		when(schoolRepository.findAllBySchoolIdIn(Matchers.<List<String>>any())).thenReturn(asList(getSchool()));
		List<School> schoollist = underTest.fetchMultiple(KeyType.USER_ID, asList("School14112018"));
		assertNotNull(schoollist);
	}
	
	@Test
	public void testfetchDefault() {
		when(schoolRepository.findAll()).thenReturn(asList(getSchool()));
		when(schoolRepository.findAllBySchoolIdIn(Matchers.<List<String>>any())).thenReturn(asList(getSchool()));
		List<School> schoollist = underTest.fetchMultiple(KeyType.DEFAULT, asList("School14112018"));
		assertNull(schoollist);
	}
	
	@Test
	public void testpageItfindAll() {
		Sort sort = new Sort();
		sort.setField("schoolId");
		io.leangen.graphql.execution.SortField.Direction direction = Direction.ASC;
		sort.setOrder(direction);
		Pageable pageable = PageRequest.of(0, 10,
				org.springframework.data.domain.Sort.Direction.valueOf(sort.getOrder().toString()), sort.getField());
		Example<School> e = Example.of(new School(),
				ExampleMatcher.matching().withStringMatcher(StringMatcher.DEFAULT));
		//when(schoolRepository.findAll(Matchers.<Example<School>>any(), any(Pageable.class))).thenReturn(getPageOfSchoolList());
		Page<School> pageofaccesscodeshistory = underTest.pageIt(pageable,e);
		assertNotNull(pageofaccesscodeshistory);
	}
	
	@Test
	public void testpageIt() {
		Sort sort = new Sort();
		sort.setField("schoolId");
		io.leangen.graphql.execution.SortField.Direction direction = Direction.ASC;
		sort.setOrder(direction);
		Pageable pageable = PageRequest.of(0, 10,
				org.springframework.data.domain.Sort.Direction.valueOf(sort.getOrder().toString()), sort.getField());
		when(schoolRepository.findAllByNameLikeAndSchoolIdIn(anyString(),Matchers.<List<String>>any(), any(Pageable.class))).thenReturn(getPageOfSchoolList());
		Page<School> pageofaccesscodeshistory = underTest.pageIt(pageable,"SchoolPearson",asList("School14112018"));
		assertNotNull(pageofaccesscodeshistory);
	}
	
	@Test
	public void testfindSchoolByStudentCode() {
		School school = getSchool();
		when(schoolRepository.findByStudentCode(anyString())).thenReturn(school);
		school = underTest.findSchoolByStudentCode("anyschoolcode");
		assertNotNull(school);
	}

	@Test
	public void testfindSchoolByTeacherCode() {
		School school = getSchool();
		when(schoolRepository.findByTeacherCode(anyString())).thenReturn(school);
		school = underTest.findSchoolByTeacherCode("anyteachercode");
		assertNotNull(school);
	}
	

	@Test
	public void testfindSchoolByCode() {
		School school = getSchool();
		when(schoolRepository.findBySchoolCode(anyString())).thenReturn(school);
		school = underTest.findSchoolByCode("anyschoolcode");
		assertNotNull(school); 
	}
	
	@Test
	public void testdelete() {
		School school = getSchool();
		when(schoolIntegration.deactivate(anyString(),any(JsonObject.class))).thenReturn(school);
		when(schoolRepository.findBySchoolId(anyString())).thenReturn(school);
		school = underTest.delete(school);
		assertNotNull(school);
	}
	
	@Test(expected=BridgeApiGraphqlException.class)
	public void testdeleteDeactiveException() {
		School school = getSchool();
		when(schoolIntegration.deactivate(anyString(),any(JsonObject.class))).thenThrow(new BridgeApiGeneralException("Exception in deactivating school"));;
		when(schoolRepository.findBySchoolId(anyString())).thenReturn(school);
		underTest.delete(school);
	}
	
	@Test(expected=BridgeApiGraphqlException.class)
	public void testdeleteDeactiveUserNotFoundException() {
		School school = getSchool();
		when(schoolIntegration.deactivate(anyString(),any(JsonObject.class))).thenReturn(school);
		when(schoolRepository.findBySchoolId(anyString())).thenReturn(null);
		underTest.delete(school);
	}
	
	private SessionUser getSessionUser() {
		SessionUser sessionuser = new SessionUser();
		sessionuser.setUid("chandraBoseCC1");
		return sessionuser;
	}
	
	private final School getSchool() {
		School school = new School();
		school.setSchoolId("School14112018");
		school.setName("SchoolPearson");
		school.setCreatedOn("14-11-2018");
		school.setProductModelIdentifier("PP");
		school.setSchoolURL("www.pearsonplacestest.com");
		school.setCreatedBy("createdBy");
		school.setTeacherCode("teacherCode");
		school.setStudentCode("studentCode");
		school.setSchoolCode("schoolCode");
		return school;
	}

	private Page<School> getPageOfSchoolList() {
		Page<School> pageOfSchools = new PageImpl<School>(asList(getSchool()));
		return pageOfSchools;
	}

}
