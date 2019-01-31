package com.pearson.sam.bridgeapi.samclient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.pearson.sam.bridgeapi.model.Licence;
import com.pearson.sam.bridgeapi.samservices.AccessCodeServices;
import com.pearson.sam.bridgeapi.util.Utils;

@Component
public class LicenceSamClient {

  private static final Logger logger = LoggerFactory.getLogger(LicenceSamClient.class);

  private final GsonBuilder gsonBuilder = new GsonBuilder();
  private final Gson gson = gsonBuilder.setPrettyPrinting().disableHtmlEscaping().create();
  private static final String ACCESS_CODE = "accessCode";
  private static final String CREATED_DATE = "createdDate";
  private static final String PRODUCTS = "products";
  private static final String LICENCE_ID = "licenceId";

  @Value("${SPEC_FILE}")
  private String specJsonFile;

  @Autowired
  AccessCodeServices accessCodeServices;

  public void init(String collectionName) {
    //logger.info("where is this init method being used??");
  }

  @SuppressWarnings("unchecked")
  public Licence create(JsonObject documents) {

    Map<String, Object> requestMap = gson.fromJson(documents, HashMap.class);
    List<String> products = (List<String>) requestMap.get(PRODUCTS);
    List<Map<String, String>> productList = new ArrayList<>();

    if (null != products) {
      for (String product : products) {
        Map<String, String> prodMap = new HashMap<>();
        prodMap.put("orgIdentifier", product);
        prodMap.put("productIdentifier", product);
        prodMap.put("productModelIdentifier", "PP");
        productList.add(prodMap);
      }
      Map<String, Object> prods = new HashMap<>();
      prods.put(PRODUCTS, productList);
      requestMap.put("scope", prods);
    }
    Map<String, Object> licenceRequestMap = transformJson(requestMap, "access_code_creation",
        specJsonFile);
    String requestJson = gson.toJson(licenceRequestMap);
    Map<String, Object> resultMap = accessCodeServices.createAccessCode(requestJson);
    String licenceValue = (String) resultMap.get(ACCESS_CODE);
    String createdDate = (String) resultMap.get(CREATED_DATE);
    Licence licence = new Licence();
    licence.setLicenceId(licenceValue);
    licence.setDateCreated(createdDate);
    return licence;
  }

  @SuppressWarnings("unchecked")
  public Licence update(Map<String, String> params, JsonObject documents) {

    Map<String, Object> requestMap = gson.fromJson(documents, HashMap.class);
    List<String> products = (List<String>) requestMap.get(PRODUCTS);
    List<Map<String, String>> productList = new ArrayList<>();

    if (null != products) {
      for (String product : products) {
        Map<String, String> prodMap = new HashMap<>();
        prodMap.put("productCode", product);
        prodMap.put("productModel", "PP");
        productList.add(prodMap);
      }
      Map<String, Object> prods = new HashMap<>();
      prods.put(PRODUCTS, productList);
      requestMap.put("scope", prods);
    }
    Map<String, Object> licenceRequestMap = transformJson(requestMap, "licence_creation",
        specJsonFile);
    String requestJson = gson.toJson(licenceRequestMap);

    Map<String, Object> resultMap = accessCodeServices.configueAccessCode(params.get(LICENCE_ID),
        requestJson);
    String licenceId = (String) resultMap.get(ACCESS_CODE);
    Licence licence = new Licence();
    licence.setLicenceId(licenceId);

    return licence;
  }

  public Licence fetchOne(JsonObject documents) {
    Map<String, Object> requestMap = Utils.convertToMap(gson.fromJson(documents, HashMap.class));

    String licence = requestMap.get(ACCESS_CODE).toString();
    Map<String, Object> resultMap = accessCodeServices.getAccessCodeDetails(licence);
    Map<String, Object> licenceRequestMap = transformJson(resultMap, "get_access_code",
        specJsonFile);
    Object obj = licenceRequestMap.remove("accessCode");
    licenceRequestMap.remove("batch");
    licenceRequestMap.put(LICENCE_ID, obj);
    return Utils.convert(licenceRequestMap, new TypeReference<Licence>() {
    });
  }

  public List<Licence> fetchMultiple(List<String> ids) {
    return Collections.emptyList();
  }

  public Licence delete(String id) {
    return null;
  }

  private Map<String, Object> transformJson(Map<String, Object> inputMap, String spec,
      String specPath) {
    return Utils.transformJsonAsMap(inputMap, spec, specPath);
  }

  public List<Licence> createMultiple(JsonObject documents) {
    return Collections.emptyList();
  }

}
