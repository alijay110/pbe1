/**
 * 
 */
package com.pearson.sam.bridgeapi.validators;

import static com.pearson.sam.bridgeapi.constants.ValidatorConstants.ACCESSCODE_FIRST;
import static com.pearson.sam.bridgeapi.constants.ValidatorConstants.ALREADY_ACTIVATED;
import static com.pearson.sam.bridgeapi.constants.ValidatorConstants.CODE_EXPIRE;
import static com.pearson.sam.bridgeapi.constants.ValidatorConstants.CODE_IN_BOOK;
import static com.pearson.sam.bridgeapi.constants.ValidatorConstants.CODE_SINGLE_USE;
import static com.pearson.sam.bridgeapi.constants.ValidatorConstants.CODE_STUDENT;
import static com.pearson.sam.bridgeapi.constants.ValidatorConstants.CODE_TEACHER;
import static com.pearson.sam.bridgeapi.constants.ValidatorConstants.IN_USE;
import static com.pearson.sam.bridgeapi.constants.ValidatorConstants.NOT_AUTHORIZED;
import static com.pearson.sam.bridgeapi.constants.ValidatorConstants.NO_PERMISSIONS;
import static com.pearson.sam.bridgeapi.constants.ValidatorConstants.TIME_ZONE;
import static com.pearson.sam.bridgeapi.constants.ValidatorConstants.WRONG_VOUCHER_CODE;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.ACCESS_CODE_SHOULD_NOT_EMPTY;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.PRODUCT_DOES_NOT_EXISTS;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.PRODUCTID_SHOULD_PRESENT;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.PRODUCTIDS_SHOULD_PRESENT;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.ACCESS_CODE_DOES_NOT_EXISTS;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.ACCESS_CODE_TYPES;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.ACCESS_CODE_TYPE_SHOULD_NOT_EMPTY;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pearson.sam.bridgeapi.enums.AccessCodeType;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGeneralException;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGraphqlException;
import com.pearson.sam.bridgeapi.model.AccessCodes;
import com.pearson.sam.bridgeapi.model.Product;
import com.pearson.sam.bridgeapi.model.SessionUser;
import com.pearson.sam.bridgeapi.model.User;
import com.pearson.sam.bridgeapi.model.Voucher;
import com.pearson.sam.bridgeapi.repository.AccessCodeRepository;
import com.pearson.sam.bridgeapi.repository.ProductRepository;
import com.pearson.sam.bridgeapi.security.ISessionFacade;
import com.pearson.sam.bridgeapi.util.Utils;

import graphql.ErrorType;

/**
 * @author VGUDLSA
 *
 */

@Component
public class SubscriptionValidator {
	private static final Logger logger = LoggerFactory.getLogger(SubscriptionValidator.class);
	@Autowired
	private ISessionFacade sessionFacade;
	
	@Autowired
	private AccessCodeRepository accessCodeRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	private SessionUser getUser(boolean getLoggedInUser) {
	    return sessionFacade.getLoggedInUser(getLoggedInUser);
	  }
	
	public Boolean validateAccessCode(AccessCodes accessCodes){
		User user = getUser(false);
		Set<String> roles =user.getRoles();
		//logger.info("logger user:"+user.getUid());
		//logger.info("logger user roles:"+roles);
		if(accessCodes != null && accessCodes.getAccessCode().contains("REA")){
			throw new BridgeApiGraphqlException(ErrorType.ValidationError, ACCESSCODE_FIRST);
		}
		
		if((roles.contains(CODE_STUDENT) || roles.contains(CODE_TEACHER)) ) {
			
			if(roles.contains(CODE_TEACHER) && CODE_TEACHER.equalsIgnoreCase(accessCodes.getType())) {
					if(accessCodes.getTotalReactivations()==1 && isWithInSixMonths(accessCodes.getLastActivatedDate())){
						if(getUser(true).getUid().equalsIgnoreCase(accessCodes.getLastActivatedBy())){
							throw new BridgeApiGraphqlException(ErrorType.ValidationError, ALREADY_ACTIVATED);
						}else{
							throw new BridgeApiGraphqlException(ErrorType.ValidationError, IN_USE);
						}
					}
			}
				
	       if(roles.contains(CODE_STUDENT) && CODE_TEACHER.equalsIgnoreCase(accessCodes.getType())) {
					throw new BridgeApiGraphqlException(ErrorType.ValidationError, NO_PERMISSIONS);
			}
       
			if(CODE_IN_BOOK.equalsIgnoreCase(accessCodes.getType())) {
				if(accessCodes.getTotalReactivations()==4){
					throw new BridgeApiGraphqlException(ErrorType.ValidationError, CODE_EXPIRE);
		        }else if(accessCodes.getTotalReactivations()>0 && accessCodes.getTotalReactivations()<4){
		        	
		        	if(getUser(true).getUid().equalsIgnoreCase(accessCodes.getLastActivatedBy()) && !isExpired(accessCodes.getEndDate())){
			        	if(isWithInSixMonths(accessCodes.getLastActivatedDate())){
			        		throw new BridgeApiGraphqlException(ErrorType.ValidationError, ALREADY_ACTIVATED);
			        	}else{
			        		throw new BridgeApiGraphqlException(ErrorType.ValidationError, ALREADY_ACTIVATED);
			        	}
		        	
		        	}else{
		        		if(isWithInSixMonths(accessCodes.getLastActivatedDate())&& !isExpired(accessCodes.getEndDate())){
			        		throw new BridgeApiGraphqlException(ErrorType.ValidationError, IN_USE);
			        	}
		        	}
		        	
		        }
				
			}else if(CODE_SINGLE_USE.equalsIgnoreCase(accessCodes.getType()) ) {
				if(accessCodes.getTotalReactivations() == 1 ){
					if(isExpired(accessCodes.getEndDate())){
						throw new BridgeApiGraphqlException(ErrorType.ValidationError, CODE_EXPIRE);
					}else{
						if(getUser(true).getUid().equalsIgnoreCase(accessCodes.getLastActivatedBy())){
							throw new BridgeApiGraphqlException(ErrorType.ValidationError, ALREADY_ACTIVATED);
						}else{
							throw new BridgeApiGraphqlException(ErrorType.ValidationError, IN_USE);
						}
						
					}
	        	}
			}
			
		}else{
			throw new BridgeApiGraphqlException(ErrorType.ValidationError, NOT_AUTHORIZED);
		}
		
		return true;
		
	}
	
	public Boolean validateVoucherCode(Voucher voucher){
		Set<String> roles = getUser(false).getRoles();
		if((roles.contains(CODE_STUDENT) || roles.contains(CODE_TEACHER)) ) {
			
			if(voucher.getTotalReactivations() == 1){
				
				if(!isExpired(voucher.getEndDate())){
					if(getUser(true).getUid().equalsIgnoreCase(voucher.getLastActivatedBy())){
						throw new BridgeApiGraphqlException(ErrorType.ValidationError, ALREADY_ACTIVATED);
					}else{
						throw new BridgeApiGraphqlException(ErrorType.ValidationError, IN_USE);
					}
				}else{
					throw new BridgeApiGraphqlException(ErrorType.ValidationError, CODE_EXPIRE);
				}
				
			}else if(voucher.getTotalReactivations() == 0){
				
				AccessCodes accessCodes = accessCodeRepository.findByAccessCode(voucher.getAccessCode());
				
				if(null != accessCodes.getProducts()){
					List<Product> products = productRepository.findAllByProductIdIn(new HashSet<>(accessCodes.getProducts()));
					if(null != products){
						Boolean flag = false;
						for(Product product :products){
							if(voucher.getType().equals(product.getReactivationVoucherCodeType())){
								flag=true;
								break;
							}
						}
						if(!flag){
							throw new BridgeApiGraphqlException(ErrorType.ValidationError, WRONG_VOUCHER_CODE);
						}
					}
				}
				
				
			}
			
		}else{
			throw new BridgeApiGraphqlException(ErrorType.ValidationError, NOT_AUTHORIZED);
		}
		
		return true;
	}
	
	private boolean isWithInSixMonths(String lastActivatedDat){

		DateTime dt = new DateTime(DateTimeZone.forID(TIME_ZONE));
		Calendar referenceDate= Calendar.getInstance();
		referenceDate.setTimeInMillis(dt.getMillis());
		Calendar dateToBeTested = Calendar.getInstance();
		dateToBeTested.setTimeInMillis(Long.parseLong(lastActivatedDat));
		
        int year1 = referenceDate.get(Calendar.YEAR);
        int month1 = referenceDate.get(Calendar.MONTH);
        int day1 = referenceDate.get(Calendar.DATE);

        int year2 = dateToBeTested.get(Calendar.YEAR);
        int month2 = dateToBeTested.get(Calendar.MONTH);
        int day2 = dateToBeTested.get(Calendar.DATE);

        if ((year1 * 12 + month1) - (year2 * 12 + month2) < 6 && day1 > day2)
            return true;
        return false;
    }
	
	private boolean isExpired(String expiryDate){
		DateTime dt = new DateTime(DateTimeZone.forID(TIME_ZONE));
		Calendar referenceDate= Calendar.getInstance();
		referenceDate.setTimeInMillis(dt.getMillis());
		Calendar dateToBeTested = Calendar.getInstance();
		dateToBeTested.setTimeInMillis(Long.parseLong(expiryDate));
		
		if(referenceDate.compareTo(dateToBeTested) > 0){
			return true;
		}else if(referenceDate.compareTo(dateToBeTested) < 0){
			return false;
		}
		return false;
	
            
    }
	
	public void isCreatedByEmpty(String createdBy) {
		if (Utils.isEmpty(createdBy)) {
			//logger.error("Created By should not be empty");
			throw new BridgeApiGeneralException("Created By should not be empty");
		}
	}
	
	public void isProductIdEmpty(String productId) {
		if (Utils.isEmpty(productId)) {
			//logger.error("Unabel to fetch Prouduct due to Product Id is empty.");
			throw new BridgeApiGeneralException(PRODUCTID_SHOULD_PRESENT);
		}
	}
	public void isListOfProductIdsEmpty(List<String> listOfProductIds) {
		if(listOfProductIds == null || listOfProductIds.isEmpty()) {
			 //logger.error("Unabel to fetch Product due to Product Ids list is empty.");
		      throw new BridgeApiGeneralException(PRODUCTIDS_SHOULD_PRESENT);
		}
	}
	public void validateProduct(String productId) {
		Product product = productRepository.findByProductId(productId);
		if(product==null || !productId.equals(product.getProductId())) {
			 //logger.error(productId + " " + PRODUCT_DOES_NOT_EXISTS);
		      throw new BridgeApiGeneralException(PRODUCT_DOES_NOT_EXISTS);
		}
	}
	public void isAccessCodeEmpty(String accessCode) {
		if (Utils.isEmpty(accessCode)) {
			//logger.error("Unabel to fetch Access Code due to Access code is empty.");
			throw new BridgeApiGeneralException(ACCESS_CODE_SHOULD_NOT_EMPTY);
		}
	}
	public void validateAccessCode(String accessCode) {
		AccessCodes accessCodeDB = accessCodeRepository.findByAccessCode(accessCode);
		if(accessCodeDB==null || !accessCode.equals(accessCodeDB.getAccessCode())) {
			 //logger.error("Unabel to fetch Access Code due to Access Code:"+accessCode +" is not existed");
		      throw new BridgeApiGeneralException(ACCESS_CODE_DOES_NOT_EXISTS);
		}
	}
	public void isValidAccessCodeType(String type) {
		  boolean isValidAccessCodeType = false;
			if(Utils.isEmpty(type)) {
				throw new BridgeApiGraphqlException(ErrorType.ValidationError, ACCESS_CODE_TYPE_SHOULD_NOT_EMPTY);
			}
			for(AccessCodeType codeType:AccessCodeType.values()) {
				if(codeType.getType().equalsIgnoreCase(type)) {
					isValidAccessCodeType= true;
					break;
				}
			}
			if(!isValidAccessCodeType) {
				StringBuffer sb = new StringBuffer();
				for(AccessCodeType codeType: AccessCodeType.values()) {
					sb.append(codeType.getType());
					sb.append(",");
				}
				throw new BridgeApiGraphqlException(ErrorType.ValidationError, ACCESS_CODE_TYPES+sb.toString().substring(0,sb.toString().length()-1));
			}
		}
}
