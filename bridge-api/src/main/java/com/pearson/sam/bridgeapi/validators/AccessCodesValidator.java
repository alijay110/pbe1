package com.pearson.sam.bridgeapi.validators;

import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.ACCESS_CODE_DOES_NOT_EXISTS;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.ACCESS_CODE_QUANTITY_MAX;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.ACCESS_CODE_QUANTITY_MIN;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.ACCESS_CODE_SHOULD_NOT_EMPTY;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.ACCESS_CODE_TYPES;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.ACCESS_CODE_TYPE_SHOULD_NOT_EMPTY;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.PRODUCTIDS_SHOULD_PRESENT;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.PRODUCTID_SHOULD_PRESENT;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.PRODUCT_DOES_NOT_EXISTS;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.START_DATE_SHOULD_NOT_GREATER_THAN_END_DATE;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pearson.sam.bridgeapi.enums.AccessCodeType;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGeneralException;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGraphqlException;
import com.pearson.sam.bridgeapi.model.AccessCodes;
import com.pearson.sam.bridgeapi.model.Product;
import com.pearson.sam.bridgeapi.repository.AccessCodeRepository;
import com.pearson.sam.bridgeapi.repository.ProductRepository;
import com.pearson.sam.bridgeapi.util.Utils;

import graphql.ErrorType;

@Component
public class AccessCodesValidator {
	public final static Logger logger = LoggerFactory.getLogger(AccessCodesValidator.class);

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private AccessCodeRepository accessCodeRepository;

	public void isValidAccessCodeType(String type) {
		if (Utils.isEmpty(type)) {
			throw new BridgeApiGraphqlException(ErrorType.ValidationError, ACCESS_CODE_TYPE_SHOULD_NOT_EMPTY);
		}
		boolean isValidAccessCodeType = false;
		for (AccessCodeType codeType : AccessCodeType.values()) {
			if (codeType.getType().equalsIgnoreCase(type)) {
				isValidAccessCodeType = true;
				break;
			}
		}
		if (!isValidAccessCodeType) {
			StringBuffer sb = new StringBuffer();
			for (AccessCodeType codeType : AccessCodeType.values()) {
				sb.append(codeType.getType());
				sb.append(",");
			}
			throw new BridgeApiGraphqlException(ErrorType.ValidationError,
					ACCESS_CODE_TYPES + sb.toString().substring(0, sb.toString().length() - 1));
		}
	}

	public void validateAccessCode(AccessCodes accessCodes) {
		validateQuantity(accessCodes);
		isValidAccessCodeType(accessCodes.getType());
	}

	public void validateQuantity(AccessCodes accessCodes) {
		if (accessCodes.getQuantity() == null || accessCodes.getQuantity() < 1) {
			throw new BridgeApiGraphqlException(ErrorType.ValidationError, ACCESS_CODE_QUANTITY_MIN);
		} else if (null != accessCodes && accessCodes.getQuantity() >= 24) {
			throw new BridgeApiGraphqlException(ErrorType.ValidationError, ACCESS_CODE_QUANTITY_MAX);
		}
	}

	public void isProductIdEmpty(String productId) {
		if (Utils.isEmpty(productId)) {
			//logger.error("Unabel to fetch Prouduct due to Product Id is empty.");
			throw new BridgeApiGeneralException(PRODUCTID_SHOULD_PRESENT);
		}
	}

	public void isListOfProductIdsEmpty(List<String> listOfProductIds) {
		if (listOfProductIds == null || listOfProductIds.isEmpty()) {
			//logger.error("Unabel to fetch Product due to Product Ids list is empty.");
			throw new BridgeApiGeneralException(PRODUCTIDS_SHOULD_PRESENT);
		}
	}

	public void validateProduct(String productId) {
		isProductIdEmpty(productId);
		Product product = productRepository.findByProductId(productId);
		if (product == null || !productId.equals(product.getProductId())) {
			//logger.error("Unabel to fetch Product due to Product Id:" + productId + " is not existed");
			throw new BridgeApiGeneralException(PRODUCT_DOES_NOT_EXISTS);
		}
	}

	public void validateAccessCode(String accessCode) {
		AccessCodes accessCodeDB = accessCodeRepository.findByAccessCode(accessCode);
		if (accessCodeDB == null || !accessCode.equals(accessCodeDB.getAccessCode())) {
			//logger.error("Unabel to fetch Access Code due to Access Code:" + accessCode + " is not existed");
			throw new BridgeApiGeneralException(ACCESS_CODE_DOES_NOT_EXISTS);
		}
	}

	public void isAccessCodeEmpty(String accessCode) {
		if (Utils.isEmpty(accessCode)) {
			//logger.error("Unabel to fetch Access Code due to Access code is empty.");
			throw new BridgeApiGeneralException(ACCESS_CODE_SHOULD_NOT_EMPTY);
		}
	}

	public void validateStarAndEndDate(String startDate, String endDate) {
		try {
			long startDateLong = Long.parseLong(startDate);
			long endDateLong = Long.parseLong(endDate);
			if (startDateLong > endDateLong) {
				//logger.error("start date is should not greater than end date");
				throw new BridgeApiGeneralException(START_DATE_SHOULD_NOT_GREATER_THAN_END_DATE);
			}
		} catch (NumberFormatException nfe) {
			//logger.error("Unable to parse the start date string to number :" + startDate);
			//logger.error("Unable to parse the end date string to number :" + endDate);
		}
	}
}
