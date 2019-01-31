package com.pearson.sam.bridgeapi.serviceimpl;

import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.MONGO_MAP;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.SAM_MAP;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.CAN_NOT_PASS_CREATEDBY_CREATEDON_WHILE_UPDATE_ORGANISATION;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.CAN_NOT_PASS_UPDATEDBY_UPDATEDON_WHILE_CREATE_ORGANISATION;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.DATA_NOT_FOUND;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.NAME_SHOULD_BE_PRESENT;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.pearson.sam.bridgeapi.annotations.MongoSource;
import com.pearson.sam.bridgeapi.annotations.SamSource;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGraphqlException;
import com.pearson.sam.bridgeapi.iservice.IOrganisationService;
import com.pearson.sam.bridgeapi.model.KeyType;
import com.pearson.sam.bridgeapi.model.Organisation;
import com.pearson.sam.bridgeapi.repository.OrganisationRepository;
import com.pearson.sam.bridgeapi.samclient.IOrganisationSamClient;
import com.pearson.sam.bridgeapi.util.Utils;
import com.pearson.sam.bridgeapi.validators.ModelValidator;

import graphql.ErrorType;

@Service
public class OrganisationServiceImpl implements IOrganisationService {
	private static final Logger logger = LoggerFactory.getLogger(OrganisationServiceImpl.class);

	private final GsonBuilder gsonBuilder = new GsonBuilder();

	private final Gson gson = gsonBuilder.disableHtmlEscaping().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
			.setPrettyPrinting().serializeNulls().create();

	private static final String CREATE_ORG = "createOrg";
	private static final String UPDATE_ORG = "updateOrg";
	private static final String CREATE = "create";
	private static final String UPDATE = "update";
	private static final String ORG_ID = "organisationId";
	private static final String GET = "get";
	private static final String DEACTIVATE = "deactivate";

	@Value("${SPEC_FILE}")
	private String specJsonFile;

	@Autowired
	private IOrganisationSamClient<Organisation> organisationSamClient;

	@Autowired
	private OrganisationRepository organisationRepository;
	
	@Autowired
	ModelValidator validator;

	@Override
	public Organisation create(Organisation organisation) {

		//logger.info("Creating Organisation {}");
		validateOrganisation(organisation,CREATE);
		Organisation responseOrganisation = new Organisation();
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		// Setting Organization Values
		organisation.setOrganisationId(generateOrganisationId(organisation.getName()));
		organisation.setCreatedOn(Utils.isNotEmpty(organisation.getCreatedOn()) ? organisation.getCreatedOn() : Utils.generateEpochDate());
		Map<String, Map<String, Object>> segregatedData = segregatedData(organisation);
		Map<String, Object> samMap = segregatedData.get(SAM_MAP);
		samMap.put("productModelIdentifier", "PP");
		samMap.put("activeTimestamp", timestamp);

		Map<String, Object> mongoMap = segregatedData.get(MONGO_MAP);

		Map<String, Object> transformedMap = transformJson(samMap, CREATE_ORG, specJsonFile);
		transformedMap.put("serviceType", "organization");
		JsonObject documents = gson.toJsonTree(transformedMap).getAsJsonObject();
		responseOrganisation = organisationSamClient.create(documents);
		responseOrganisation.setCreatedBy(organisation.getCreatedBy());
		responseOrganisation.setCreatedOn(organisation.getCreatedOn());
		Map<String, Object> OrgMap = Utils.convertToMap(responseOrganisation);
		OrgMap.put("name", samMap.get("name"));
		Organisation mergedData = mergedData(OrgMap, mongoMap, CREATE);

		return mergedData;
	}

	@Override
	public Organisation update(Organisation organisation) {
		// TODO Auto-generated method stub
		Organisation mergedData = null;
		
		validator.validateEmptyNullValue(organisation.getOrganisationId(), ORG_ID);
		validateOrganisation(organisation,UPDATE);
		organisation.setUpdatedOn(Utils.isNotEmpty(organisation.getUpdatedOn()) ? organisation.getUpdatedOn() : Utils.generateEpochDate());
		Map<String, Map<String, Object>> segregatedData = segregatedData(organisation);
		Map<String, String> orgId = new HashMap<>();
		orgId.put(ORG_ID, organisation.getOrganisationId());
		Map<String, Object> samMap = segregatedData.get("samMap");
		samMap.put("productModelIdentifier", "PP");

		Map<String, Object> mongoMap = segregatedData.get("mongoMap");

		Map<String, Object> transformedMap = transformJson(samMap, UPDATE_ORG, specJsonFile);
		transformedMap.put("serviceType", "organization");
		JsonObject documents = gson.toJsonTree(transformedMap).getAsJsonObject();
		documents.remove("orgIdentifier");
		Organisation responseSchool = organisationSamClient.update(orgId, documents);
		Map<String, Object> schoolMap = Utils.convertToMap(responseSchool);
		mergedData = mergedData(schoolMap, mongoMap, UPDATE);
		return fetch(mergedData);
	}

	@Override
	public Organisation fetch(Organisation organisation) {
		// TODO Auto-generated method stub
		Organisation mergedData = null;
		Map<String, Map<String, Object>> segregatedData = segregatedData(organisation);
		Map<String, Object> samMap = segregatedData.get(SAM_MAP);
		Map<String, Object> mongoMap = segregatedData.get(MONGO_MAP);
		Map<String, Object> transformedMap = transformJson(samMap, CREATE_ORG, specJsonFile);
		transformedMap.put("serviceType", "organization");
		JsonObject documents = gson.toJsonTree(transformedMap).getAsJsonObject();
		Organisation responseSchool = organisationSamClient.fetchOne(documents);
		Map<String, Object> schoolMap = Utils.convertToMap(responseSchool);
		mergedData = mergedData(schoolMap, mongoMap, GET);
		return mergedData;
	}

	@Override
	public Page<Organisation> pageIt(Pageable pageable, Example<Organisation> e) {
		// TODO Auto-generated method stub
		return organisationRepository.findAll(e, pageable);
	}

	@Override
	public Page<Organisation> pageIt(Pageable pageable, String name, List<String> organisationIds) {
		// TODO Auto-generated method stub
		return organisationRepository.findAllByOrganisationIdIn(organisationIds, pageable);
	}

	@Override
	public Organisation findByOrganisationId(String organisationId) {
		// TODO Auto-generated method stub
		Organisation responseOrganisation = new Organisation();
		Organisation organisation = new Organisation();
		organisation = organisationRepository.findByOrganisationId(organisationId);
		Map<String, Object> dataMap = Utils.convertToMap(organisation);
		Map<String, Object> transformedMap = transformJson(dataMap, CREATE_ORG, specJsonFile);
		transformedMap.put("serviceType", "organization");
		JsonObject documents = gson.toJsonTree(transformedMap).getAsJsonObject();
		responseOrganisation = organisationSamClient.fetchOne(documents);
		Utils.copyNonNullProperties(organisation, responseOrganisation);

		return responseOrganisation;
	}

	@Override
	public Organisation delete(Organisation organisation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Organisation> fetchMultiple(KeyType key, List<String> ids) {
		// TODO Auto-generated method stub
		return null;
	}

	private String generateOrganisationId(String organisationName) {
		if(Utils.isEmpty(organisationName)) {
			throw new BridgeApiGraphqlException(ErrorType.ValidationError, NAME_SHOULD_BE_PRESENT);
		}
		String organisationId = "";
		String names[] = organisationName.trim().split(" ");
		String firstName = names[0];
		String secondName = "";
		if (names.length > 1 && StringUtils.isNotEmpty(names[1]))
			secondName = names[1];

		if (StringUtils.isNotEmpty(firstName) && StringUtils.isNotEmpty(secondName)) {
			organisationId = firstName.substring(0, 1) + secondName.substring(0, 1);
		} else if (StringUtils.isNotEmpty(firstName) && StringUtils.isEmpty(secondName)) {
			if (firstName.length() == 2) {
				organisationId = organisationName;
			} else if (firstName.length() == 1) {
				organisationId = firstName + "0";
			} else {
				organisationId = firstName.substring(0, 2);
			}
		}
		organisationId += "O";
		organisationId = organisationId + Utils.generateRandomNumber(100000, 900000);
		return organisationId;
	}

	private Map<String, Map<String, Object>> segregatedData(Organisation organisation) {
		Map<String, Map<String, Object>> segregatedMap = new HashMap<>();

		Map<String, Object> dataMap = Utils.convertToMap(organisation);

		List<String> mongoFields = Utils.extractAnnotatedFieldNames(Organisation.class, MongoSource.class);
		List<String> samFields = Utils.extractAnnotatedFieldNames(Organisation.class, SamSource.class);

		Map<String, Object> samMap = new HashMap<>(dataMap);
		samMap.keySet().retainAll(samFields);
		Map<String, Object> mongoMap = dataMap;
		mongoMap.keySet().retainAll(mongoFields);

		segregatedMap.put(SAM_MAP, samMap);
		segregatedMap.put(MONGO_MAP, mongoMap);

		return segregatedMap;
	}

	private Map<String, Object> transformJson(Map<String, Object> inputMap, String spec, String specPath) {
		return Utils.transformJsonAsMap(inputMap, spec, specPath);
	}

	private Organisation mergedData(Map<String, Object> orgMap, Map<String, Object> mongoMap, String type) {
		mongoMap.put(ORG_ID, orgMap.get(ORG_ID));
		if (type.equals(UPDATE)) {
			Organisation dbUser = organisationRepository.findByOrganisationId(orgMap.get(ORG_ID).toString());
			Organisation org = Utils.convert(mongoMap, new TypeReference<Organisation>() {
			});
			Utils.copyNonNullProperties(org, dbUser);
			organisationRepository.save(dbUser);
		} else if (type.equals(GET)) {
			Organisation dbUser = organisationRepository.findByOrganisationId(orgMap.get(ORG_ID).toString());
			mongoMap = Utils.convertToMap(dbUser);
		} else if (type.equals(DEACTIVATE)) {
			Organisation dbUser = organisationRepository.findByOrganisationId(mongoMap.get(ORG_ID).toString());
			checkAvailable(dbUser);
			organisationRepository.save(dbUser);
			mongoMap = Utils.convertToMap(dbUser);
		} else {
			organisationRepository.save(Utils.convert(mongoMap, new TypeReference<Organisation>() {
			}));
		}

		Map<String, Object> mergedUserMap = new HashMap<>();
		mergedUserMap.putAll(mongoMap);
		mergedUserMap.putAll(orgMap);
		return Utils.convert(mergedUserMap, new TypeReference<Organisation>() {
		});
	}

	private void checkAvailable(Organisation obj) {
		if (obj == null) {
			throw new BridgeApiGraphqlException(ErrorType.DataFetchingException, DATA_NOT_FOUND);
		}
	}
	
	private void validateOrganisation(Organisation organisation, String operation) {
		if (operation.equals(CREATE)) {
			if (null!= organisation.getUpdatedBy() || null != organisation.getUpdatedOn()) {
				throw new BridgeApiGraphqlException(ErrorType.DataFetchingException,
						CAN_NOT_PASS_UPDATEDBY_UPDATEDON_WHILE_CREATE_ORGANISATION);
			}
		} else if (null != organisation.getCreatedBy() || null != organisation.getCreatedOn()) {
			throw new BridgeApiGraphqlException(ErrorType.DataFetchingException,
					CAN_NOT_PASS_CREATEDBY_CREATEDON_WHILE_UPDATE_ORGANISATION);
		}
	}

}
