package com.pearson.sam.bridgeapi.bridgeserviceimpl;

import static com.pearson.sam.bridgeapi.util.Utils.copyNonNullProperties;

import com.pearson.sam.bridgeapi.elasticsearch.model.ClassroomSearch;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGraphqlException;
import com.pearson.sam.bridgeapi.ibridgeservice.IClassRoomBridgeService;
import com.pearson.sam.bridgeapi.iservice.IClassRoomService;
import com.pearson.sam.bridgeapi.iservice.IClassroomSearchService;
import com.pearson.sam.bridgeapi.iservice.IProductService;
import com.pearson.sam.bridgeapi.iservice.IUserService;
import com.pearson.sam.bridgeapi.model.Classroom;
import com.pearson.sam.bridgeapi.model.KeyType;
import com.pearson.sam.bridgeapi.model.Members;
import com.pearson.sam.bridgeapi.model.Product;
import com.pearson.sam.bridgeapi.model.User;
import com.pearson.sam.bridgeapi.security.ISessionFacade;
import com.pearson.sam.bridgeapi.util.Utils;

import graphql.ErrorType;

import io.jsonwebtoken.lang.Collections;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.dataloader.BatchLoader;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

@Service
public class ClassRoomBridgeServiceImpl implements IClassRoomBridgeService {
	private static final Logger logger = LoggerFactory.getLogger(ClassRoomBridgeServiceImpl.class);

	@Autowired
	private IClassRoomService classRoomService;

	@Autowired
	private IProductService productService;

	@Autowired
	private IUserService userService;

	@Autowired
	private ISessionFacade sessionFacade;

	@Autowired
	private IClassroomSearchService classroomSearchService;

	
	 private static Example<Classroom> classEx = Example.of(new Classroom());

	  public void loadClassroomsToMaster() {
	    initLoad(PageRequest.of(0, classRoomService.getTotalCount().intValue()));
	  }

	  private void initLoad(Pageable pageable) {
	    List<Classroom> classRoomList = this.fetchClassRoomList(classRoomService.pageIt(pageable, classEx).getContent());
	    
	    
	    
	    
	    classRoomList.stream().forEach( cls -> {ClassroomSearch query = new ClassroomSearch(); copyNonNullProperties(cls, query);  classroomSearchService.save(query);});;
	  }

	  
	@Override
	public Classroom create(Classroom classRoom) {
		// TODO Auto-generated method stub
		User user = sessionFacade.getLoggedInUser(true);
		classRoom.setCreatedBy(user.getUid());
		classRoom = classRoomService.create(classRoom);
		classroomSearchService.save(getClassroomSearchObject(classRoom));
		return classRoom;
	}

	@Override
	public Classroom update(Classroom userClassRoom) {
		// TODO Auto-generated method stub
		User user = sessionFacade.getLoggedInUser(true);
		userClassRoom.setUpdatedBy(user.getUid());
		userClassRoom = classRoomService.update(userClassRoom);
		classroomSearchService.update(getClassroomSearchObject(userClassRoom));
		return userClassRoom;
	}

	@Override
	public Classroom fetch(Classroom classRoom) {
		// TODO Auto-generated method stub
		classRoom = classRoomService.findClassRoomById(classRoom.getClassroomId());
		return fetchProdAndMemberDetails(classRoom);
	}

	private Classroom fetchProdAndMemberDetails(Classroom classRoom) {
		if (classRoom.getProduct() != null && !classRoom.getProduct().isEmpty())
			fetchProdcutDetails(classRoom);
		if (Utils.isNotEmpty(classRoom.getOwner()))
			fetchOwnerDetails(classRoom);
		fetchListOfMemberDetails(classRoom);
		return classRoom;
	}

	@Override
	public Classroom delete(Classroom classRoom) {
		// TODO Auto-generated method stub
		String classRoomId = classRoom.getClassroomId();
		classRoom = classRoomService.findClassRoomById(classRoom.getClassroomId());
		if (classRoom == null) {
			//logger.error("Unabel to find the Classroom Id:" + classRoomId);
			throw new BridgeApiGraphqlException(ErrorType.DataFetchingException,
					"Unabel to find the Classroom Id:" + classRoomId);
		}
		classRoom = classRoomService.delete(classRoom);
		classroomSearchService.delete(getClassroomSearchObject(classRoom));
		return classRoom;
	}

	@Override
	public List<Classroom> fetchMultiple(KeyType key, List<String> ids) {
		// TODO Auto-generated method stub

		List<Classroom> classRoomList = null;

		switch (key) {
		case COURSE_ID:
			classRoomList = classRoomService.fetchMultiple(key, ids);
			break;

		case USER_ID:
			User user = sessionFacade.getLoggedInUser(true);
			//logger.info("Logged in user {}", user.getUid());
			classRoomList = classRoomService.fetchMultiple(key, user.getClassroom());
			break;

		case PRODUCT_ID:
			classRoomList = classRoomService.fetchMultiple(key, ids);
			break;

		case MULTIPLE_COURSES:
			classRoomList = classRoomService.fetchMultiple(key, ids);
			break;

		default:
			break;
		}
		return fetchClassRoomList(classRoomList);
	}

	@Override
	public Page<Classroom> pageIt(Pageable pageable, Example<Classroom> e) {
		// TODO Auto-generated method stub
		Page<Classroom> classroomPage = classRoomService.pageIt(pageable, e);
		List<Classroom> classRoomClientResponse = fetchClassRoomList(classroomPage.getContent());
		return new PageImpl<Classroom>(classRoomClientResponse, pageable, classroomPage.getTotalElements());
	}

	private void fetchProdcutDetails(Classroom classroom) {
		List<Product> productDetails = new ArrayList<Product>();
		for (String productId : classroom.getProduct()) {
			Product product = new Product();
			product.setProductId(productId);
			Product clientResponse = productService.fetchOne(product);
			productDetails.add(clientResponse);
		}
		classroom.setProductDetails(productDetails);
	}

	private void fetchOwnerDetails(Classroom classroom) {
		User user = fetchUserDetails(classroom.getOwner());
		classroom.setOwnerDetails(user);
	}

	private User fetchUserDetails(String userName) {
		User user = new User();
		user.setUid(userName);
		User clientResponse = null;
		try {
			clientResponse = userService.getUser(user);
		} catch (BridgeApiGraphqlException bage) {
			//logger.error("Unable to fetch User for User id:{}", userName);
			//logger.error("Unable to fetch User:" + bage.getMessage());
		} catch (Exception ex) {
			//logger.error("Unable to fetch User due to internal exeception for User id:{}", userName);
			//logger.error("Unable to fetch User internal exeception:" + ex.getMessage());
		}
		return clientResponse != null ? clientResponse : null;
	}

	private void fetchMemberDetails(Members member) {
		List<User> studentsDetails = null;
		List<User> teachersDetails = null;
		//logger.info("fetchMemberDetails:{}", member);
		if (!Collections.isEmpty(member.getStudents())) {
			studentsDetails = member.getStudents().stream().map(userName -> fetchUserDetails(userName))
					.collect(Collectors.toList());
			member.setStudentsDetails(studentsDetails);
		}
		if (!Collections.isEmpty(member.getTeachers())) {
			teachersDetails = member.getTeachers().stream().map(userName -> fetchUserDetails(userName))
					.collect(Collectors.toList());
			member.setTeachersDetails(teachersDetails);
		}
	}

	private void fetchListOfMemberDetails(Classroom classroom) {
		if (!Objects.isNull(classroom.getMembers()) && !Collections.isEmpty(classroom.getMembers())) {
			classroom.getMembers().forEach(member -> fetchMemberDetails(member));
		}
	}

	private List<Classroom> fetchClassRoomList(List<Classroom> classRooms) {
		DataLoaderOptions dlo = new DataLoaderOptions();
		dlo.setBatchingEnabled(true);
		dlo.setMaxBatchSize(Utils.batchSizeCount(classRooms.size()));

		BatchLoader<String, Classroom> batchLoader = (List<String> classRoomIds) -> {
			//logger.info("fetchClassRoomList calling batchLoader load...");
			List<Classroom> responseClassrooms = classRoomIds.stream().map(classRoomId -> {
				Classroom clientResponse = null;
				try {
					clientResponse = classRoomService.findClassRoomById(classRoomId);
					clientResponse = fetchProdAndMemberDetails(clientResponse);
				} catch (Exception ex) {
					//logger.error("Unable to fetch details of ClassRoom Id:{}", classRoomId);
					//logger.error("Error Message:{}", ex.getMessage());
				}
				return clientResponse;
			}).collect(Collectors.toList());

			return CompletableFuture.supplyAsync(() -> {
				return responseClassrooms;
			});

		};
		DataLoader<String, Classroom> classDataLoader = new DataLoader<String, Classroom>(batchLoader, dlo);
		classRooms.stream().map(key -> classDataLoader.load(key.getClassroomId())).collect(Collectors.toList());
		List<Classroom> responseClassRoomsList = classDataLoader.dispatchAndJoin();
		return responseClassRoomsList;
	}

	@Override
	public Classroom findClassRoomByValidationCode(String validationCode) {
		// TODO Auto-generated method stub
		Classroom classroom = classRoomService.findClassRoomByValidationCode(validationCode);
		if (classroom == null) {
			//logger.error("Unabel to find the Classroom by validation code:" + validationCode);
			throw new BridgeApiGraphqlException(ErrorType.DataFetchingException,
					"Unabel to find the Classroom by validation code:" + validationCode);
		}
		return fetchProdAndMemberDetails(classroom);
	}

	@Override
	public Page<ClassroomSearch> search(Pageable pageable, String searchable) {
		return classroomSearchService.search(pageable, searchable);
	}
	
	@Override
	public Page<ClassroomSearch> pageIt(Pageable pageable, ClassroomSearch searchable,StringMatcher sm) {
		return classroomSearchService.pageIt(pageable, searchable,sm);
	}

	private String getSessionId() {
		return RequestContextHolder.currentRequestAttributes().getSessionId();
	}

	private ClassroomSearch getClassroomSearchObject(Classroom classroom) {
		ClassroomSearch classroomSearch = new ClassroomSearch();
		Utils.copyNonNullProperties(classroom, classroomSearch);
		return classroomSearch;
	}

	

}
