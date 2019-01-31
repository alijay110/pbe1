package com.pearson.sam.bridgeapi.serviceimpl;

import com.bazaarvoice.jolt.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGraphqlException;
import com.pearson.sam.bridgeapi.ibridgeservice.IUserBridgeService;
import com.pearson.sam.bridgeapi.model.BulkUploadJobDetails;
import com.pearson.sam.bridgeapi.model.BulkUploadJobSummary;
import com.pearson.sam.bridgeapi.model.ExistingUserData;
import com.pearson.sam.bridgeapi.model.KafkaUser;
import com.pearson.sam.bridgeapi.model.NewlyAddedUserData;
import com.pearson.sam.bridgeapi.model.User;
import com.pearson.sam.bridgeapi.repository.BulkUploadJobDetailsRepository;
import com.pearson.sam.bridgeapi.repository.BulkUploadJobSummaryRepository;
import com.pearson.sam.bridgeapi.resthelper.SAMServiceException;
import com.pearson.sam.bridgeapi.samclient.IUserSamClient;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.pearson.sam.bridgeapi.util.Utils;

@Service
public class KafkaConsumer {

	private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);
	
	@Autowired
    private KafkaTemplate<String, BulkUploadJobDetails> kafkaTemplate;
	
	
	
	@Autowired
	private IUserBridgeService userBridgeService;
	
	@Autowired
	private BulkUploadJobDetailsRepository bulkUploadJobDetailsRepository;
	
	@Autowired
	private BulkUploadJobSummaryRepository bulkUploadJobSummaryRepository;
	
	@Value("${SPEC_FILE}")
	private String specJsonFile;
	
    @KafkaListener(topics = "PP2Topic", groupId = "group_id",
            containerFactory = "userKafkaListenerFactory")
    public void consumeJson(BulkUploadJobSummary bulkUploadJobSummary) {
    	
         Object[] csvData = bulkUploadJobSummary.getValidCSVData();
         List<ExistingUserData> existingUserDataList = new ArrayList<>();
         List<NewlyAddedUserData> newlyAddedUserDataList = new ArrayList<>();
         List<String> schools = new ArrayList<>();
         schools.add(bulkUploadJobSummary.getSchoolId());
         Set<String> roles = new HashSet<>();
         int errorCount=0;
         int successCount=0;
         
         //Fetching Job id form bulkUploadJobDetailsRepository
         BulkUploadJobDetails dbbulkUploadJobDetails = bulkUploadJobDetailsRepository.findByJobId(bulkUploadJobSummary.getJobId());
         BulkUploadJobSummary dbBulkUploadJobSummary = bulkUploadJobSummaryRepository.findByJobId(bulkUploadJobSummary.getJobId());
         
        for (Object data : csvData) {
            Map<String, Object> userMap = Utils.convertToMap(data);
            //logger.info("userMap.get(\"usertype\") :{}", userMap.get("usertype"));
            Map<String, Object> transformedMap = Utils.transformJsonAsMap(userMap, "BulkCreateUser",
                    specJsonFile);
            //logger.info("transformedMap :{}", transformedMap);
            User user = Utils.convert(transformedMap, new TypeReference<User>() {
            });
            
            if(userMap.get("usertype") != null) {
            	roles.add(userMap.get("usertype").toString());
            }else {
            	if(bulkUploadJobSummary.getSelectedUserRole().equalsIgnoreCase("channel-staff")) {
            		roles.add("channel-partner");
            	}else {
            		roles.add(bulkUploadJobSummary.getUploadType());
            	}
            }
            
            user.setUserName(user.getEmail());
            user.setRoles(roles);
            user.setSchool(schools);
            //logger.info("user.getFullName() :{}", user.getFullName());
            //logger.info("user.getEmail() :{}", user.getEmail());
            //logger.info("user.getYear() :{}", user.getYear());
            //logger.info("user.getPasswd() :{}", user.getPasswd());
            //logger.info("user.getSchool() :{}", user.getSchool());
            //logger.info("user.getRoles() :{}", user.getRoles());
            
            try {
            	User createUser = userBridgeService.createUser(user);
            	//logger.info("bulkUploadJobSummary.getSelectedUserRole() :{}",bulkUploadJobSummary.getSelectedUserRole().equalsIgnoreCase("students"));
            	successCount++;
            	//Add newly added user to newlyAddedUserData Array
            	NewlyAddedUserData newlyAddedUserData = new NewlyAddedUserData();
            	newlyAddedUserData.setFullName(createUser.getFullName());
            	newlyAddedUserData.setEmail(createUser.getEmail());
            	newlyAddedUserData.setRole(createUser.getRoles().stream().findFirst().get());
            	newlyAddedUserData.setSchoolId(bulkUploadJobSummary.getSchoolId());
            	
            	if(bulkUploadJobSummary.getSelectedUserRole().equalsIgnoreCase("students")) {
            		newlyAddedUserData.setYearLevel(user.getYear());
            	}
            	newlyAddedUserDataList.add(newlyAddedUserData);
            	//logger.info("New User added to newlyAddedUserDataList :{}",user.getEmail());
            }catch(BridgeApiGraphqlException | SAMServiceException be) {
            	errorCount++;
            	//logger.info("be.getMessage(); :{}",user.getEmail()+"=="+ be.getMessage());
            	ExistingUserData existingUserData = new ExistingUserData();
            	existingUserData.setFullName(user.getFullName());
            	existingUserData.setEmail(user.getEmail());
            	existingUserData.setYearLevel(user.getYear());
            	existingUserData.setRole(bulkUploadJobSummary.getUploadType());
            	existingUserData.setSchoolId(bulkUploadJobSummary.getSchoolId());
            	existingUserDataList.add(existingUserData);
            	//logger.info("User added to existingUserDataList :{}",user.getEmail());
            }
        }
        dbbulkUploadJobDetails.setNewlyAddedUserData(newlyAddedUserDataList);
        dbbulkUploadJobDetails.setExistingUserData(existingUserDataList);
        dbbulkUploadJobDetails.setErrorCount(errorCount);
        dbbulkUploadJobDetails.setSuccessCount(successCount);
        
        //Change the completed status
        if(dbbulkUploadJobDetails.getErrorCount() == 0) {
        	dbBulkUploadJobSummary.setStatus("Completed");
        }else {
        	dbBulkUploadJobSummary.setStatus("Completed with Errors");
        }
        
        
        if(successCount==1) {
    		dbbulkUploadJobDetails.setSuccessBoxMessage(successCount+" "+bulkUploadJobSummary.getSelectedUserRole()+""
    				+ " user has been added to "+bulkUploadJobSummary.getSchoolId());
    	}else if(successCount>1) {
    		dbbulkUploadJobDetails.setSuccessBoxMessage(successCount+" "+bulkUploadJobSummary.getSelectedUserRole()+""
    				+ " users has been added to "+bulkUploadJobSummary.getSchoolId());
    	}
        if(errorCount==1) {
    		dbbulkUploadJobDetails.setErrorBoxMessage(" There was 1 "+bulkUploadJobSummary.getSelectedUserRole()+" "
    				+ " user that was not added/updated. See why below.");
    	}else if(errorCount>1) {
    		dbbulkUploadJobDetails.setErrorBoxMessage(" There were "+errorCount+" "+bulkUploadJobSummary.getSelectedUserRole()+" "
    				+ " users that were not added/updated. See why below.");
    	}
    	bulkUploadJobDetailsRepository.save(dbbulkUploadJobDetails);
    	bulkUploadJobSummaryRepository.save(dbBulkUploadJobSummary);
    	//logger.info("Produced SAM message :{}",Utils.convertToMap(dbbulkUploadJobDetails));
    }
    
    @KafkaListener(topics = "SamTopic", groupId = "group_id",
            containerFactory = "userKafkaListenerFactory")
    public void consumeJson1(BulkUploadJobDetails bulkUploadJobDetails) {
      	//logger.info("consumeJson1 SamTopic JSON Message :{}",Utils.convertToMap(bulkUploadJobDetails));
        
    }
}