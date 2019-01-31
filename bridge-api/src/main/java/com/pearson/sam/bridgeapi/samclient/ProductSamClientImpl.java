package com.pearson.sam.bridgeapi.samclient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.pearson.sam.bridgeapi.model.Product;
import com.pearson.sam.bridgeapi.samservices.ProductServices;
import com.pearson.sam.bridgeapi.util.Utils;

/**
 * 
 * @author VKu99Ma
 *
 */
@Service
public class ProductSamClientImpl implements IProductSamClient {

	private final GsonBuilder gsonBuilder = new GsonBuilder();
	private final Gson gson = gsonBuilder.setPrettyPrinting().disableHtmlEscaping().create();
	private static final String PRODUCT_ID = "productId";
	private static final String PRODUCT_IDENTIFIER = "productIdentifier";

	private static final Logger logger = LoggerFactory.getLogger(ProductSamClientImpl.class);

	@Value("${SPEC_FILE}")
	private String specJsonFile;

	@Autowired
	ProductServices productServices;

	public Product create(JsonObject documents) {
		String requestJson = gson.toJson(documents);
		Map<String, Object> resultMap = productServices.createProduct(requestJson);
		String productId = (String) resultMap.get(PRODUCT_IDENTIFIER);
		Product product = new Product();
		product.setProductId(productId);
		return product;
	}

	@Override
	public Product fetchOne(JsonObject documents) {
		String productId = documents.get(PRODUCT_ID).getAsString();
		Map<String, Object> resultMap = productServices.getProduct(productId);
		Map<String, Object> productMap = transformJson(resultMap, "ProductResponse", specJsonFile);
		/*List<Map<String, Object>> li = Utils.convert(productMap,
				new TypeReference<List<Map<String, Object>>>() {
				});
		Map<String, Object> mp = li.get(0);
		Object obj = mp.remove(PRODUCT_IDENTIFIER);
		mp.put(PRODUCT_ID, obj);
		Object obj1 = mp.remove("ISBN");
		mp.put("isbn", obj1);*/
		Product prod = Utils.convert(productMap, new TypeReference<Product>() {
		});
		
		
		
		return Utils.convert(productMap, new TypeReference<Product>() {
		});
	}
	
	@Override
	public List<Product> fetchMultiple(List<String> ids) {
		List<Product> result = new ArrayList<>();
	
		Map<String, Object> resultMap = productServices.getProducts();
		List<Map<String, Object>> li = Utils.convert(resultMap.get("product"),
		new TypeReference<List<Map<String, Object>>>() {
		});
		for (Map<String, Object> mp : li) {
			Map<String, Object> productMap = transformJson(mp, "MultipleProductsResponse", specJsonFile);
			if(productMap == null){
			System.out.println("");
			}			
			result.add(Utils.convert(productMap, new TypeReference<Product>() {
			}));
	}
	return result;
	}
	
	@Override
	public Product update(Map<String, String> params, JsonObject documents) {
		String requestJson = gson.toJson(documents);
		Map<String, Object> resultMap = productServices.updateProduct(params.get("productIdentifier"), requestJson);
		String productId = (String) resultMap.get("productIdentifier");
		Product product = new Product();
		product.setProductId(productId);
		return product;
	}

	private Map<String, Object> transformJson(Map<String, Object> inputMap, String spec, String specPath) {
		return Utils.transformJsonAsMap(inputMap, spec, specPath);
	}

	@Override
	public Map<String, Object> getLearningMaterial(String orgId, String courseId, String userId) {
		return   productServices.getLearningMaterial(orgId, courseId, userId);
	}
	
	
	

}
