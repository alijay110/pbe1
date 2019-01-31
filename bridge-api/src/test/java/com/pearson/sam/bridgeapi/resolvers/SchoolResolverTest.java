package com.pearson.sam.bridgeapi.resolvers;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.pearson.sam.bridgeapi.config.AuthContext;
import com.pearson.sam.bridgeapi.ibridgeservice.ISchoolBridgeService;
import com.pearson.sam.bridgeapi.model.KeyType;
import com.pearson.sam.bridgeapi.model.School;
import com.pearson.sam.bridgeapi.model.Sort;

import io.leangen.graphql.execution.SortField.Direction;

@RunWith(MockitoJUnitRunner.class)
public class SchoolResolverTest {

	@Mock
	private AuthContext authContex;

	@InjectMocks
	private SchoolResolver underTest;

	@Mock
	private ISchoolBridgeService schoolBridgeService;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testAddSchool() {
		when(schoolBridgeService.create(any(School.class))).thenReturn(new School());
		School result = underTest.addSchool(new School(), authContex);
		Assert.assertNotNull(result);
	}

	@Test
	public void testUpdateSchool() {
		when(schoolBridgeService.update(any(School.class))).thenReturn(new School());
		School result = underTest.updateSchool("schoolId", new School(), authContex);
		Assert.assertNotNull(result);
	}

	@Test
	public void testDeactivateSchool() {
		when(schoolBridgeService.delete(any(School.class))).thenReturn(new School());
		School result = underTest.deactivateSchool("schoolId", authContex);
		Assert.assertNotNull(result);
	}

	@Test
	public void testSchool() {
		when(schoolBridgeService.fetch(any(School.class))).thenReturn(new School());
		School result = underTest.school("schoolId");
		Assert.assertNotNull(result);
	}

	@Test
	public void testGetSchoolDataBySchoolId() {
		when(schoolBridgeService.fetch(any(School.class))).thenReturn(new School());
		School result = underTest.getSchoolDataBySchoolId("schoolId");
		Assert.assertNotNull(result);
	}

	@Test
	public void testSchools() throws IOException {
		List<School> schools = new ArrayList<>();
		schools.add(new School());
		when(schoolBridgeService.fetchMultiple(KeyType.USER_ID, null)).thenReturn(getSchoolslst());
		List<School> result = underTest.schools(authContex);
		Assert.assertNotNull(result);
	}

	@Test
	public void testGetPaginatedSchoolTableData() {
		Sort sort = new Sort();
		sort.setField("schoolId");
		io.leangen.graphql.execution.SortField.Direction d = Direction.ASC;
		sort.setOrder(d);
		Pageable pageable = PageRequest.of(0, 10,
				org.springframework.data.domain.Sort.Direction.valueOf(sort.getOrder().toString()), sort.getField());
		when(schoolBridgeService.pageIt(any(Pageable.class), any(Example.class))).thenReturn(getSchools());
//		Page<School> result = underTest.getPaginatedSchoolTableData(0, 15, new School(), StringMatcher.DEFAULT, sort,
//				authContex);
//		Assert.assertNotNull(result);
	}

	@Test
	public void testGetAllSchoolsIdAndNameByString() {
		Sort sort = new Sort();
		sort.setField("schoolId");
		io.leangen.graphql.execution.SortField.Direction d = Direction.ASC;
		sort.setOrder(d);
		when(schoolBridgeService.pageIt(any(Pageable.class), any(Example.class))).thenReturn(getSchools());
		Page<School> result = underTest.getAllSchoolsIdAndNameByString(0, 15, new School(), StringMatcher.DEFAULT, sort,
				authContex);
		Assert.assertNotNull(result);
	}

	@Test
	public void testGetAllSelectedSchoolsIdAndNameByString() {
		Sort sort = new Sort();
		sort.setField("schoolId");
		io.leangen.graphql.execution.SortField.Direction d = Direction.ASC;
		sort.setOrder(d);

		List<String> schoolIds = new ArrayList<>();
		when(schoolBridgeService.pageIt(any(Pageable.class), anyString(), anyList())).thenReturn(getSchools());

		Page<School> result = underTest.getAllSelectedSchoolsIdAndNameByString(0, 15, schoolIds, "name", sort,
				authContex);
		Assert.assertNotNull(result);
	}

	@Test
	public void testSchoolsByUserId() {
		List<School> schools = new ArrayList<>();
		schools.add(new School());
		when(schoolBridgeService.fetchMultiple(KeyType.USER_ID, null)).thenReturn(getSchoolslst());
		List<School> result = underTest.schoolsByUserId(authContex);
		Assert.assertNotNull(result);
	}

	@Test
	public void testSchoolByCode() {
		when(schoolBridgeService.findSchoolByCode(anyString())).thenReturn(new School());
		School result = underTest.schoolByCode("schoolCode");
		Assert.assertNotNull(result);
	}

	@Test
	public void testSchoolByCodeByTeacherCode() {
		when(schoolBridgeService.findSchoolByTeacherCode(anyString())).thenReturn(new School());
		School result = underTest.schoolByCodeByTeacherCode("teacherCode");
		Assert.assertNotNull(result);
	}

	@Test
	public void testSchoolIdByStudentCode() {
		when(schoolBridgeService.findSchoolByStudentCode(anyString())).thenReturn(new School());
		School result = underTest.schoolIdByStudentCode("studentCode");
		Assert.assertNotNull(result);
	}

	private List<School> getSchoolslst() {
		List<School> school = Arrays.asList(new School(), new School());
		return school;
	}

	private final Page<School> getSchools() {
		Page<School> pageSchool = new PageImpl(getSchoolslst());
		return pageSchool;
	}

	private final School getSchool() {
		School school = new School();
		school.setSchoolCode("schoolCode");
		school.setTeacherCode("teacherCode");
		return school;
	}
}
