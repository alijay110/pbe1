package com.pearson.sam.bridgeapi.serviceimpl;

import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.MONGO_MAP;
import static com.pearson.sam.bridgeapi.constants.ValidatorConstants.INVALID_CODE;
import static com.pearson.sam.bridgeapi.constants.ValidatorConstants.INVALID_VOUCHER_CODE;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.HYPHEN;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.pearson.sam.bridgeapi.annotations.MongoSource;
import com.pearson.sam.bridgeapi.annotations.SamSource;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGeneralException;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGraphqlException;
import com.pearson.sam.bridgeapi.iservice.ISubscriptionService;
import com.pearson.sam.bridgeapi.model.AccessCodes;
import com.pearson.sam.bridgeapi.model.MethodType;
import com.pearson.sam.bridgeapi.model.Voucher;
import com.pearson.sam.bridgeapi.repository.AccessCodeRepository;
import com.pearson.sam.bridgeapi.repository.VoucherRepository;
import com.pearson.sam.bridgeapi.samclient.SubscriptionSamClient;
import com.pearson.sam.bridgeapi.util.Utils;
import com.pearson.sam.bridgeapi.validators.ModelValidator;
import com.pearson.sam.bridgeapi.validators.SubscriptionValidator;

import graphql.ErrorType;

@Component
public class SubscriptionService implements ISubscriptionService {
	private static final Logger logger = LoggerFactory.getLogger(SubscriptionService.class);
	private static final String SAM_MAP = "samMap";

	@Autowired
	private SubscriptionSamClient subscriptionSamClient;

	@Autowired
	private AccessCodeRepository accessCodeRepository;

	@Autowired
	private VoucherRepository voucherRepository;

	@Autowired
	private SubscriptionValidator validator;
	
	@Autowired
	ModelValidator modelValidator;
	
	private final GsonBuilder gsonBuilder = new GsonBuilder();
	private final Gson gson = gsonBuilder.setPrettyPrinting().disableHtmlEscaping().create();

	@Override
	public AccessCodes createAccessCodeSubscription(AccessCodes accesscode) {

		//logger.info("Saving Access Code {}", accesscode);
		modelValidator.validateModel(accesscode, MethodType.CREATE.toString());
		Map<String, Map<String, Object>> segregatedDataMap = segregatedData(accesscode);
		AccessCodes mongoAccessCode = null;
		AccessCodes mongoDb = null;
		mongoDb = accessCodeRepository.findByAccessCode(accesscode.getAccessCode());

		if (null != accesscode && null != mongoDb) {
			Map<String, Object> mongoMap = segregatedDataMap.get("mongoMap");
			mongoAccessCode = Utils.convert(mongoMap, new TypeReference<AccessCodes>() {
			});
			Utils.copyNonNullProperties(mongoDb, mongoAccessCode);
			Utils.copyNonNullProperties(accesscode, mongoAccessCode);
			mongoAccessCode.setTotalReactivations(
					mongoDb.getTotalReactivations() == null ? 0 : mongoDb.getTotalReactivations());
			String samAccessCode = padUpSAMAccessCode(accesscode.getAccessCode());
			if (validator.validateAccessCode(mongoAccessCode)) {
				Map<String, Object> samMap = segregatedDataMap.get(SAM_MAP);
				samMap.put("accessCode", samAccessCode);
				JsonObject documents = gson.toJsonTree(samMap).getAsJsonObject();
				try {
					AccessCodes restObj = subscriptionSamClient.create(documents);
					String accCode = padDownSAMAccessCode(restObj.getAccessCode());
					mongoAccessCode.setAccessCode(accCode);
					mongoAccessCode.setUserName(accesscode.getLastActivatedBy());
					mongoAccessCode.setLastActivatedBy(accesscode.getLastActivatedBy());
					mongoAccessCode.setTotalReactivations(
							mongoDb.getTotalReactivations() == null ? 1 : mongoDb.getTotalReactivations() + 1);
					mongoAccessCode.setId(mongoDb.getId());
					mongoAccessCode.setUserName(accesscode.getUserName());
					mongoAccessCode = accessCodeRepository.save(mongoAccessCode);
				} catch (BridgeApiGeneralException e) {
					throw new BridgeApiGraphqlException(ErrorType.ValidationError, e.getMessage());
				}
			}
		} else {
			throw new BridgeApiGraphqlException(ErrorType.ValidationError, INVALID_CODE);
		}

		return mongoAccessCode;
	}

	/*
	 * This Method gets the Voucher and Subscribes as the AccessCode Model
	 */
	@Override
	public Voucher createVoucherSubscription(Voucher graphVoucher) {
		//logger.info("Saving Access Code {}", graphVoucher);
		AccessCodes mongoAccessCode = null;
		AccessCodes mongoDb = null;

		Voucher voucher = voucherRepository.findByVoucherCode(graphVoucher.getVoucherCode());
		if (null != voucher) {
			Utils.copyNonNullProperties(voucher, graphVoucher);
			Map<String, Object> voucherUiMap = Utils.convert(graphVoucher, new TypeReference<Map<String, Object>>() {
			});
			Map<String, Object> accessCodeMap = Utils.convert(new AccessCodes(),
					new TypeReference<Map<String, Object>>() {
					});
			graphVoucher.setTotalReactivations(
					voucher.getTotalReactivations() == null ? 0 : voucher.getTotalReactivations());
			accessCodeMap.putAll(voucherUiMap);
			accessCodeMap.remove("voucherId");
			accessCodeMap.remove("voucherCode");
			accessCodeMap.put("code", voucher.getVoucherId());
			mongoDb = Utils.convert(accessCodeMap, new TypeReference<AccessCodes>() {
			});
			List<String> products = new ArrayList<>();
			/*
			 * JsonObject accessObject =
			 * gson.toJsonTree(graphVoucher).getAsJsonObject(); AccessCodes
			 * mongoProductDb = subscriptionIntegration.fetchOne(accessObject);
			 */
			AccessCodes mongoProductDb = accessCodeRepository.findByAccessCode(graphVoucher.getAccessCode());
			products.add("PP2PRODUCT0");
			mongoDb.setProducts(mongoProductDb == null ? products : mongoProductDb.getProducts());
		} else {
			throw new BridgeApiGraphqlException(ErrorType.ValidationError, INVALID_VOUCHER_CODE);
		}

		if (null != graphVoucher && null != mongoDb && validator.validateVoucherCode(graphVoucher)) {
			Map<String, Map<String, Object>> segregatedDataMap = segregatedData(mongoDb);
			JsonObject documents = gson.toJsonTree(segregatedDataMap.get(SAM_MAP)).getAsJsonObject();
			try {
				documents.addProperty("requestParam", graphVoucher.getVoucherCode());
				AccessCodes restObj = subscriptionSamClient.create(documents);
				Map<String, Object> mongoMap = segregatedDataMap.get("mongoMap");
				mongoAccessCode = Utils.convert(mongoMap, new TypeReference<AccessCodes>() {
				});
				Utils.copyNonNullProperties(mongoDb, mongoAccessCode);
				mongoAccessCode.setAccessCode(restObj.getAccessCode());
				mongoAccessCode.setUserName(graphVoucher.getLastActivatedBy());
				mongoAccessCode.setLastActivatedBy(graphVoucher.getLastActivatedBy());
				mongoAccessCode.setLastActivatedDate(graphVoucher.getLastActivatedDate());
				mongoAccessCode.setTotalReactivations(
						voucher.getTotalReactivations() == null ? 1 : (int) (voucher.getTotalReactivations() + 1));
				mongoAccessCode.setId(voucher.getId());
			} catch (BridgeApiGeneralException e) {
				throw new BridgeApiGraphqlException(ErrorType.ValidationError, e.getMessage());
			}
		}

		return getVoucherDetails(graphVoucher.getVoucherCode(), mongoAccessCode, graphVoucher.getAccessCode());
	}


	private Voucher getVoucherDetails(String voucherCode, AccessCodes codes, String accesscode) {
		Voucher voucher = new Voucher();
		Map<String, Object> accessCodeMap = Utils.convert(codes, new TypeReference<Map<String, Object>>() {
		});
		Map<String, Object> voucherDbMap = Utils.convert(voucher, new TypeReference<Map<String, Object>>() {
		});
		voucherDbMap.putAll(accessCodeMap);
		Object voucherId = voucherDbMap.remove("code");
		voucherDbMap.put("voucherId", voucherId);
		voucherDbMap.remove("userName");
		voucherDbMap.remove("products");
		voucherDbMap.put("accessCode", accesscode);
		voucherDbMap.put("voucherCode", voucherCode);
		voucherDbMap.remove("lastActivatedDate");
		Voucher voucherDb = Utils.convert(voucherDbMap, new TypeReference<Voucher>() {
		});
		voucherDb = voucherRepository.save(voucherDb);
		return voucherDb;

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
	public AccessCodes getAccessCodeSubscription(AccessCodes accesscodes) {
		// TODO Auto-generated method stub
		String mongoAccessCode = accesscodes.getAccessCode();
		String samAccessCode = padUpSAMAccessCode(mongoAccessCode);
		//logger.info("fetchOne Access Code {}", accesscodes);
		try {
			Boolean isAccessCodeExisted = accessCodeRepository.existsByCode(accesscodes.getAccessCode());
			if (isAccessCodeExisted) {
				accesscodes.setAccessCode(samAccessCode);
				accesscodes = subscriptionSamClient.getAccessCodeSubscription(accesscodes);
				accesscodes.setAccessCode(mongoAccessCode);
			}
		} catch (BridgeApiGeneralException e) {
			throw new BridgeApiGraphqlException(ErrorType.ValidationError, e.getMessage());
		}
		return accesscodes;
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
}
