package com.pearson.sam.bridgeapi.serviceimpl;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGeneralException;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGraphqlException;
import com.pearson.sam.bridgeapi.model.Classroom;
import com.pearson.sam.bridgeapi.model.KeyType;
import com.pearson.sam.bridgeapi.model.Members;
import com.pearson.sam.bridgeapi.model.Sort;
import com.pearson.sam.bridgeapi.repository.ClassRoomRepository;
import com.pearson.sam.bridgeapi.samclient.ClassRoomSamClient;
import com.pearson.sam.bridgeapi.serviceimpl.ClassRoomServiceImpl;
import com.pearson.sam.bridgeapi.util.Utils;

import io.leangen.graphql.execution.SortField.Direction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 * ClassRoom service unit Test Cases
 * 
 * @author VVaijPa
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class ClassRoomServiceImplTest {
	@InjectMocks
	private ClassRoomServiceImpl underTest = new ClassRoomServiceImpl();

	@Mock
	private ClassRoomRepository classRoomRepository;

	@Mock
	private ClassRoomSamClient classRoomSamClient;

	@Mock
	private Classroom classRoom;

	/**
	 * Create the Classroom unit test case
	 * 
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @throws ProcessingException
	 */
	@Test
	public void testCreateClassRoom()
			throws JsonParseException, JsonMappingException, IOException, ProcessingException {
		when(classRoomRepository.insert(any(Classroom.class))).thenReturn(getClassRoom());
		when(classRoomSamClient.create(any())).thenReturn(getClassRoom());

		Classroom response = underTest.create(getClassRoom());
		Assert.assertNotNull(response);
	}
	
	@Test
	public void testCreateClassRoomEmptyClassRoomId() {
		when(Utils.isEmpty(classRoom.getClassroomId())).thenThrow(new BridgeApiGeneralException("ClassRoomId should not be empty"));
		when(classRoomSamClient.create(any())).thenReturn(getClassRoom());
		Classroom classRoomLocal = new Classroom();
		classRoomLocal.setClassroomId(null);
		
		Classroom response = underTest.create(classRoomLocal);
		Assert.assertNotNull(response);
	}

	/**
	 * unit test case for create ClassRoom with SAM throwing some Exception
	 * while creating the Product.
	 */

	@Test
	public void testCreateClassRoomWhenSAMThrowsException() {
		when(classRoomRepository.insert(any(Classroom.class))).thenReturn(getClassRoom());
		when(classRoomSamClient.create(any())).thenThrow(new BridgeApiGeneralException(null));
		doNothing().when(classRoomRepository).deleteByClassroomId("classroomId");
		underTest.create(getClassRoom());
	}

	@Test
	public void testFindClassRoomById() {
		when(classRoomRepository.findByClassroomId(any(String.class))).thenReturn(getClassRoom());

		Map<String, Object> classRoomMap = new HashMap<>();
		when(classRoomSamClient.getGroupDetails(any(String.class), any(String.class), any(String.class),
				any(String.class), any(String.class), any(String.class))).thenReturn(classRoomMap);
		when(classRoomRepository.findByClassroomId(any(String.class))).thenReturn(getClassRoom());
		when(classRoomRepository.findByValidationCode(any(String.class))).thenReturn(getClassRoom());
		Classroom response = underTest.findClassRoomById("classRoomId");
		assertNotNull(response);
	}

	@Test(expected=BridgeApiGraphqlException.class)
	public void testFindClassRoomByWithGroupDetailsException() {
		when(classRoomRepository.findByClassroomId(any(String.class))).thenReturn(getClassRoom());

		when(classRoomSamClient.getGroupDetails(any(String.class), any(String.class), any(String.class),
				any(String.class), any(String.class), any(String.class))).thenThrow(new BridgeApiGeneralException("Test"));
		Classroom response = underTest.findClassRoomById("classRoomId");
		assertNotNull(response);
	}
	
	@Test(expected=BridgeApiGraphqlException.class)
	public void testFindClassRoomByIdWithClassRoomEmpty() {
		Classroom cr = new Classroom();
		cr.setClassroomId(null);
		when(classRoomRepository.findByClassroomId(cr.getClassroomId())).thenReturn(null);
		underTest.findClassRoomById("Id");
	}
	
	@Test
	public void testUpdateClassRoom() throws IOException, ProcessingException {
		when(classRoomRepository.save(any(Classroom.class))).thenReturn(getClassRoom());
		when(classRoomRepository.findByClassroomId(any(String.class))).thenReturn(getClassRoom());
		Classroom response = underTest.update(getClassRoom());
		Assert.assertNotNull(response);
	}

	@Test
	public void testUpdateClassRoom1() throws IOException, ProcessingException {
		when(classRoomRepository.save(any(Classroom.class))).thenReturn(getClassRoom());
		when(classRoomRepository.findByClassroomId(any(String.class))).thenReturn(getClassRoom());
		when(classRoomSamClient.update(anyMap(),any())).thenThrow(new BridgeApiGeneralException("Test"));
		Classroom response = underTest.update(getClassRoom());
		Assert.assertNotNull(response);
	}
	
	@Test(expected=BridgeApiGraphqlException.class)
	public void testUpdateClassRoomWithClassRoomEmptyDB() {
		Classroom cr = new Classroom();
		cr.setClassroomId(null);
		when(classRoomRepository.findByClassroomId(cr.getClassroomId())).thenReturn(null);
		underTest.update(getClassRoom());
	}
	
	@Test
	public void testDeleteClassRoom() throws IOException, ProcessingException {
		doNothing().when(classRoomRepository).delete(any(Classroom.class));
		Classroom response = underTest.delete(getClassRoom());
		Assert.assertNotNull(response);
	}

	@Test
	public void testFetchMultipleClassRooms() throws IOException, ProcessingException {
		List<Classroom> classRoomList = new ArrayList<>();
		classRoomList.add(getClassRoom());
		List<String> ids = new ArrayList<>();
		ids.add("CLASSROOM01");
		ids.add("CLASSROOM02");
		List<Classroom> courseResponse = underTest.fetchMultiple(KeyType.COURSE_ID, ids);
		Assert.assertNotNull(courseResponse);

		List<Classroom> userResponse = underTest.fetchMultiple(KeyType.USER_ID, ids);
		Assert.assertNotNull(userResponse);

		List<Classroom> productResponse = underTest.fetchMultiple(KeyType.PRODUCT_ID, ids);
		Assert.assertNotNull(productResponse);

		List<Classroom> productsResponse = underTest.fetchMultiple(KeyType.MULTIPLE_COURSES, ids);
		Assert.assertNotNull(productsResponse);
	}

	@Test
	public void testPageIt() {
		Sort sort = new Sort();
		sort.setField("classRoomId");
		io.leangen.graphql.execution.SortField.Direction d = Direction.ASC;
		sort.setOrder(d);
		Pageable pageable = PageRequest.of(0, 10,
				org.springframework.data.domain.Sort.Direction.valueOf(sort.getOrder().toString()), sort.getField());
		Example<Classroom> e = Example.of(new Classroom(),
				ExampleMatcher.matching().withStringMatcher(StringMatcher.DEFAULT));
		when((classRoomRepository.findAll(any(), any(Pageable.class)))).thenReturn(getClassRoomPage());
		Page<Classroom> page = underTest.pageIt(pageable, e);
		Assert.assertNotNull(page);

	}

	@Test
	public void testFindClassRoomByValidationCode() {
		when(classRoomRepository.findByValidationCode(any(String.class))).thenReturn(getClassRoom());
		Classroom response = underTest.findClassRoomByValidationCode("validationCode");
		assertNotNull(response);
	}

	@Test
	public void testCountBySchoolId() {
		Long count = 1L;
		when(classRoomRepository.countBySchoolId(any(String.class))).thenReturn(count);
		Long response = underTest.countBySchoolId("schoolId");
		assertNotNull(response);
	}

	private Classroom getClassRoom() {
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
		return classroom;	}

	private Page<Classroom> getClassRoomPage() {
		List<Classroom> listClassRoom = Arrays.asList(getClassRoom(), getClassRoom());
		Page<Classroom> pageClassRoom = new PageImpl<Classroom>(listClassRoom);
		return pageClassRoom;
	}
}
