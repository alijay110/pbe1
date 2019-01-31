package com.pearson.sam.bridgeapi.resolvers;

import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_MUTATION_ADD_CLASSROOM;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_MUTATION_REMOVE_CLASSROOM;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_MUTATION_UPDATE_CLASSROOM;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_QUERY_CLASSROOM;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_QUERY_CLASSROOMS;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_QUERY_CLASSROOMS_BY_LIST;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_QUERY_CLASSROOMS_BY_PRODUCTID;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_QUERY_GET_CLASSROOM;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_QUERY_GET_CLASSROOM_BY_CODE;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_QUERY_GET_CLASSROOM_BY_PRODID_ID;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.PGE_GRAPHQL_QUERY_GET_CLASSROOM;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.PGE_GRAPHQL_QUERY_GET_CLASSROOMSEARCH;
import static com.pearson.sam.bridgeapi.util.Utils.copyNonNullProperties;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.pearson.sam.bridgeapi.config.AuthContext;
import com.pearson.sam.bridgeapi.elasticsearch.model.ClassroomSearch;
import com.pearson.sam.bridgeapi.ibridgeservice.IClassRoomBridgeService;
import com.pearson.sam.bridgeapi.model.Classroom;
import com.pearson.sam.bridgeapi.model.KeyType;
import com.pearson.sam.bridgeapi.model.MethodType;
import com.pearson.sam.bridgeapi.model.Sort;
import com.pearson.sam.bridgeapi.validators.ModelValidator;

import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.GraphQLRootContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

@Component
public class ClassRoomResolver {
	
	private static final Logger logger = LoggerFactory.getLogger(ClassRoomResolver.class);
	
	private static final String VALIDATION_CODE = "validationCode";
	private static final String CLASSROOM_ID = "classRoomId";

	@Autowired
	private IClassRoomBridgeService classRoomBridgeService;

	@Autowired
	ModelValidator validator;

	@GraphQLMutation(name = GRAPHQL_MUTATION_ADD_CLASSROOM)
	public Classroom addClassRoom(@GraphQLArgument(name = "data") Classroom data) {
		validator.validateModel(data, MethodType.CREATE.toString());
		return classRoomBridgeService.create(data);
	}

	@GraphQLMutation(name = GRAPHQL_MUTATION_UPDATE_CLASSROOM)
	public Classroom updateClassRoom(@GraphQLArgument(name = "classRoomId") String classRoomId,
			@GraphQLArgument(name = "data") Classroom classRoom) {
		classRoom.setClassroomId(classRoomId);
		validator.validateModel(classRoom, MethodType.CREATE.toString());
		return classRoomBridgeService.update(classRoom);
	}

  /**
   * removeClassRoom.
   * 
   * @return
   * @throws ProcessingException 
   * @throws IOException 
   */
  @GraphQLMutation(name = GRAPHQL_MUTATION_REMOVE_CLASSROOM)
  public Classroom removeClassRoom(@GraphQLArgument(name = "classRoomId") String classRoomId){
    Classroom classRoom = new Classroom();
    classRoom.setClassroomId(classRoomId);
    return classRoomBridgeService.delete(classRoom);
  }

  /**
   * classRoom.
   * 
   * @param data
   *          desc
   * @return desc
   * @throws ProcessingException 
   * @throws IOException 
   */
  @GraphQLQuery(name = GRAPHQL_QUERY_CLASSROOM)
  public Classroom classRoom(@GraphQLArgument(name = "classRoomId") String data){
	validator.validateEmptyNullValue(data, CLASSROOM_ID);
    Classroom classRoom = new Classroom();
    classRoom.setClassroomId(data);
    return classRoomBridgeService.fetch(classRoom);
  }

  @GraphQLQuery(name = GRAPHQL_QUERY_CLASSROOMS)
  public List<Classroom> classRooms() {
	 return classRoomBridgeService.fetchMultiple(KeyType.USER_ID, null);
  }

  /**
   * classRoomsByProductId.
   * 
   * @param productId
   *          pid
   * @return
   */
  @GraphQLQuery(name = GRAPHQL_QUERY_CLASSROOMS_BY_PRODUCTID)
  public List<Classroom> classRoomsByProductId(
      @GraphQLArgument(name = "productId") String productId) {
    ArrayList<String> products = new ArrayList<>();
    products.add(productId);
    return classRoomBridgeService.fetchMultiple(KeyType.PRODUCT_ID, products);
  }

  
  /**
   * getClassroomById.
   * @param data data
   * @return  
   * @throws ProcessingException 
   * @throws IOException 
   */
  @GraphQLQuery(name = GRAPHQL_QUERY_GET_CLASSROOM)
  public Classroom getClassroomById(@GraphQLArgument(name = "classRoomId") String data){
	validator.validateEmptyNullValue(data, CLASSROOM_ID);
    Classroom classRoom = new Classroom();
    classRoom.setClassroomId(data);
    return classRoomBridgeService.fetch(classRoom);
  }
  
  /**
   * getClassroomById.
   * @param data data
   * @return  
   * @throws ProcessingException 
   * @throws IOException 
   */
  @GraphQLQuery(name = GRAPHQL_QUERY_GET_CLASSROOM_BY_PRODID_ID)
  public Classroom getClassroomByIdandProdId(@GraphQLArgument(name = "classRoomId") String data,@GraphQLArgument(name = "productModelIdentifier") String prodId){
    Classroom classRoom = new Classroom();
    classRoom.setClassroomId(data);
    classRoom.setProductModelIdentifier(prodId);
    return classRoomBridgeService.fetch(classRoom);
  }

  /**
   * getClassroomByCode.
   * @param code code
   * @return  
   * @throws ProcessingException 
   * @throws IOException 
   */
  @GraphQLQuery(name = GRAPHQL_QUERY_GET_CLASSROOM_BY_CODE)
  public Classroom getClassroomByCode(@GraphQLArgument(name = "code") String code){
	  validator.validateEmptyNullValue(code, VALIDATION_CODE);
    return classRoomBridgeService.findClassRoomByValidationCode(code);
  }

  @GraphQLQuery(name = GRAPHQL_QUERY_CLASSROOMS_BY_LIST)
  public List<Classroom> getMultipleClassroomsById(
      @GraphQLArgument(name = "classroomIds") List<String> classroomIds) {
	  return classRoomBridgeService.fetchMultiple(KeyType.MULTIPLE_COURSES, classroomIds);
  }
  
  /**
   * getPaginatedClassroomTableData.
   * 
   * @return
   */
  @GraphQLQuery(name = PGE_GRAPHQL_QUERY_GET_CLASSROOM)
  public Page<Classroom> getPaginatedClassroomTableData(
      @GraphQLArgument(name = "offset", defaultValue = "0") Integer pageNumber,
      @GraphQLArgument(name = "limit", defaultValue = "15") Integer pageLimit,
      @GraphQLArgument(name = "filter", defaultValue = "{}") Classroom classRoom,
      @GraphQLArgument(name = "filterPattern", defaultValue = "5") StringMatcher sm,
      @GraphQLArgument(name = "sort", 
          defaultValue = "{\"field\":\"classroomId\",\"order\":\"ASC\"}") Sort sort,
      @GraphQLRootContext AuthContext context) {
    Pageable pageable = PageRequest.of(pageNumber, pageLimit,
        Direction.valueOf(sort.getOrder().toString()), sort.getField());
    ClassroomSearch query = new ClassroomSearch();
    copyNonNullProperties(classRoom,query);

    Page<ClassroomSearch> oldPage = classRoomBridgeService.pageIt(pageable,query,sm);
    return new PageImpl<>(oldPage.getContent().stream().map(clsSrch -> new Classroom(clsSrch)).collect(Collectors.toList()), pageable,
        oldPage.getTotalElements());
  }
  
  /**
	 * This Method will give the paginated classroomSearch as response.
	 * 
	 * @param pageNumber
	 * @param pageLimit
	 * @param filter
	 * @param context
	 * @return Page<ClassroomSearch>
	 */
	@GraphQLQuery(name = PGE_GRAPHQL_QUERY_GET_CLASSROOMSEARCH, description = "Search classroom in elastic search")
	public Page<ClassroomSearch> getClassroomSearch(@GraphQLArgument(name = "offset", defaultValue = "0") Integer pageNumber,
			@GraphQLArgument(name = "limit", defaultValue = "15") Integer pageLimit,
			@GraphQLArgument(name = "filter", defaultValue = "{}") String query,
			@GraphQLRootContext AuthContext context) {
		Pageable pageable = PageRequest.of(pageNumber, pageLimit);
		return classRoomBridgeService.search(pageable, query);
	}

}
