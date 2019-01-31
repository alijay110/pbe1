package com.pearson.sam.bridgeapi.serviceimpl;

import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.VALIDATION_CODE_SHOULD_BE_UNIQUE;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.VALIDATION_CODE_SHOULD_NOT_BE_EMPTY;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.pearson.sam.bridgeapi.annotations.MongoSource;
import com.pearson.sam.bridgeapi.annotations.SamSource;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGeneralException;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGraphqlException;
import com.pearson.sam.bridgeapi.iservice.IClassRoomService;
import com.pearson.sam.bridgeapi.model.Classroom;
import com.pearson.sam.bridgeapi.model.KeyType;
import com.pearson.sam.bridgeapi.model.Members;
import com.pearson.sam.bridgeapi.repository.ClassRoomRepository;
import com.pearson.sam.bridgeapi.samclient.ClassRoomSamClient;
import com.pearson.sam.bridgeapi.util.Utils;

import graphql.ErrorType;
import io.jsonwebtoken.lang.Collections;

@Service
public class ClassRoomServiceImpl implements IClassRoomService {
	private static final Logger logger = LoggerFactory.getLogger(ClassRoomServiceImpl.class);

	private static final String GROUP_NAME = "groupName";
	private static final String SAM_MAP = "samMap";
	private static final String MONGO_MAP = "mongoMap";
	private static final String GROUP_OWNER = "PP";
	private static final String PRODUCT_MODEL_IDENTIFIER = "PP";
	private static final String ORG_IDENTIFIER = "PP";
	private static final String GROUP_IDENTIFIER = "groupIdentifier";
	private static final String CLASSROOM_ID = "classroomId";
	private static final String NAME = "name";

	@Autowired
	private ClassRoomRepository classRoomRepository;

	@Autowired
	private ClassRoomSamClient classRoomSamClient;

	private final GsonBuilder gsonBuilder = new GsonBuilder();
	private final Gson gson = gsonBuilder.setPrettyPrinting().disableHtmlEscaping().create();

	@Override
	public Classroom create(Classroom classRoom) {
		// TODO Auto-generated method stub
		validateValidationCode(classRoom);
		Classroom responseClassRoom = new Classroom();
		classRoom.setCreatedOn(Utils.isNotEmpty(classRoom.getCreatedOn()) ? classRoom.getCreatedOn() : Utils.generateEpochDate());
		classRoom.setDateCreated(Utils.isNotEmpty(classRoom.getDateCreated()) ? classRoom.getDateCreated() : Utils.generateEpochDate());
		classRoom.setActiveTimestamp(new Date().toString());
		classRoom.setGroupOwner(GROUP_OWNER);
		classRoom.setProductModelIdentifier(PRODUCT_MODEL_IDENTIFIER);
		classRoom.setOrgIdentifier(ORG_IDENTIFIER);
		if (Utils.isEmpty(classRoom.getClassroomId()))
			classRoom.setClassroomId(generateRandomClassId());
		//logger.info("saving classroom:{}", classRoom);
		Map<String, Map<String, Object>> segregatedDataMap = segregatedData(classRoom);
		Classroom mongoClassRoom = Utils.convert(segregatedDataMap.get(MONGO_MAP), new TypeReference<Classroom>() {
		});

		try {
			classRoomRepository.insert(mongoClassRoom);

			JsonObject documents = gson.toJsonTree(segregatedDataMap.get(SAM_MAP)).getAsJsonObject();
			documents.addProperty(GROUP_IDENTIFIER, classRoom.getClassroomId());
			documents.addProperty(GROUP_NAME, classRoom.getName());
			responseClassRoom = classRoomSamClient.create(documents);
			Utils.copyNonNullProperties(classRoom, responseClassRoom);
		} catch (BridgeApiGeneralException e) {
			classRoomRepository.deleteByClassroomId(classRoom.getClassroomId());
		}
		return responseClassRoom;
	}

	@Override
	public Classroom findClassRoomById(String classRoomId) {
		// TODO Auto-generated method stub
		Classroom classRoom = classRoomRepository.findByClassroomId(classRoomId);
		if (classRoom == null) {
			//logger.error("Unabel to find the Classroom Id:"+classRoomId);
			throw new BridgeApiGraphqlException(ErrorType.DataFetchingException, "Unabel to find the Classroom Id:"+classRoomId);
		}
		classRoom.setProductModelIdentifier(PRODUCT_MODEL_IDENTIFIER);
		if (classRoom.getClassroomId() != null && classRoom.getProductModelIdentifier() != null) {
			try {
				Map<String, Object> restMap = classRoomSamClient.getGroupDetails(classRoom.getClassroomId(),
						classRoom.getProductModelIdentifier(), classRoom.getOrgIdentifier(), null, null, null);
				classRoom = mergeData(classRoom, restMap);
			} catch (BridgeApiGeneralException e) {
				throw new BridgeApiGraphqlException(ErrorType.ValidationError, e.getMessage());
			}

		} else if (classRoom.getClassroomId() != null) {
			classRoom = classRoomRepository.findByClassroomId(classRoom.getClassroomId());
		} else if (classRoom.getValidationCode() != null) {
			classRoom = classRoomRepository.findByValidationCode(classRoom.getValidationCode());
		}
		return classRoom;
	}

	@Override
	public Classroom update(Classroom userClassRoom) {
		// TODO Auto-generated method stub
		userClassRoom.setUpdatedOn(
				null != userClassRoom.getUpdatedOn() ? userClassRoom.getUpdatedOn() : Utils.generateEpochDate());
		userClassRoom.setGroupOwner(GROUP_OWNER);
		// Storing is not required for Student and Teacher details into MongoDB.
		Classroom dbClassRoom = classRoomRepository.findByClassroomId(userClassRoom.getClassroomId());
		if (dbClassRoom == null) {
			//logger.error("Unable to find the ClassRoom:" + userClassRoom.getClassroomId());
			throw new BridgeApiGraphqlException("Unable to find the ClassRoom:" + userClassRoom.getClassroomId());
		}
		List<Members> sourceMembers = userClassRoom.getMembers();
		List<Members> targetMembers = dbClassRoom.getMembers();
		Utils.copyNonNullProperties(userClassRoom, dbClassRoom);
		if (!Collections.isEmpty(sourceMembers) && !Collections.isEmpty(targetMembers)
				&& sourceMembers.size() == targetMembers.size()) {
			for (int i = 0; i < sourceMembers.size(); i++) {
				Utils.copyNonNullProperties(sourceMembers.get(i), targetMembers.get(i));
			}
		}
		dbClassRoom.setMembers(targetMembers);
		setMembersData(userClassRoom);
		dbClassRoom = classRoomRepository.save(dbClassRoom);
		try {
			Map<String, Map<String, Object>> segregatedDataMap = segregatedData(userClassRoom);
			JsonObject documents = gson.toJsonTree(segregatedDataMap.get(SAM_MAP)).getAsJsonObject();
			documents.addProperty(GROUP_NAME, dbClassRoom.getName());
			Map<String, String> mp = new HashMap<>();
			mp.put(CLASSROOM_ID, userClassRoom.getClassroomId());
			classRoomSamClient.update(mp, documents);
		} catch (BridgeApiGeneralException e) {
			//logger.error("Unable to update the ClassRoom:" + userClassRoom.getClassroomId());
			//logger.error("Unable to update the ClassRoom, Error Message is:" + e.getMessage());
		}
		return dbClassRoom;
	}

	private String generateRandomClassId() {
		String classRoomId = "C";
		classRoomId += Utils.generateRandomNumber(0, 9);
		classRoomId += Utils.generateRandomAlphabet(1);
		classRoomId += Utils.generateRandomNumber(0, 9);
		classRoomId += Utils.generateRandomAlphaNumeric(2);
		return classRoomId;
	}

	private Map<String, Map<String, Object>> segregatedData(Classroom classRoom) {
		Map<String, Map<String, Object>> segregatedMap = new HashMap<>();
		// Storing is not required for Student and Teacher details into MongoDB.
		setMembersData(classRoom);
		// Ended.
		Map<String, Object> dataMap = Utils.convertToMap(classRoom);

		List<String> mongoFields = Utils.extractAnnotatedFieldNames(Classroom.class, MongoSource.class);
		List<String> samFields = Utils.extractAnnotatedFieldNames(Classroom.class, SamSource.class);

		Map<String, Object> samMap = new HashMap<>(dataMap);
		samMap.keySet().retainAll(samFields);
		Map<String, Object> mongoMap = dataMap;
		mongoMap.keySet().retainAll(mongoFields);

		segregatedMap.put(SAM_MAP, samMap);
		segregatedMap.put(MONGO_MAP, mongoMap);

		return segregatedMap;
	}

	private void setMembersData(Classroom classRoom) {
		List<Members> members = classRoom.getMembers();
		if (!Collections.isEmpty(members)) {
			for (Members member : members) {
				if (!Objects.isNull(member)) {
					member.setStudentsDetails(null);
					member.setTeachersDetails(null);
				}
			}
		}
		classRoom.setMembers(members);
	}

	private Classroom mergeData(Classroom classRoom, Map<String, Object> samMap) {
		Map<String, Object> mongoMap = Utils.convertToMap(classRoom);
		if (samMap != null) {
			mongoMap.putAll(samMap);
			Object obj1 = mongoMap.remove(GROUP_IDENTIFIER);
			mongoMap.put(CLASSROOM_ID, obj1);
			Object obj2 = mongoMap.remove(GROUP_NAME);
			mongoMap.put(NAME, obj2);
		}

		classRoom = Utils.convert(mongoMap, new TypeReference<Classroom>() {
		});
		return classRoom;
	}

	public Classroom delete(Classroom classRoom) {
		// TODO Auto-generated method stub
		classRoomRepository.delete(classRoom);
		return classRoom;
	}

	@Override
	public List<Classroom> fetchMultiple(KeyType key, List<String> ids) {
		// TODO Auto-generated method stub
		List<Classroom> classRoomList = null;

		switch (key) {
		case COURSE_ID:
			classRoomList = classRoomRepository.findAll();
			break;

		case USER_ID:
			classRoomList = classRoomRepository.findAllByClassroomIdIn(ids);
			break;
			
		case PRODUCT_ID:
			classRoomList = classRoomRepository.findAllByProductIn(ids);
			break;

		case MULTIPLE_COURSES:
			classRoomList = classRoomRepository.findAllByClassroomIdIn(ids);
			break;

		default:
			break;
		}
		return classRoomList;
	}

	@Override
	public Page<Classroom> pageIt(Pageable pageable, Example<Classroom> e) {
		// TODO Auto-generated method stub
		return classRoomRepository.findAll(e, pageable);
	}

	@Override
	public Classroom findClassRoomByValidationCode(String validationCode) {
		// TODO Auto-generated method stub
		return classRoomRepository.findByValidationCode(validationCode);
	}

	@Override
	public Long countBySchoolId(String schoolId) {
		// TODO Auto-generated method stub
		return classRoomRepository.countBySchoolId(schoolId);
	}


	private void validateValidationCode(Classroom classroom) {
		if (Utils.isEmpty(classroom.getValidationCode())) {
			throw new BridgeApiGraphqlException(ErrorType.ValidationError, VALIDATION_CODE_SHOULD_NOT_BE_EMPTY);
		} else {
			if (classRoomRepository.existsByValidationCode(classroom.getValidationCode())) {
				throw new BridgeApiGraphqlException(ErrorType.ValidationError, VALIDATION_CODE_SHOULD_BE_UNIQUE);
			}
		}
	}

  /* (non-Javadoc)
   * @see com.pearson.sam.bridgeapi.iservice.IClassRoomService#getTotalCount()
   */
  @Override
  public Long getTotalCount() {
    // TODO Auto-generated method stub
    return classRoomRepository.count();
  }

}