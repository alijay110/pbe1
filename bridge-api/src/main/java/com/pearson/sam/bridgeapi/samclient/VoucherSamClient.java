package com.pearson.sam.bridgeapi.samclient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.pearson.sam.bridgeapi.model.Voucher;
import com.pearson.sam.bridgeapi.samservices.AccessCodeServices;
import com.pearson.sam.bridgeapi.util.Utils;

import java.util.ArrayList;
import java.util.Collections;
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
public class VoucherSamClient {

  private static final Logger logger = LoggerFactory.getLogger(VoucherSamClient.class);
  private final GsonBuilder gsonBuilder = new GsonBuilder();
  private final Gson gson = gsonBuilder.setPrettyPrinting().disableHtmlEscaping().create();
  private static final String ACCESS_CODE = "accessCode";
  private static final String CREATED_DATE = "createdDate";
  private static final String PRODUCTS = "products";

  @Autowired
  AccessCodeServices accessCodeServices;

  @Value("${SPEC_FILE}")
  private String specJsonFile;

  public void init(String collectionName) {
    //logger.info("what is the use of this method here?");

  }

  public Voucher create(JsonObject documents) {

    Map<String, Object> requestMap = gson.fromJson(documents, HashMap.class);
    List<String> products = (List<String>) requestMap.get(PRODUCTS);
    requestMap.put("dateCreated", new Date().toString());
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

    Map<String, Object> accessCodeRequestMap = transformJson(requestMap, "access_code_creation",
        specJsonFile);
    String requestJson = gson.toJson(accessCodeRequestMap);
    Map<String, Object> resultMap = accessCodeServices.createAccessCode(requestJson);
    String voucherValue = (String) resultMap.get(ACCESS_CODE);
    String createdDate = (String) resultMap.get(CREATED_DATE);
    Voucher voucher = new Voucher();
    voucher.setVoucherCode(voucherValue);
    voucher.setDateCreated(createdDate);
    return voucher;
  }

  public Voucher update(Map<String, String> params, JsonObject documents) {

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

    Map<String, Object> accessCodeRequestMap = transformJson(requestMap, "voucher_creation",
        specJsonFile);
    String requestJson = gson.toJson(accessCodeRequestMap);
    String accessCode = params.get(ACCESS_CODE);
    Map<String, Object> resultMap = accessCodeServices.configueAccessCode(accessCode, requestJson);

    String voucherId = (String) resultMap.get(ACCESS_CODE);
    Voucher voucher = new Voucher();
    voucher.setVoucherId(voucherId);
    voucher.setVoucherCode(voucherId);
    return voucher;

  }

  public Voucher fetchOne(JsonObject documents) {
    Map<String, Object> requestMap = gson.fromJson(documents, HashMap.class);

    String accessCodeId = requestMap.get("accessCodeId").toString();

    Map<String, Object> resultMap = accessCodeServices.getAccessCodeDetails(accessCodeId);
    Map<String, Object> accessCodeRequestMap = transformJson(resultMap, "get_voucher",
        specJsonFile);

    return Utils.convert(accessCodeRequestMap, new TypeReference<Voucher>() {
    });
  }

  public List<Voucher> fetchMultiple(List<String> ids) {
    return Collections.emptyList();
  }

  public Voucher delete(String id) {
    return null;
  }

  private Map<String, Object> transformJson(Map<String, Object> inputMap, String spec,
      String specPath) {
    return Utils.transformJsonAsMap(inputMap, spec, specPath);
  }

  public List<Voucher> createMultiple(JsonObject documents) {
    return Collections.emptyList();
  }

}
