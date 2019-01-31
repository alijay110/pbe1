package com.pearson.sam.bridgeapi.samclient;

import java.util.List;
import java.util.Map;

import com.google.gson.JsonObject;

public interface IOrganisationSamClient<T> {
	/**
	 * 
	 * @param documents
	 * @return T
	 */
	T create(JsonObject documents);
	/**
	 * 
	 * @param params
	 * @param documents
	 * @return T
	 */
	T update(Map<String, String> params, JsonObject documents);
	/**
	 * 
	 * @param documents
	 * @return T
	 */
	T fetchOne(JsonObject documents);
	/**
	 * 
	 * @param orgIdentifier
	 * @param documents
	 * @return T
	 */
	T deactivate(String orgIdentifier, JsonObject documents);
	/**
	 * 
	 * @param ids
	 * @return List<T>
	 */
	List<T> fetchMultiple(List<String> ids);
	/**
	 * 
	 * @param id
	 * @return T
	 */
	T delete(String id);
	/**
	 * 
	 * @param documents
	 * @return List<T>
	 */
	List<T> createMultiple(JsonObject documents);

}
