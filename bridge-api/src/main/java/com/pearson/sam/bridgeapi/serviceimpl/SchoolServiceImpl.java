package com.pearson.sam.bridgeapi.serviceimpl;

import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.MONGO_MAP;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.SAM_MAP;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.pearson.sam.bridgeapi.annotations.MongoSource;
import com.pearson.sam.bridgeapi.annotations.SamSource;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGeneralException;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGraphqlException;
import com.pearson.sam.bridgeapi.iservice.ISchoolService;
import com.pearson.sam.bridgeapi.model.KeyType;
import com.pearson.sam.bridgeapi.model.School;
import com.pearson.sam.bridgeapi.model.SessionUser;
import com.pearson.sam.bridgeapi.repository.SchoolRepository;
import com.pearson.sam.bridgeapi.samclient.IOrganisationSamClient;
import com.pearson.sam.bridgeapi.security.ISessionFacade;
import com.pearson.sam.bridgeapi.util.Utils;

import graphql.ErrorType;
import graphql.GraphQLException;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.dataloader.BatchLoader;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

@Service
public class SchoolServiceImpl implements ISchoolService {
  private static final Logger logger = LoggerFactory.getLogger(SchoolServiceImpl.class);

  private final GsonBuilder gsonBuilder = new GsonBuilder();
  private final Gson gson = gsonBuilder.disableHtmlEscaping()
      .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).setPrettyPrinting().serializeNulls()
      .create();

  private static final String CREATE_SCHOOL = "createSchool";
  private static final String UPDATE_SCHOOL = "updateSchool";
  private static final String CREATE = "create";
  private static final String UPDATE = "update";
  private static final String GET = "get";
  private static final String SCHOOL_ID = "schoolId";
  private static final String DEACTIVATE = "deactivate";
  private static final String TEACHER_CODE = "ST";
  private static final String STUDENT_CODE = "SS";
  private static final String KEY = "School";
  
  @Value("${SPEC_FILE}")
  private String specJsonFile;

  @Autowired
  private SchoolRepository schoolRepository;

  @Autowired
  private IOrganisationSamClient<School> schoolIntegration;

  @Autowired
  protected ISessionFacade sessionFacade;

  @Autowired
  private ISchoolService schoolService;

//  @Autowired
//  private CacheService cacheService;
  
  public SessionUser getUser(boolean getLoggedInUser) {
    return sessionFacade.getLoggedInUser(getLoggedInUser);
  }

  @Override
  public School create(School school) {
    // TODO Auto-generated method stub
    //logger.info("School Service --saving {}", school);
    School responseSchool = new School();
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    String schoolId = school.getSchoolId();
    if (Utils.isEmpty(schoolId)) {
      schoolId = generateSchoolId(school.getName());
      //logger.info("school id is empty and generating school id:" + schoolId);
      school.setSchoolId(schoolId);
    }
    school.setTeacherCode(generateTeacherStudentCode(TEACHER_CODE));
    school.setStudentCode(generateTeacherStudentCode(STUDENT_CODE));
    Map<String, Map<String, Object>> segregatedData = segregatedData(school);
    Map<String, Object> samMap = segregatedData.get(SAM_MAP);
    Map<String, Object> mongoMap = segregatedData.get(MONGO_MAP);

    samMap.put("createdBy", school.getCreatedBy());

    if (StringUtils.isNotEmpty(school.getCreatedOn())) {
      samMap.put("createdOn", school.getCreatedOn());
      mongoMap.put("createdOn", school.getCreatedOn());
    } else {
      samMap.put("createdOn", Utils.generateEpochDate());
      school.setCreatedOn(samMap.get("createdOn").toString());
      mongoMap.put("createdOn", Utils.generateEpochDate());
    }
    if (StringUtils.isNotEmpty(school.getProductModelIdentifier())) {
      samMap.put("productModelIdentifier", school.getProductModelIdentifier());
    } else {
      samMap.put("productModelIdentifier", "PP");
      school.setProductModelIdentifier(samMap.get("productModelIdentifier").toString());
    }
    samMap.put("activeTimestamp", timestamp);

    Map<String, Object> transformedMap = transformJson(samMap, CREATE_SCHOOL, specJsonFile);
    transformedMap.put("serviceType", "school");
    JsonObject documents = gson.toJsonTree(transformedMap).getAsJsonObject();

    responseSchool = schoolIntegration.create(documents);
    responseSchool.setCreatedBy(school.getCreatedBy());
    responseSchool.setCreatedOn(school.getCreatedOn());
    responseSchool.setProductModelIdentifier(school.getProductModelIdentifier());
    responseSchool.setSchoolURL(school.getSchoolURL());

    Map<String, Object> schoolMap = Utils.convertToMap(responseSchool);
    School mergedData = mergedData(schoolMap, mongoMap, CREATE);
    return mergedData;
  }

  private String generateSchoolId(String schoolName) {
    String schoolId = "";
    String names[] = schoolName.trim().split(" ");
    String firstName = names[0];
    String secondName = "";
    if (names.length > 1 && StringUtils.isNotEmpty(names[1]))
      secondName = names[1];

    if (StringUtils.isNotEmpty(firstName) && StringUtils.isNotEmpty(secondName)) {
      schoolId = firstName.substring(0, 1) + secondName.substring(0, 1);
    } else if (StringUtils.isNotEmpty(firstName) && StringUtils.isEmpty(secondName)) {
      if (firstName.length() == 2) {
        schoolId = schoolName;
      } else if (firstName.length() == 1) {
        schoolId = firstName + "0";
      } else {
        schoolId = firstName.substring(0, 2);
      }
    }
    schoolId += "S";
    schoolId = schoolId + Utils.generateRandomNumber(100000, 900000);
    return schoolId;
  }

  private String generateTeacherStudentCode(String code) {
    String teacherStudentCode = code + Utils.generateRandomNumber(10, 90);
    teacherStudentCode = teacherStudentCode + Utils.generateRandomAlphaNumeric(2);
    return teacherStudentCode;
  }

  private School mergedData(Map<String, Object> schoolMap, Map<String, Object> mongoMap,
      String type) {
    mongoMap.put(SCHOOL_ID, schoolMap.get(SCHOOL_ID));
    if (type.equals(UPDATE)) {
      School dbUser = schoolRepository.findBySchoolId(schoolMap.get(SCHOOL_ID).toString());
      School school = Utils.convert(mongoMap, new TypeReference<School>() {
      });
      Utils.copyNonNullProperties(school, dbUser);
      schoolRepository.save(dbUser);
    } else if (type.equals(GET)) {
      School dbUser = schoolRepository.findBySchoolId(schoolMap.get(SCHOOL_ID).toString());
      mongoMap = Utils.convertToMap(dbUser);
    } else if (type.equals(DEACTIVATE)) {
      School dbUser = schoolRepository.findBySchoolId(mongoMap.get(SCHOOL_ID).toString());
      checkAvailable(dbUser);
      dbUser.setSchoolStatus("Inactive");
      schoolRepository.save(dbUser);
      mongoMap = Utils.convertToMap(dbUser);
    } else {
      schoolRepository.save(Utils.convert(mongoMap, new TypeReference<School>() {
      }));
    }

    Map<String, Object> mergedUserMap = new HashMap<>();
    mergedUserMap.putAll(mongoMap);
    mergedUserMap.putAll(schoolMap);
    return Utils.convert(mergedUserMap, new TypeReference<School>() {
    });
  }

  private Map<String, Object> transformJson(Map<String, Object> inputMap, String spec,
      String specPath) {
    return Utils.transformJsonAsMap(inputMap, spec, specPath);
  }

  private void checkAvailable(Object obj) {
    if (obj == null) {
      throw new BridgeApiGraphqlException(ErrorType.DataFetchingException, "Data Not Found!");
    }
  }

  private Map<String, Map<String, Object>> segregatedData(School school) {
    Map<String, Map<String, Object>> segregatedMap = new HashMap<>();

    Map<String, Object> dataMap = Utils.convertToMap(school);

    List<String> mongoFields = Utils.extractAnnotatedFieldNames(School.class, MongoSource.class);
    List<String> samFields = Utils.extractAnnotatedFieldNames(School.class, SamSource.class);

    Map<String, Object> samMap = new HashMap<>(dataMap);
    samMap.keySet().retainAll(samFields);
    Map<String, Object> mongoMap = dataMap;
    mongoMap.keySet().retainAll(mongoFields);

    segregatedMap.put(SAM_MAP, samMap);
    segregatedMap.put(MONGO_MAP, mongoMap);

    return segregatedMap;
  }

	@Override
	public School update(School school) {
		// TODO Auto-generated method stub
		School mergedData = null;

		Map<String, Map<String, Object>> segregatedData = segregatedData(school);
		Map<String, String> schoolId = new HashMap<>();
		schoolId.put(SCHOOL_ID, school.getSchoolId());
		Map<String, Object> samMap = segregatedData.get("samMap");
		samMap.put("updatedBy", school.getUpdatedBy());
		samMap.put("updatedOn", (null != school.getUpdatedOn() ? school.getUpdatedOn() : Utils.generateEpochDate()));
		if (StringUtils.isNotEmpty(school.getProductModelIdentifier())) {
			samMap.put("productModelIdentifier", school.getProductModelIdentifier());
		} else {
			samMap.put("productModelIdentifier", "PP");
			school.setProductModelIdentifier(samMap.get("productModelIdentifier").toString());
		}
		Map<String, Object> mongoMap = segregatedData.get("mongoMap");
		mongoMap.put("updatedBy", getUser(true).getUid());
		mongoMap.put("updatedOn", (null != school.getUpdatedOn() ? school.getUpdatedOn() : Utils.generateEpochDate()));

		Map<String, Object> transformedMap = transformJson(samMap, UPDATE_SCHOOL, specJsonFile);
		transformedMap.put("serviceType", "school");
		transformedMap.remove("orgIdentifier");
		JsonObject documents = gson.toJsonTree(transformedMap).getAsJsonObject();
		School responseSchool = schoolIntegration.update(schoolId, documents);
		Map<String, Object> schoolMap = Utils.convertToMap(responseSchool);
		mergedData = mergedData(schoolMap, mongoMap, UPDATE);
//	    cacheService.update(mergedData,mergedData.getSchoolId(), KEY,this::retrieveSchool);
		return fetch(mergedData);
	}

  private School fetch(School school) {
    validateSchool(school);
    School mergedData = null;
    try {
      Map<String, Map<String, Object>> segregatedData = segregatedData(school);
      Map<String, Object> samMap = segregatedData.get(SAM_MAP);
      Map<String, Object> mongoMap = segregatedData.get(MONGO_MAP);
      Map<String, Object> transformedMap = transformJson(samMap, CREATE_SCHOOL, specJsonFile);
      transformedMap.put("serviceType", "school");
      JsonObject documents = gson.toJsonTree(transformedMap).getAsJsonObject();
      School responseSchool = schoolIntegration.fetchOne(documents);
      Map<String, Object> schoolMap = Utils.convertToMap(responseSchool);
      mergedData = mergedData(schoolMap, mongoMap, GET);

    } catch (BridgeApiGeneralException e) {

    }
    return mergedData;
  }

  private void validateSchool(School school) {
    //logger.info("{}",school);
    if (school == null || Utils.isEmpty(school.getSchoolId())) {
      //logger.error("Unabel to fetch School due to School Id is empty.");
      throw new BridgeApiGeneralException("School Id should not be empty");
    }
    String schoolId = school.getSchoolId();
    School schoolMongoDB = schoolRepository.findBySchoolId(schoolId);
    if (schoolMongoDB == null) {
      //logger.error("Unabel to find School for the given school id:" + schoolId);
      throw new BridgeApiGeneralException(
          "Unabel to find School for the given school id:" + schoolId);
    }
  }

  @Override
  public Page<School> pageIt(Pageable pageable, Example<School> e) {
    return schoolRepository.findAll(e, pageable);
  }

  @Override
  public School delete(School school) {
    // TODO Auto-generated method stub
    validateSchool(school);
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    School mergedData = null;
    Map<String, Map<String, Object>> segregatedData = segregatedData(school);
    Map<String, Object> samMap = segregatedData.get("samMap");
    samMap.put("updatedBy", school.getUpdatedBy());
    samMap.put("updatedOn", timestamp);
    samMap.put("inactiveTimestamp", timestamp);

    Map<String, Object> mongoMap = Utils.convertToMap(school);
    Map<String, Object> transformedMap = transformJson(samMap, "deactivateSchool", specJsonFile);
    JsonObject documents = gson.toJsonTree(transformedMap).getAsJsonObject();

    try {
      School responseSchool = schoolIntegration.deactivate(school.getSchoolId(), documents);
      Map<String, Object> schoolMap = Utils.convertToMap(responseSchool);
      mergedData = mergedData(schoolMap, mongoMap, DEACTIVATE);
      // response.setSuccessful(true);
      // response.setResponse(mergedData);
    } catch (BridgeApiGeneralException e) {
      // response.setSuccessful(false);
      throw new BridgeApiGraphqlException(ErrorType.ValidationError, e.getMessage());
    }
    // return response;
    return mergedData;
  }

  @Override
  public Page<School> pageIt(Pageable pageable, String name, List<String> schoolIds) {
    // TODO Auto-generated method stub
    return schoolRepository.findAllByNameLikeAndSchoolIdIn(name, schoolIds, pageable);
  }

  @Override
  public School findSchoolByTeacherCode(String teacherCode) {
    // TODO Auto-generated method stub
    return schoolRepository.findByTeacherCode(teacherCode);
  }

  @Override
  public School findSchoolByStudentCode(String studentCode) {
    // TODO Auto-generated method stub
    return schoolRepository.findByStudentCode(studentCode);
  }

  @Override
  public School findSchoolByCode(String code) {
    // TODO Auto-generated method stub
    return schoolRepository.findBySchoolCode(code);
  }

  @Override
  public School findSchoolById(String schoolId) {
    // TODO Auto-generated method stub
    return schoolRepository.findBySchoolId(schoolId);
  }

  @Override
  public List<School> fetchMultiple(KeyType key, List<String> ids) {
    // TODO Auto-generated method stub
    List<School> schoolList = null;

    switch (key) {
      case SCHOOL_ID:
        //schoolList = schoolRepository.findAll();
    	  schoolList = null;
        break;

      case USER_ID:
        schoolList = schoolRepository.findAllBySchoolIdIn(ids);
        break;

      default:
        break;
    }
    return schoolList;
  }

  public List<School> fetchSchoolList(List<School> schoolsList) {
    DataLoaderOptions dlo = new DataLoaderOptions();
    dlo.setBatchingEnabled(true);
    dlo.setCachingEnabled(true);
    dlo.setMaxBatchSize(Utils.batchSizeCount(schoolsList.size()));
//    String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();

    BatchLoader<School, School> batchLoader = (List<School> schools) -> {
      //logger.info("calling batchLoader load...");
      return CompletableFuture.supplyAsync(() -> {
        return schools.parallelStream().map(school -> this.loadSchool(school, StringUtils.EMPTY))
            .collect(Collectors.toList());
      });
    };
    DataLoader<School, School> schoolsDataLoader = new DataLoader<>(batchLoader, dlo);
    CompletableFuture<List<School>> cf = schoolsDataLoader.loadMany(schoolsList);
    schoolsDataLoader.dispatchAndJoin();

    List<School> l = null;
    try {
      l = cf.get();
    } catch (InterruptedException | ExecutionException e) {
      throw new GraphQLException(e.getMessage());
    }

    return l;
  }

  private School loadSchool(School school, String sessionId) {
    
    try {
      school.replaceWith(getSchoolWithSessionId(school, sessionId));
    } catch (Exception e) {
      //logger.error("Exception while loading schools {}",e);
    }
    
    return school;
  }

  private School getSchoolWithSessionId(School school, String sessionId) {
//    Object obj = cacheService.getObjectWithSessionId(school, sessionId, school.getSchoolId(), KEY,
//        this::retrieveSchool);
    Object obj = this.retrieveSchool(school);
    return Utils.convert(obj, new TypeReference<School>() {
    });
  }

  private School retrieveSchool(Object obj) {
    School school = Utils.convert(obj, new TypeReference<School>() {
    });
    String schoolId = school.getSchoolId();
    school = schoolService.findSchoolById(schoolId);
    
    if(school==null) {
      throw new BridgeApiGeneralException("School ID Not Found!!");
    }
    
    school = fetch(school);
    return school;
  }

  @Override
  public School getSchoolDetails(School school) {
    return getSchoolWithSessionId(school,
        StringUtils.EMPTY);
  }

  /* (non-Javadoc)
   * @see com.pearson.sam.bridgeapi.iservice.ISchoolService#getTotalCount()
   */
  @Override
  public Long getTotalCount() {
    return schoolRepository.count();
  }

}
