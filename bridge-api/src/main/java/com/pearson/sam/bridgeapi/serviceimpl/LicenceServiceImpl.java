package com.pearson.sam.bridgeapi.serviceimpl;

import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.MONGO_MAP;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.SAM_MAP;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.DATA_NOT_FOUND;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.UNABLE_TO_FIND_THE_LICENCE_ID;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.pearson.sam.bridgeapi.annotations.MongoSource;
import com.pearson.sam.bridgeapi.annotations.SamSource;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGeneralException;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGraphqlException;
import com.pearson.sam.bridgeapi.iservice.ILicenceService;
import com.pearson.sam.bridgeapi.model.KeyType;
import com.pearson.sam.bridgeapi.model.Licence;
import com.pearson.sam.bridgeapi.model.LicenceHistory;
import com.pearson.sam.bridgeapi.repository.LicenceHistoryRespository;
import com.pearson.sam.bridgeapi.repository.LicenceRespository;
import com.pearson.sam.bridgeapi.samclient.LicenceSamClient;
import com.pearson.sam.bridgeapi.security.ISessionFacade;
import com.pearson.sam.bridgeapi.util.Utils;

import graphql.ErrorType;

@Service
public class LicenceServiceImpl implements ILicenceService {
	private static final Logger logger = LoggerFactory.getLogger(LicenceServiceImpl.class);
	private final GsonBuilder gsonBuilder = new GsonBuilder();
	private final Gson gson = gsonBuilder.setPrettyPrinting().disableHtmlEscaping().create();
	private static final String ACCESS_CODE = "accessCode";

	@Autowired
	public LicenceRespository licenceRespository;

	@Autowired
	LicenceSamClient licenceRestIntegration;

	@Autowired
	public LicenceHistoryRespository licenceHistoryRespository;

	@Autowired
	protected ISessionFacade sessionFacade;

	@Override
	public Licence create(Licence licence) {
		// TODO Auto-generated method stub
		licence.setDateCreated(Utils.isNotEmpty(licence.getDateCreated()) ? licence.getDateCreated() : Utils.generateEpochDate());
		licence.setLastActivatedDate(Utils.isNotEmpty(licence.getLastActivatedDate()) ? licence.getLastActivatedDate() : Utils.generateEpochDate());
		licence.setLastActivatedBy(sessionFacade.getLoggedInUser(true).getUid());
		Map<String, Map<String, Object>> segregatedDataMap = segregatedData(licence);
		if (null != licence) {
			licence.setCreationDate(Utils.isNotEmpty(licence.getCreationDate()) ? licence.getCreationDate() : Utils.generateEpochDate());
			JsonObject documents = gson.toJsonTree(segregatedDataMap.get(SAM_MAP)).getAsJsonObject();
			try {
				documents.addProperty(ACCESS_CODE, licence.getLicenceId());
				Licence restObj = licenceRestIntegration.create(documents);
				licence.setLicenceId(restObj.getLicenceId());
				licence.setDateCreated(restObj.getDateCreated());
				licence.setCreatedBy(licence.getCreatedBy());
				licence = licenceRespository.save(licence);
				updateLicenceHistory(licence);
			} catch (BridgeApiGeneralException e) {
				throw new BridgeApiGraphqlException(e.getMessage());
			}
		}
		return licence;
	}

	@Override
	public Licence update(Licence licence) {
		// TODO Auto-generated method stub
		//logger.info("update Access Code {}", licence);
		licence.setDateCreated(Utils.isNotEmpty(licence.getDateCreated()) ? licence.getDateCreated() : Utils.generateEpochDate());
		licence.setCreationDate(Utils.isNotEmpty(licence.getCreationDate()) ? licence.getCreationDate() : Utils.generateEpochDate());
		licence.setLastActivatedDate(Utils.isNotEmpty(licence.getLastActivatedDate()) ? licence.getLastActivatedDate() : Utils.generateEpochDate());
		licence.setLastActivatedBy(sessionFacade.getLoggedInUser(true).getUid());
		Licence mongoLicence = licenceRespository.findByLicenceId(licence.getLicenceId());
		if (mongoLicence == null) {
			//logger.error(UNABLE_TO_FIND_THE_LICENCE_ID + licence.getLicenceId());
			throw new BridgeApiGeneralException(UNABLE_TO_FIND_THE_LICENCE_ID + licence.getLicenceId());
		}
		try {
			Map<String, Map<String, Object>> segregatedDataMap = segregatedData(licence);
			JsonObject documents = gson.toJsonTree(segregatedDataMap.get(SAM_MAP)).getAsJsonObject();
			documents.addProperty(ACCESS_CODE, licence.getLicenceId());
			documents.addProperty("updatedOn", mongoLicence.getDateCreated());
			documents.addProperty("updatedBy", licence.getCreatedBy());
			documents.remove("createdOn");
			documents.remove("createdBy");
			documents.remove("dateCreated");
			Map<String, String> mp = new HashMap<>();
			mp.put("licenceId", licence.getLicenceId());
			licenceRestIntegration.update(mp, documents);
			Utils.copyNonNullProperties(licence, mongoLicence);
			licenceRespository.save(mongoLicence);
			updateLicenceHistory(licence);
		} catch (BridgeApiGeneralException e) {
			//logger.error("Error while update the Licence:" + e.getMessage());
		}
		return mongoLicence;
	}

	@Override
	public Licence fetch(Licence licence) {
		// TODO Auto-generated method stub
		//logger.info("fetchOne Access Code {}", licence);
		Map<String, Map<String, Object>> segregatedDataMap = segregatedData(licence);
		Licence restObj = null;

		if (null != licence) {
			JsonObject documents = gson.toJsonTree(segregatedDataMap.get(SAM_MAP)).getAsJsonObject();
			try {
				documents.addProperty(ACCESS_CODE, licence.getLicenceId());
				restObj = licenceRestIntegration.fetchOne(documents);
				licence.setLicenceId(restObj.getLicenceId());
				licence.setDateCreated(restObj.getDateCreated());
				Map<String, Object> restMap = Utils.convert(licence, new TypeReference<Map<String, Object>>() {
				});
				Licence licence1 = licenceRespository.findByLicenceId(restObj.getLicenceId());
				Map<String, Object> mongoMap = Utils.convert(licence1, new TypeReference<Map<String, Object>>() {
				});
				restMap.putAll(mongoMap);
				restObj = Utils.convert(restMap, new TypeReference<Licence>() {
				});
			} catch (BridgeApiGeneralException e) {
				throw new BridgeApiGraphqlException(ErrorType.ValidationError, e.getMessage());
			}
		}
		return restObj;
	}

	@Override
	public Licence delete(Licence licence) {
		// TODO Auto-generated method stub
		Licence licenceDB = null;
		licenceDB = licenceRespository.findByLicenceId(licence.getLicenceId());
		if (licenceDB != null) {
			licenceRespository.delete(licenceDB);
		} else {
			throw new BridgeApiGraphqlException(ErrorType.DataFetchingException, DATA_NOT_FOUND);
		}
		return licenceDB;
	}

	@Override
	public List<Licence> fetchMultiple(KeyType key, List<String> ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Licence> pageIt(Pageable pageable, Example<Licence> e) {
		// TODO Auto-generated method stub
		return licenceRespository.findAll(e, pageable);
	}

	private void updateLicenceHistory(Licence licence) {
		LicenceHistory licenceHistory = null;
		licenceHistory = licenceHistoryRespository.findByLicence(licence.getLicenceId());
		if (licenceHistory != null) {
			licenceHistory.setAlteration(licence);
			licenceHistory.setLicence(licence.getLicenceId());
			licenceHistory.setAlterDate(Utils.generateEpochDate());
			licenceHistory.setAlteredBy(sessionFacade.getLoggedInUser(true).getUid());
			licenceHistoryRespository.save(licenceHistory);
		}
	}

	private Map<String, Map<String, Object>> segregatedData(Licence licence) {
		Map<String, Map<String, Object>> segregatedMap = new HashMap<>();

		Map<String, Object> dataMap = Utils.convertToMap(licence);

		List<String> mongoFields = Utils.extractAnnotatedFieldNames(Licence.class, MongoSource.class);
		List<String> samFields = Utils.extractAnnotatedFieldNames(Licence.class, SamSource.class);

		Map<String, Object> samMap = new HashMap<>(dataMap);
		samMap.keySet().retainAll(samFields);
		Map<String, Object> mongoMap = dataMap;
		mongoMap.keySet().retainAll(mongoFields);

		segregatedMap.put(SAM_MAP, samMap);
		segregatedMap.put(MONGO_MAP, mongoMap);

		return segregatedMap;
	}

}
