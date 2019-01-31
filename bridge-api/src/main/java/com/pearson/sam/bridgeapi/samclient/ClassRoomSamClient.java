package com.pearson.sam.bridgeapi.samclient;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.pearson.sam.bridgeapi.model.Classroom;
import com.pearson.sam.bridgeapi.samservices.GroupServices;

/**
 * ClassRoomRestIntegration.
 * @author VGUDLSA
 *
 */

@Component
public class ClassRoomSamClient {

  private final GsonBuilder gsonBuilder = new GsonBuilder();
  private final Gson gson = gsonBuilder.setPrettyPrinting().disableHtmlEscaping().create();
  protected static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  @Value("${SPEC_FILE}")
  private String specJsonFile;
  
  @Autowired
  private GroupServices groupServices;
  

  public Classroom create(JsonObject documents) {
    String requestJson = gson.toJson(documents);
    Map<String, Object> resultMap = groupServices.createGroup(requestJson);
    String classroomId = (String) resultMap.get("groupIdentifier");
    Classroom classroom = new Classroom();
    classroom.setClassroomId(classroomId);
    return classroom;
  }
  
  public Map<String, Object> getGroupDetails(String groupIdentifier, String productModelIdentifier, String orgIdentifier,
  		String activationTimestamp, String deactivationTimestamp, String userName) {
  	return groupServices.getGroupDetails(groupIdentifier, productModelIdentifier,orgIdentifier,
	  		 activationTimestamp, deactivationTimestamp, userName);
  }

  public Classroom update(Map<String, String> params, JsonObject documents) {
	  String requestJson = gson.toJson(documents);
	  Map<String, Object> resultMap= groupServices.updateGroupeNameOrType(requestJson,params.get("classroomId"));
	  String classroomId = (String) resultMap.get("groupIdentifier");
	  Classroom classroom = new Classroom();
	  classroom.setClassroomId(classroomId);
	  return classroom;
  }


  public Classroom fetchOne(JsonObject documents) {
	return null;
  }


  public List<Classroom> fetchMultiple(List<String> ids) {
	return Collections.emptyList();
  }


  public Classroom delete(String id) {
    return null;
  }

}
