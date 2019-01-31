package com.pearson.sam.bridgeapi.serviceimpl;

import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.MONGO_MAP;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.SAM_MAP;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.VOUCHER_CODE_DOES_NOT_EXISTS;

import com.bazaarvoice.jolt.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.pearson.sam.bridgeapi.annotations.MongoSource;
import com.pearson.sam.bridgeapi.annotations.SamSource;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGraphqlException;
import com.pearson.sam.bridgeapi.iservice.IAccessCodeRepoService;
import com.pearson.sam.bridgeapi.iservice.IVoucherService;
import com.pearson.sam.bridgeapi.model.MethodType;
import com.pearson.sam.bridgeapi.model.SessionUser;
import com.pearson.sam.bridgeapi.model.Voucher;
import com.pearson.sam.bridgeapi.repository.VoucherRepository;
import com.pearson.sam.bridgeapi.security.ISessionFacade;
import com.pearson.sam.bridgeapi.util.Utils;
import com.pearson.sam.bridgeapi.validators.ModelValidator;

import graphql.ErrorType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.executable.ValidateOnExecution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@ValidateOnExecution
public class VoucherServiceImpl implements IVoucherService {
	private static final Logger logger = LoggerFactory.getLogger(VoucherServiceImpl.class);

	@Autowired
	private IndexService idxService;

	@Autowired
	private IAccessCodeRepoService accessCodeServices;

	@Autowired
	private VoucherRepository voucherRepository;

	@Autowired
	protected ISessionFacade sessionFacade;

	@Autowired
	ModelValidator validator;

	private static final String VOUCHER_CODE = "voucherCode";
	private static final String VOUCHER_ID = "voucherId";
	private static final String PP2VOUCHER = "PP2VOUCHER";
	private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHJKMNPQRSTUVWXYZ";
	private static final String ALPHA_NUMERIC_NUMBERS = "23456789";

	@Override
	public Voucher create(Voucher voucher) {

		//logger.info("Saving Access Code {}", voucher);
		validator.validateModel(voucher, MethodType.CREATE.toString());
		Map<String, Map<String, Object>> segregatedDataMap = segregatedData(voucher);
		Utils.convert(segregatedDataMap.get("mongoMap"), new TypeReference<Voucher>() {
		});

		if (null != voucher) {
			voucher.setVoucherId(PP2VOUCHER + idxService.getNextId(VOUCHER_ID));
			voucher.setCreatedBy(getUser(true).getUid());
			voucher.setDateCreated(
					null != voucher.getDateCreated() ? voucher.getDateCreated() : Utils.generateEpochDate());
			StringBuilder voucherCode = new StringBuilder();
			voucherCode.append("REA" + getVoucherTypeId(voucher.getType())).append("-").append(randomAlphaNumeric(4))
					.append("-").append(randomAlphaNumeric(4));
			voucher.setVoucherCode(voucherCode.toString());
			voucher = voucherRepository.save(voucher);
		}

		return voucher;
	}

	/**
	 * createMultiple.
	 * 
	 * @return
	 */
	@Override
	public List<Voucher> createMultiple(Voucher voucher) {
		//logger.info("Saving Multiple Voucher Code {}", voucher);
		Map<String, Map<String, Object>> segregatedDataMap = segregatedData(voucher);
		Utils.convert(segregatedDataMap.get("mongoMap"), new TypeReference<Voucher>() {
		});
		List<Voucher> result = new ArrayList<>();

		if (null != voucher) {

			for (int i = 0; i < voucher.getQuantity(); i++) {
				Voucher request = new Voucher();
				request.setVoucherId(PP2VOUCHER + idxService.getNextId(VOUCHER_ID));
				request.setCreatedBy(getUser(true).getUserId());
				StringBuilder voucherCode = new StringBuilder().append("REA" + getVoucherTypeId(voucher.getType()))
						.append("-").append(randomAlphaNumeric(4)).append("-").append(randomAlphaNumeric(4));

				request.setVoucherCode(voucherCode.toString());
				request.setDateCreated(
						null != voucher.getDateCreated() ? voucher.getDateCreated() : Utils.generateEpochDate());
				request.setQuantity(voucher.getQuantity());
				request.setType(voucher.getType());
				result.add(request);
			}
			storeInSamAccess(result);
			result = voucherRepository.insert(result);
		}
		for(Voucher respVoucher:result) {
			respVoucher.setTotalReactivations(voucher.getTotalReactivations());
		}
		return result;
	}

	@Override
	public Voucher update(Voucher voucher) {
		//logger.info("update Access Code {}", voucher);
		validator.validateModel(voucher, MethodType.CREATE.toString());
		Voucher dbVoucher = voucherRepository.findByVoucherCode(voucher.getVoucherCode());
		if (dbVoucher == null)
			throw new BridgeApiGraphqlException(ErrorType.DataFetchingException, VOUCHER_CODE_DOES_NOT_EXISTS);
		voucher.setUpdatedBy(getUser(true).getUserId());
		voucher.setUpdatedOn(null != voucher.getUpdatedOn() ? voucher.getUpdatedOn() : Utils.generateEpochDate());

		Utils.copyNonNullProperties(voucher, dbVoucher);
		dbVoucher = voucherRepository.save(dbVoucher);
		return dbVoucher;
	}

	@Override
	public Voucher fetchOne(Voucher voucher) {
		//logger.info("fetchOne Access Code {}", voucher);
		Voucher mongoVoucher = null;
		if (voucher.getVoucherCode() != null) {
			mongoVoucher = voucherRepository.findByVoucherCode(voucher.getVoucherCode());
		}
		return mongoVoucher;
	}

	@Override
	public List<Voucher> getVoucherFromBatch(String batch) {
		//logger.info("Getting Access Codes From Batch {}");
		List<Voucher> findByBatch = null;
		findByBatch = voucherRepository.findByBatch(batch);
		return findByBatch;
	}

	@Override
	public Page<Voucher> pageIt(Pageable pageable, Example<Voucher> e) {
		return voucherRepository.findAll(e, pageable);
	}

	public void storeInSamAccess(List<Voucher> voucherList) {

		Map<String, Object> voucherMap = null;

		List<Map<String, Object>> voucherCodeMapList = new ArrayList<>();
		for (Voucher voucher : voucherList) {
			voucherMap = Utils.convert(voucher, new TypeReference<Map<String, Object>>() {
			});

			voucherMap.remove("type");
			voucherMap.remove("voucherId");
			voucherMap.remove(VOUCHER_CODE);
			voucherMap.remove("accessCode");
			voucherMap.remove("batch");
			voucherMap.remove("void");
			voucherMap.remove("quantity");
			voucherMap.remove("dateCreated");
			voucherMap.put("createdOn", voucher.getDateCreated());
			voucherMap.put("accessCode", voucher.getVoucherCode());

			voucherCodeMapList.add(voucherMap);
		}

		Map<String, Object> bodyMap = new HashMap<>();

		bodyMap.put("accessCodesList", voucherCodeMapList);

		String bodyString = "";
		bodyString = JsonUtils.toJsonString(bodyMap);
		Map<String, Object> result = accessCodeServices.addAccessCodes(bodyString);
		/*
		 * List<Map<String, Object>> resultMap =
		 * Utils.convert(result.get("accessCodeList"), new
		 * TypeReference<List<Map<String, Object>>>() { });
		 */
		List<Map<String, Object>> resultMap = (List<Map<String, Object>>) result.get("accessCodeList");
		Map<String, Object> map = resultMap.get(0);
		System.out.println(map.toString() + ":" + map.get("batchIdentifier"));
		String batch = (String) resultMap.get(0).get("batchIdentifier");

		for (Voucher voucher : voucherList) {
			voucher.setBatch(batch);
		}
	}

	private Map<String, Map<String, Object>> segregatedData(Voucher voucherCode) {
		Map<String, Map<String, Object>> segregatedMap = new HashMap<>();

		Map<String, Object> dataMap = Utils.convertToMap(voucherCode);

		List<String> mongoFields = Utils.extractAnnotatedFieldNames(Voucher.class, MongoSource.class);
		List<String> samFields = Utils.extractAnnotatedFieldNames(Voucher.class, SamSource.class);

		Map<String, Object> samMap = new HashMap<>(dataMap);
		samMap.keySet().retainAll(samFields);
		Map<String, Object> mongoMap = dataMap;
		mongoMap.keySet().retainAll(mongoFields);

		segregatedMap.put(SAM_MAP, samMap);
		segregatedMap.put(MONGO_MAP, mongoMap);

		return segregatedMap;
	}

	private int getVoucherTypeId(String type) {
		if ("7-10".equals(type)) {
			return 1;
		} else if ("11-12".equals(type)) {
			return 2;
		} else if ("VCE English".equals(type)) {
			return 3;
		} else if ("Exam Guide".equals(type)) {
			return 4;
		}
		return 0;
	}

	/**
	 * randomAlphaNumeric.
	 * 
	 * @return
	 */
	public static String randomAlphaNumeric(int count) {
		StringBuilder builder = new StringBuilder();
		while (count-- != 0) {
			if (count % 2 != 0) {
				int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
				builder.append(ALPHA_NUMERIC_STRING.charAt(character));
			} else {
				int number = (int) (Math.random() * ALPHA_NUMERIC_NUMBERS.length());
				builder.append(ALPHA_NUMERIC_NUMBERS.charAt(number));
			}
		}
		return builder.toString();
	}

	private SessionUser getUser(boolean getLoggedInUser) {
		return sessionFacade.getLoggedInUser(getLoggedInUser);
	}

  /* (non-Javadoc)
   * @see com.pearson.sam.bridgeapi.iservice.IVoucherService#getTotalCount()
   */
  @Override
  public Long getTotalCount() {
    return voucherRepository.count();
  }

}
