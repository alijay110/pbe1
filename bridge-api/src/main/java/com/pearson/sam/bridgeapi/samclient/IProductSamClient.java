package com.pearson.sam.bridgeapi.samclient;

import java.util.List;
import java.util.Map;

import com.google.gson.JsonObject;
import com.pearson.sam.bridgeapi.model.Product;

public interface IProductSamClient {
	/**
	 * This Method will add the Product in SAM.
	 * 
	 * @param documents
	 * @return
	 */
	public Product create(JsonObject documents);

	/**
	 * This Method will fetch one product from SAM.
	 * 
	 * @param documents
	 * @return
	 */
	public Product fetchOne(JsonObject documents);
	
	/**
	 * This Method will fetch multiple product from SAM based on input ids.
	 * 
	 * @param ids
	 * @return
	 */
	public List<Product> fetchMultiple(List<String> ids);
	
	/**
	 * This Method will update the product.
	 * 
	 * @param params
	 * @param documents
	 * @return
	 */
	public Product update(Map<String, String> params, JsonObject documents);
	
	/**
	 * 
	 * @param AssetsList
	 * @return
	 */
	
	public Map<String, Object> getLearningMaterial(String orgId,String courseId, String userId);
}
