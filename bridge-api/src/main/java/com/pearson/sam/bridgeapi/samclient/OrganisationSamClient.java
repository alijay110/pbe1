package com.pearson.sam.bridgeapi.samclient;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGraphqlException;
import com.pearson.sam.bridgeapi.model.Organisation;
import com.pearson.sam.bridgeapi.model.School;
import com.pearson.sam.bridgeapi.resolvers.ViewerResolver;
import com.pearson.sam.bridgeapi.samservices.OrganizationServices;
import com.pearson.sam.bridgeapi.util.Utils;

import graphql.ErrorType;

/**
 * Schoool integ.
 * 
 * @author VGUDLSA
 * @param <T>
 *
 */

@Component
public class OrganisationSamClient<T> implements IOrganisationSamClient<T> {

	@Autowired
	OrganizationServices organizationServices;

	private static final Logger logger = LoggerFactory.getLogger(ViewerResolver.class);
	static RestTemplate restTemplate = new RestTemplateBuilder().build();

	private final GsonBuilder gsonBuilder = new GsonBuilder();
	private final Gson gson = gsonBuilder.disableHtmlEscaping().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
			.setPrettyPrinting().serializeNulls().create();

	public OrganisationSamClient() {
		//logger.info("-----*********** Inisde SchoolIntegration *****************------------");
	}

	String samUrl;

	@Value("${SPEC_FILE}")
	private String specJsonFile;

	public void init(String collectionName) {
		//logger.info("init {}", collectionName);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T create(JsonObject documents) {
		boolean status = getServiceType(documents);
		String requestJson = gson.toJson(documents);
		// TO DO as part of refactoring - why null? is it required?
		Map<String, Object> resultMap = organizationServices.createOrganization(null, requestJson);
		if (status)
			return (T) Utils.convert(Utils.transformJson(resultMap, "OrgGetResponse", specJsonFile),
					new TypeReference<Organisation>() {
					});
		else
			return (T) Utils.convert(Utils.transformJson(resultMap, "SchoolGetResponse", specJsonFile),
					new TypeReference<School>() {
					});
	}

	@SuppressWarnings("unchecked")
	@Override
	public T update(Map<String, String> params, JsonObject documents) {
		Map<String, Object> resultMap = null;
		boolean status = getServiceType(documents);
		String requestJson = gson.toJson(documents);
		String org = params.get("schoolId");

		if (status) {
			org = params.get("organisationId");
		}

		try {
			resultMap = organizationServices.updateOrganization(org, requestJson);
		} catch (Exception e) {
			throw new BridgeApiGraphqlException(ErrorType.DataFetchingException, e.getMessage());
		}

		if (status)
			return (T) Utils.convert(Utils.transformJson(resultMap, "OrgGetResponse", specJsonFile),
					new TypeReference<Organisation>() {
					});
		else
			return (T) Utils.convert(Utils.transformJson(resultMap, "SchoolGetResponse", specJsonFile),
					new TypeReference<School>() {
					});

	}

	private boolean getServiceType(JsonObject documents) {
		String serviceType = documents.get("serviceType").getAsString();
		boolean status = (serviceType == "organization") ? true : false;
		documents.remove("serviceType");
		return status;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T fetchOne(JsonObject documents) {
		boolean status = getServiceType(documents);
		String org = documents.get("orgIdentifier").getAsString();
		Map<String, Object> resultMap = organizationServices.getOrganization(org);
		if (status)
			return (T) Utils.convert(Utils.transformJson(resultMap, "OrgGetResponse", specJsonFile),
					new TypeReference<Organisation>() {
					});
		else
			return (T) Utils.convert(Utils.transformJson(resultMap, "SchoolGetResponse", specJsonFile),
					new TypeReference<School>() {
					});

	}

	@SuppressWarnings("unchecked")
	@Override
	public T deactivate(String orgIdentifier, JsonObject documents) {
		String requestJson = gson.toJson(documents);

		Map<String, Object> resultMap = organizationServices.deactivateOrganization(orgIdentifier, requestJson);

		return (T) Utils.convert(Utils.transformJson(resultMap, "SchoolResponse", specJsonFile),
				new TypeReference<School>() {
				});
	}
	@Override
	public List<T> fetchMultiple(List<String> ids) {
		return Collections.emptyList();
	}
	
	@Override
	public T delete(String id) {
		return null;
	}
	
	@Override
	public List<T> createMultiple(JsonObject documents) {
		return Collections.emptyList();
	}
}
