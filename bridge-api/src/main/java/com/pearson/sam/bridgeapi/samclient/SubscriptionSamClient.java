package com.pearson.sam.bridgeapi.samclient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.pearson.sam.bridgeapi.model.AccessCodes;
import com.pearson.sam.bridgeapi.samservices.AccessCodeServices;
import com.pearson.sam.bridgeapi.util.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("unchecked")
public class SubscriptionSamClient {

  private static final Logger logger = LoggerFactory.getLogger(SubscriptionSamClient.class);
  private final GsonBuilder gsonBuilder = new GsonBuilder();
  private final Gson gson = gsonBuilder.setPrettyPrinting().disableHtmlEscaping().create();
  private static final String ACCESS_CODE = "accessCode";
  private static final String CREATED_DATE = "createdDate";
  private static final String DATE_CREATED = "dateCreated";

  @Autowired
  AccessCodeServices accessCodeServices;

  @Value("${SPEC_FILE}")
  private String specJsonFile;

  public void init(String collectionName) {
    //logger.info("Inisde AccessCodeIntegration");
  }

  @SuppressWarnings("deprecation")

  public AccessCodes create(JsonObject documents) {
    //logger.info("Inside Save {}", documents);
    Map<String, Object> requestMap = getScopeNProduct(documents);
    requestMap.put(DATE_CREATED, new Date().getTime());

    Map<String, Object> accessCodeRequestMap = transformJson(requestMap, "subscription_creation",
        specJsonFile);

    accessCodeRequestMap.remove("createdDate");
    accessCodeRequestMap.put("createdOn", String.valueOf(requestMap.get("startDate")));
    String requestJson = gson.toJson(accessCodeRequestMap);
    String requestParam = documents.get("requestParam") == null
        ? (String) requestMap.get(ACCESS_CODE)
        : documents.get("requestParam").getAsString();
    Map<String, Object> resultMap = accessCodeServices
        .createAndActivateProductAndSubscription(requestParam, requestJson);
    String accessCodeValue = (String) resultMap.get(ACCESS_CODE);
    String createdDate = (String) resultMap.get(CREATED_DATE);
    AccessCodes accessCodes = new AccessCodes();
    accessCodes.setCode(accessCodeValue);
    accessCodes.setAccessCode(accessCodeValue);
    accessCodes.setDateCreated(createdDate);
    return accessCodes;
  }

  public List<AccessCodes> createMultiple(JsonObject documents) {
    return null;
  }

  public AccessCodes update(Map<String, String> params, JsonObject documents) {
    return null;
  }

  public AccessCodes fetchOne(JsonObject documents) {
    String accessCode = documents.get("accessCode").getAsString();
    Map<String, Object> resultMap = accessCodeServices.getAccessCodeDetails(accessCode);
    Map<String, Object> accessCodeRequestMap = transformJson(resultMap, "get_access_code",
        specJsonFile);
    return Utils.convert(accessCodeRequestMap, new TypeReference<AccessCodes>() {
    });
  }

	public AccessCodes getAccessCodeSubscription(AccessCodes accessCodes) {
		Map<String, Object> resultMap = accessCodeServices.getAccessCodeDetails(accessCodes.getAccessCode());
		Map<String, Object> accessCodeRequestMap = transformJson(resultMap, "get_access_code", specJsonFile);
		return Utils.convert(accessCodeRequestMap, new TypeReference<AccessCodes>() {
		});
	}

  public AccessCodes delete(String id) {
    return null;
  }

  private Map<String, Object> getScopeNProduct(JsonObject documents) {
    Map<String, Object> requestMap = gson.fromJson(documents, HashMap.class);
    List<String> products = (List<String>) requestMap.get("products");
    if (null != products) {
      List<Map<String, String>> productList = new ArrayList<>();

      for (String product : products) {
        Map<String, String> prodMap = new HashMap<>();
        prodMap.put("orgIdentifier", product);
        prodMap.put("activationDate", String.valueOf(requestMap.get("startDate")));
        prodMap.put("expirationDate", String.valueOf(requestMap.get("endDate")));
        prodMap.put("productIdentifier", product);
        prodMap.put("productModelIdentifier", "PP");
        productList.add(prodMap);
      }
      Map<String, Object> prods = new HashMap<>();
      prods.put("products", productList);
      requestMap.put("scope", prods);
    }
    requestMap.put("subscriptionDate", String.valueOf(requestMap.get("startDate")));
    return requestMap;
  }

  private Map<String, Object> transformJson(Map<String, Object> inputMap, String spec,
      String specPath) {
    return Utils.transformJsonAsMap(inputMap, spec, specPath);
  }

}
