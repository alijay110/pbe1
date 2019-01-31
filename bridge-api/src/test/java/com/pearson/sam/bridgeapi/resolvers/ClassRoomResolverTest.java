package com.pearson.sam.bridgeapi.resolvers;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
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
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.pearson.sam.bridgeapi.config.AuthContext;
import com.pearson.sam.bridgeapi.ibridgeservice.IClassRoomBridgeService;
import com.pearson.sam.bridgeapi.model.Classroom;
import com.pearson.sam.bridgeapi.model.KeyType;
import com.pearson.sam.bridgeapi.model.Product;
import com.pearson.sam.bridgeapi.model.Sort;

import io.leangen.graphql.execution.SortField.Direction;

/**
 * Class Room Resolver unit tests
 * 
 * @author Param
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class ClassRoomResolverTest {

	@Mock
	private AuthContext authContex;

	@InjectMocks
	private ClassRoomResolver underTest;

	@Mock
	private IClassRoomBridgeService classRoomBridgeService;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testAddClassRoom() throws IOException, ProcessingException {
		when(classRoomBridgeService.create(any(Classroom.class))).thenReturn(new Classroom());
		Classroom result = underTest.addClassRoom(new Classroom());
		Assert.assertNotNull(result);
	}

	@Test
	public void testUpdateClassRoom() throws IOException, ProcessingException {
		when(classRoomBridgeService.update(any(Classroom.class))).thenReturn(new Classroom());
		Classroom result = underTest.updateClassRoom("classRoomId", new Classroom());
		Assert.assertNotNull(result);
	}

	@Test
	public void testRemoveClassRoom() throws IOException, ProcessingException {
		when(classRoomBridgeService.delete(any(Classroom.class))).thenReturn(new Classroom());
		Classroom result = underTest.removeClassRoom("classRoomId");
		Assert.assertNotNull(result);
	}

	@Test
	public void testClassRoom() throws IOException, ProcessingException {
		when(classRoomBridgeService.fetch(any(Classroom.class))).thenReturn(new Classroom());
		Classroom result = underTest.classRoom("classRoomId");
		Assert.assertNotNull(result);
	}

	@Test
	public void testClassRooms() throws IOException, ProcessingException {
		List<Classroom> classRooms = new ArrayList<>();
		classRooms.add(new Classroom());
		when(classRoomBridgeService.fetchMultiple(KeyType.USER_ID, null)).thenReturn(classRooms);
		List<Classroom> result = underTest.classRooms();
		Assert.assertNotNull(result);
	}

	@Test
	public void testClassRoomsByProductId() {
		List<Classroom> classRooms = new ArrayList<>();
		classRooms.add(new Classroom());

		ArrayList<String> products = new ArrayList<>();
		products.add("productId");

		when(classRoomBridgeService.fetchMultiple(KeyType.PRODUCT_ID, products)).thenReturn(classRooms);
		List<Classroom> result = underTest.classRoomsByProductId("productId");
		Assert.assertNotNull(result);
	}

	@Test
	public void testGetClassroomById() throws IOException, ProcessingException {
		when(classRoomBridgeService.fetch(any(Classroom.class))).thenReturn(new Classroom());
		Classroom result = underTest.getClassroomById("classRoomId");
		Assert.assertNotNull(result);
	}

	@Test
	public void testGetClassroomByIdandProdId() throws IOException, ProcessingException {
		when(classRoomBridgeService.fetch(any(Classroom.class))).thenReturn(new Classroom());
		Classroom result = underTest.getClassroomByIdandProdId("classRoomId", "productModelIdentifier");
		Assert.assertNotNull(result);
	}

	@Test
	public void testGetClassroomByCode() throws IOException, ProcessingException {
		when(classRoomBridgeService.findClassRoomByValidationCode(anyString())).thenReturn(new Classroom());
		Classroom result = underTest.getClassroomByCode("code");
		Assert.assertNotNull(result);
	}

	@Test
	public void testGetMultipleClassroomsById() throws IOException, ProcessingException {
		List<Classroom> classRooms = new ArrayList<>();
		classRooms.add(new Classroom());

		ArrayList<String> classRoomList = new ArrayList<>();
		classRoomList.add("classRoom1");

		when(classRoomBridgeService.fetchMultiple(KeyType.MULTIPLE_COURSES, classRoomList)).thenReturn(classRooms);
		List<Classroom> result = underTest.getMultipleClassroomsById(classRoomList);
		Assert.assertNotNull(result);
	}

	@Test
	public void testGetPaginatedClassroomTableData() throws IOException, ProcessingException {
		Example<Product> e = Example.of(new Product(),
				ExampleMatcher.matching().withStringMatcher(StringMatcher.DEFAULT));
		Sort sort = new Sort();
		sort.setField("classroomId");
		io.leangen.graphql.execution.SortField.Direction d = Direction.ASC;
		sort.setOrder(d);
		Pageable pageable = PageRequest.of(0, 10,
				org.springframework.data.domain.Sort.Direction.valueOf(sort.getOrder().toString()), sort.getField());
		when(classRoomBridgeService.pageIt(any(Pageable.class), any(Example.class))).thenReturn(getClassrooms());
//		Page<Classroom> classRoom = underTest.getPaginatedClassroomTableData(0, 15, new Classroom(),
//				StringMatcher.DEFAULT, sort, authContex);
//		Assert.assertNotNull(classRoom);
	}

	private final Page<Classroom> getClassrooms() {
		List<Classroom> classRoom = new ArrayList<Classroom>();
		classRoom.add(new Classroom());
		Page<Classroom> pageClassroom = new PageImpl(classRoom);
		return pageClassroom;
	}

}