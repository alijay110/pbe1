package com.pearson.sam.bridgeapi.validators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pearson.sam.bridgeapi.exceptions.BridgeApiGeneralException;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGraphqlException;
import com.pearson.sam.bridgeapi.model.Voucher;
import com.pearson.sam.bridgeapi.model.VoucherCodeType;
import com.pearson.sam.bridgeapi.repository.VoucherRepository;
import com.pearson.sam.bridgeapi.util.Utils;

import graphql.ErrorType;

@Component
public class VoucherCodesValidator {
	
	public final static Logger logger = LoggerFactory.getLogger(AccessCodesValidator.class);
	
	@Autowired
	private VoucherRepository voucherRepository;

	public void isValidVoucherCodeType(String type) {
		if (Utils.isEmpty(type)) {
			throw new BridgeApiGraphqlException(ErrorType.ValidationError, "Type is should not be empty");
		}
		try {
			VoucherCodeType.valueOf(type);
		}catch(IllegalArgumentException iag) {
			//logger.error("Voucher code type "+ type + " is not existed.");
			StringBuffer sb = new StringBuffer();
			for (VoucherCodeType codeType : VoucherCodeType.values()) {
				sb.append(codeType.getAction());
				sb.append(",");
			}
			throw new BridgeApiGraphqlException(ErrorType.ValidationError,
					"Voucher Code types are allowed:" + sb.toString().substring(0, sb.toString().length() - 1));
		}
	}

	public void validateVoucherCodeQuantity(Voucher voucher) {
		if (voucher.getQuantity() == null)
			throw new BridgeApiGraphqlException(ErrorType.ValidationError, "Quantity is should not be empty");
		if (voucher.getQuantity() < 1) {
			throw new BridgeApiGraphqlException(ErrorType.ValidationError, "Quantity should not be less than 1");
		} else if (null != voucher && voucher.getQuantity() >= 24) {
			throw new BridgeApiGraphqlException(ErrorType.ValidationError, "Quantity should not be more than 24");
		}
		isValidVoucherCodeType(voucher.getType());
	}
	
	public void validateVoucherCode(String voucherCode) {
		Voucher voucher = voucherRepository.findByVoucherCode(voucherCode);
		if(voucher==null || !voucherCode.equals(voucher.getVoucherCode())) {
			 //logger.error("Unabel to fetch voucher Code due to voucher Code:"+voucherCode +" is not existed");
		      throw new BridgeApiGeneralException("Unabel to fetch voucher Code due to voucher Code:"+voucherCode +" is not existed");
		}
	}

}
