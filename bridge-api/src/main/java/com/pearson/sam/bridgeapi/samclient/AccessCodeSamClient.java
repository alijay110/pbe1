package com.pearson.sam.bridgeapi.samclient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.pearson.sam.bridgeapi.model.AccessCodes;
import com.pearson.sam.bridgeapi.samservices.AccessCodeServices;
import com.pearson.sam.bridgeapi.util.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("unchecked")
public class AccessCodeSamClient {

	private static final Logger logger = LoggerFactory.getLogger(AccessCodeSamClient.class);
	private final GsonBuilder gsonBuilder = new GsonBuilder();
	private final Gson gson = gsonBuilder.setPrettyPrinting().disableHtmlEscaping().create();
	private static final String ACCESS_CODE = "accessCode";
	private static final String CREATED_DATE = "createdDate";
	private static final String DATE_CREATED = "dateCreated";

	@Value("${SPEC_FILE}")
	private String specJsonFile;

	@Autowired
	AccessCodeServices accessCodeServices;

	public AccessCodes create(JsonObject documents) {
		//logger.info("Inside Save {}", documents);
		Map<String, Object> requestMap = getScopeNProduct(documents);
		requestMap.put(DATE_CREATED, new Date().toString());

		Map<String, Object> accessCodeRequestMap = transformJson(requestMap, "access_code_creation", specJsonFile);
		String requestJson = gson.toJson(accessCodeRequestMap);
		Map<String, Object> resultMap = accessCodeServices.createAccessCode(requestJson);
		String accessCodeValue = (String) resultMap.get(ACCESS_CODE);
		String createdDate = (String) resultMap.get(CREATED_DATE);
		AccessCodes accessCodes = new AccessCodes();
		accessCodes.setCode(accessCodeValue);
		accessCodes.setAccessCode(accessCodeValue);
		accessCodes.setDateCreated(createdDate);
		return accessCodes;
	}

	public List<AccessCodes> createMultiple(JsonObject documents) {
		Map<String, Object> requestMap = getScopeNProduct(documents);
		List<AccessCodes> result = new ArrayList<>();
		
		Map<String, Object> accessCodeRequestMap = transformJson(requestMap, "access_code_creation", specJsonFile);
		String requestJson = gson.toJson(accessCodeRequestMap);
		Map<String, Object> resultMap = accessCodeServices.createAccessCodeInBatch(requestJson);

		List<Map<String, Object>> li = Utils.convert(resultMap.get("accessCodeList"),
				new TypeReference<List<Map<String, Object>>>() {
				});

		List<Map<String, Object>> prods = null;
		for (Map<String, Object> mp : li) {
			mp.remove("message");
			Object obj1 = mp.remove(CREATED_DATE);
			Object obj2 = mp.remove("batchIdentifier");
			mp.put(DATE_CREATED, obj1);
			mp.put("batch", obj2);
			prods = (List<Map<String, Object>>) mp.get("products");
			if (null != prods) {
				for (Map<String, Object> item : prods) {
					// mp.remove("products");

					mp.put("products", Stream.of(item.get("orgIdentifier")).collect(Collectors.toList()));

				}
			}
			// mp.remove("products");
			result.add(Utils.convert(mp, new TypeReference<AccessCodes>() {
			}));
		}

		return result;

	}

	public AccessCodes update(Map<String, String> params, JsonObject documents) {

		Map<String, Object> requestMap = getScopeNProduct(documents);
		Map<String, Object> accessCodeRequestMap = transformJson(requestMap, "access_code_updation", specJsonFile);
		String requestJson = gson.toJson(accessCodeRequestMap);
		String accessCode = params.get(ACCESS_CODE);
		Map<String, Object> resultMap = accessCodeServices.configueAccessCode(accessCode, requestJson);

		String accessCodeId = (String) resultMap.get(ACCESS_CODE);
		AccessCodes accessCodes = new AccessCodes();
		accessCodes.setAccessCode(accessCodeId);
		accessCodes.setCode(accessCodeId);
		return accessCodes;

	}

	public AccessCodes fetchOne(JsonObject documents) {
		Map<String, Object> requestMap = gson.fromJson(documents, HashMap.class);
		String accessCodeId = requestMap.get("accessCodeId").toString();
		Map<String, Object> accessCodeRequestMap = accessCodeServices.getAccessCodeDetails(accessCodeId);
		Map<String, Object> scope = (Map<String, Object>) accessCodeRequestMap.get("scope");
		List<String> products = new ArrayList<String>();
		if (Objects.nonNull(scope)) {
			List<Map<String, Object>> prods = (List<Map<String, Object>>) scope.get("products");
			for (Map<String, Object> item : prods) {
				products.add((String) item.get("orgIdentifier"));
			}
		}
		accessCodeRequestMap.put("products", products);
		// SAM response payload contains the below fields but these fields are
		// not availabe in GraphQL side
		// we need to remove the below fields.
		accessCodeRequestMap.remove("identifier");
		accessCodeRequestMap.remove("createdDate");
		accessCodeRequestMap.remove("batchIdentifier");
		accessCodeRequestMap.remove("createdOn");
		accessCodeRequestMap.remove("updatedOn");
		accessCodeRequestMap.remove("updatedBy");
		accessCodeRequestMap.remove("scope");
		accessCodeRequestMap.remove("codeType");
		// End remove
		return Utils.convert(accessCodeRequestMap, new TypeReference<AccessCodes>() {
		});
	}

	public List<AccessCodes> fetchMultiple(List<String> ids) {
		return Collections.emptyList();
	}

	public AccessCodes delete(String id) {
		return null;
	}

	public Map<String, Object> addAccessCodes(String bodyString) {
		return accessCodeServices.addAccessCodes(bodyString);
	}
	
	public Map<String, Object> getAllSubscriptionDetailsofUser(String userName){
		return accessCodeServices.getAllSubscriptionDetailsofUser(userName);
	}

	private Map<String, Object> getScopeNProduct(JsonObject documents) {
		Map<String, Object> requestMap = gson.fromJson(documents, HashMap.class);
		List<String> products = (List<String>) requestMap.get("products");
		if (null != products) {
			List<Map<String, String>> productList = new ArrayList<>();

			for (String product : products) {
				Map<String, String> prodMap = new HashMap<>();
				prodMap.put("orgIdentifier", product);
				prodMap.put("productIdentifier", product);
				prodMap.put("productModelIdentifier", "PP");
				productList.add(prodMap);
			}
			Map<String, Object> prods = new HashMap<>();
			prods.put("products", productList);
			requestMap.put("scope", prods);
		}
		return requestMap;
	}

	private Map<String, Object> transformJson(Map<String, Object> inputMap, String spec, String specPath) {
		return Utils.transformJsonAsMap(inputMap, spec, specPath);
	}

}
