package com.pearson.sam.bridgeapi.serviceimpl;

import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.HYPHEN;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.MONGO_MAP;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.SAM_MAP;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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
import com.pearson.sam.bridgeapi.iservice.IAccessCodeRepoService;
import com.pearson.sam.bridgeapi.model.AccessCodes;
import com.pearson.sam.bridgeapi.model.KeyType;
import com.pearson.sam.bridgeapi.model.SessionUser;
import com.pearson.sam.bridgeapi.repository.AccessCodeRepository;
import com.pearson.sam.bridgeapi.samclient.AccessCodeSamClient;
import com.pearson.sam.bridgeapi.security.ISessionFacade;
import com.pearson.sam.bridgeapi.util.Utils;

import graphql.ErrorType;

@Service
public class AccessCodeRepoService implements IAccessCodeRepoService {
	private static final Logger logger = LoggerFactory.getLogger(AccessCodeRepoService.class);

	@Autowired
	private AccessCodeSamClient accessCodeSamClient;

	@Autowired
	private AccessCodeRepository accessCodeRepository;

	@Autowired
	protected ISessionFacade sessionFacade;
	
	private final GsonBuilder gsonBuilder = new GsonBuilder();
	private final Gson gson = gsonBuilder.setPrettyPrinting().disableHtmlEscaping().create();

	@Override
	public AccessCodes create(AccessCodes accesscode) {

		logger.info("Saving Access Code {}", accesscode);
		Map<String, Map<String, Object>> segregatedDataMap = segregatedData(accesscode);
		AccessCodes mongoAccessCode = Utils.convert(segregatedDataMap.get("mongoMap"),
				new TypeReference<AccessCodes>() {
				});

		if (null != accesscode) {
			JsonObject documents = gson.toJsonTree(segregatedDataMap.get(SAM_MAP)).getAsJsonObject();
			try {
				AccessCodes restObj = accessCodeSamClient.create(documents);
				mongoAccessCode.setAccessCode(restObj.getAccessCode());
				mongoAccessCode.setCode(restObj.getCode());
				mongoAccessCode.setCreatedBy(accesscode.getCreatedBy());
				mongoAccessCode.setDateCreated(restObj.getDateCreated());
				mongoAccessCode = accessCodeRepository.save(mongoAccessCode);
			} catch (BridgeApiGeneralException e) {
				throw new BridgeApiGraphqlException(ErrorType.ValidationError, e.getMessage());
			}
		}

		return mongoAccessCode;
	}

	@Override
	public AccessCodes update(AccessCodes accesscode) {
		logger.info("update Access Code {}", accesscode);
		AccessCodes dbAccessCode = accessCodeRepository.findByCode(accesscode.getCode());
		logger.info("dbAccessCode Before {}", dbAccessCode);
		if (dbAccessCode == null) {
			logger.error("AccessCode :" + accesscode.getCode() + " is not found.");
			throw new BridgeApiGeneralException("AccessCode :" + accesscode.getCode() + " is not found.");
		}
		if (dbAccessCode.getTotalReactivations() != null && 0 != dbAccessCode.getTotalReactivations()) {
			logger.error("Unable to update AccessCode :" + dbAccessCode.getAccessCode()
					+ " because it is already activated");
			throw new BridgeApiGeneralException("Unable to update AccessCode :" + dbAccessCode.getAccessCode()
					+ " because it is already activated");
		}
		try {
			Map<String, Map<String, Object>> segregatedDataMap = segregatedData(accesscode);
			Map<String, Object> samMap = segregatedDataMap.get(SAM_MAP);
			String samAccessCode = padUpSAMAccessCode(accesscode.getAccessCode());
			samMap.put("accessCode", samAccessCode);
			JsonObject documents = gson.toJsonTree(samMap).getAsJsonObject();
			if (accesscode.getLastActivatedBy() != null) {
				documents.addProperty("updatedBy", getUser(true).getUid());
			} else {
				documents.addProperty("updatedBy", getUser(true).getUid());
				accesscode.setLastActivatedBy(getUser(true).getUid());
			}

			if (accesscode.getLastActivatedDate() != null) {
				documents.addProperty("updatedOn", accesscode.getLastActivatedDate());
			} else {
				documents.addProperty("updatedOn", Utils.generateEpochDate());
				accesscode.setLastActivatedDate(Utils.generateEpochDate());
			}

			Map<String, String> mp = new HashMap<>();
			mp.put("accessCode", samAccessCode);

			AccessCodes restObj = accessCodeSamClient.update(mp, documents);
			dbAccessCode.setCode(accesscode.getCode());
			Utils.copyNonNullProperties(accesscode, dbAccessCode);

			dbAccessCode = accessCodeRepository.save(dbAccessCode);
		} catch (BridgeApiGeneralException e) {
			logger.error(e.getMessage());
		}
		return dbAccessCode;
	}

	@Override
	public AccessCodes fetchOne(AccessCodes accesscode) {
		logger.info("fetchOne Access Code {}", accesscode);
		AccessCodes mongoAccessCode = null;
		if (accesscode.getCode() != null) {
			try {
				mongoAccessCode = accessCodeRepository.findByCode(accesscode.getCode());
				if (mongoAccessCode != null) {
					Map<String, String> mp = new HashMap<>();
					String samAccessCode = padUpSAMAccessCode(mongoAccessCode.getAccessCode());
					mp.put("accessCodeId", samAccessCode);
					JsonObject documents = gson.toJsonTree(mp).getAsJsonObject();
					accesscode = accessCodeSamClient.fetchOne(documents);
					mongoAccessCode.setProducts(accesscode.getProducts());
					mongoAccessCode.setCreatedBy(accesscode.getCreatedBy());
					mongoAccessCode.setDateCreated(accesscode.getDateCreated());
				}
			} catch (BridgeApiGeneralException e) {
				throw new BridgeApiGraphqlException(ErrorType.ValidationError, e.getMessage());
			}
		}
		return mongoAccessCode;
	}

	@Override
	public List<AccessCodes> fetchMultiple(KeyType key, List<String> ids) {
		List<AccessCodes> accessCodeList = null;
		switch (key) {
		case ACCESS_CODES:
			accessCodeList = accessCodeRepository.findAll();
			break;
		case USER_ID:
			break;
		default:
			break;
		}
		return accessCodeList;
	}

	public AccessCodes delete(AccessCodes object) {
		return null;
	}

	@Override
	public Page<AccessCodes> pageIt(Pageable pageable, Example<AccessCodes> e) {
		return accessCodeRepository.findAll(e, pageable);
	}

	private Map<String, Map<String, Object>> segregatedData(AccessCodes accessCodes) {
		Map<String, Map<String, Object>> segregatedMap = new HashMap<>();

		Map<String, Object> dataMap = Utils.convertToMap(accessCodes);

		List<String> mongoFields = Utils.extractAnnotatedFieldNames(AccessCodes.class, MongoSource.class);
		List<String> samFields = Utils.extractAnnotatedFieldNames(AccessCodes.class, SamSource.class);

		Map<String, Object> samMap = new HashMap<>(dataMap);
		samMap.keySet().retainAll(samFields);
		Map<String, Object> mongoMap = dataMap;
		mongoMap.keySet().retainAll(mongoFields);

		segregatedMap.put(SAM_MAP, samMap);
		segregatedMap.put(MONGO_MAP, mongoMap);

		return segregatedMap;
	}

	@Override
	public List<AccessCodes> generateAccessCodes(AccessCodes accessCodedata) {
		logger.info("Geneating Multiple Access Codes {}", accessCodedata);
		
		accessCodedata.setDateCreated(null != accessCodedata.getDateCreated() ? accessCodedata.getDateCreated() : Utils.generateEpochDate());
		accessCodedata.setCreatedBy( getUser(true).getUid());
		
		Map<String, Map<String, Object>> segregatedDataMap = segregatedData(accessCodedata);
		List<AccessCodes> restObj = new ArrayList<>();

		if (null != accessCodedata) {
			JsonObject documents = gson.toJsonTree(segregatedDataMap.get(SAM_MAP)).getAsJsonObject();
			try {
				// documents.addProperty("batch", accessCodedata.getBatch());
				AccessCodes findTopByOrderByIdDesc = accessCodeRepository.findTopByOrderByIdDesc();
				if (Optional.ofNullable(findTopByOrderByIdDesc).isPresent()) {
					// batch = 1 + findTopByOrderByIdDesc.getBatch();
				}
				documents.remove("subscriptionDate");
				documents.remove("userName");
				restObj = accessCodeSamClient.createMultiple(documents);
				for (AccessCodes item : restObj) {
					accessCodedata.setProducts(accessCodedata.getProducts());
					String mongoAccessCode = padDownSAMAccessCode(item.getAccessCode());
					accessCodedata.setCode(mongoAccessCode);
					accessCodedata.setDateCreated(accessCodedata.getDateCreated());
					accessCodedata.setAccessCode(mongoAccessCode);
					accessCodedata.setBatch(item.getBatch());
					BeanUtils.copyProperties(accessCodedata, item);
				}
				restObj = accessCodeRepository.insert(restObj);
			} catch (BridgeApiGeneralException e) {
				throw new BridgeApiGraphqlException(ErrorType.ValidationError, e.getMessage());
			}
		}
		return restObj;
	}

	@Override
	public List<AccessCodes> getAccessCodesFromBatch(String batch) {
		logger.info("Getting Access Codes From Batch {}");
		List<AccessCodes> findByBatch = null;
		try {
			findByBatch = accessCodeRepository.findByBatch(batch);
		} catch (BridgeApiGeneralException e) {
			throw new BridgeApiGraphqlException(ErrorType.ValidationError, e.getMessage());
		}
		return findByBatch;
	}

	@Override
	public Map<String, Object> addAccessCodes(String bodyString) {
		return accessCodeSamClient.addAccessCodes(bodyString);
	}
	
	private SessionUser getUser(boolean getLoggedInUser) {
		return sessionFacade.getLoggedInUser(getLoggedInUser);
	}
	
	private String padUpSAMAccessCode(String accessCode) {
		 accessCode = accessCode.replace(HYPHEN, "");
		 accessCode = "0000"+accessCode;
		 return accessCode;
	 }

	private String padDownSAMAccessCode(String accessCode) {
		StringBuffer sb = new StringBuffer();
		sb.append(accessCode.substring(4, 8));
		sb.append(HYPHEN);
		sb.append(accessCode.substring(8, 12));
		sb.append(HYPHEN);
		sb.append(accessCode.substring(12));
		return sb.toString();
	}
	
  /* (non-Javadoc)
   * @see com.pearson.sam.bridgeapi.iservice.IAccessCodeRepoService#getTotalCount()
   */
  @Override
  public Long getTotalCount() {
    return accessCodeRepository.count();
  }

}
