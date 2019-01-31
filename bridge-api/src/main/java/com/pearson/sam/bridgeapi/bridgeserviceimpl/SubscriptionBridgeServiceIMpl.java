/**
 * 
 */
package com.pearson.sam.bridgeapi.bridgeserviceimpl;

import com.pearson.sam.bridgeapi.exceptions.BridgeApiGraphqlException;
import com.pearson.sam.bridgeapi.ibridgeservice.ISubscriptionBridgeService;
import com.pearson.sam.bridgeapi.iservice.ISubscriptionService;
import com.pearson.sam.bridgeapi.iservice.IUserService;
import com.pearson.sam.bridgeapi.model.AccessCodes;
import com.pearson.sam.bridgeapi.model.KeyType;
import com.pearson.sam.bridgeapi.model.Voucher;
import com.pearson.sam.bridgeapi.resthelper.SAMServiceException;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author VGUDLSA
 *
 */
@Component
public class SubscriptionBridgeServiceIMpl implements ISubscriptionBridgeService {
	
	@Autowired
	ISubscriptionService subscriptionService;
	
	@Autowired
	private IUserService userService;

	/* (non-Javadoc)
	 * @see com.pearson.sam.bridgeapi.Ibridgeservice.ISubscriptionBridgeService#create(com.pearson.sam.bridgeapi.model.AccessCodes)
	 */
	@Override
	public AccessCodes createAccessCodeSubscription(AccessCodes accesscode) {
		try{
			if(!userService.checkAvailability(accesscode.getCreatedBy()))
			   return subscriptionService.createAccessCodeSubscription(accesscode);
			else
				throw new BridgeApiGraphqlException("CreatedBy user not found");
		}catch(SAMServiceException e){
			throw new BridgeApiGraphqlException(e.getMessage());
		}
	}

	
	@Override
	public Voucher createVoucherSubscription(Voucher graphVoucher) {
		return subscriptionService.createVoucherSubscription(graphVoucher);
	}


	@Override
	public AccessCodes getAccessCodeSubscription(AccessCodes accesscodes) {
		// TODO Auto-generated method stub
		return subscriptionService.getAccessCodeSubscription(accesscodes);
	}

}
