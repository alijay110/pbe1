/**
 * 
 */
package com.pearson.sam.bridgeapi.ibridgeservice;

import com.pearson.sam.bridgeapi.model.AccessCodes;
import com.pearson.sam.bridgeapi.model.KeyType;
import com.pearson.sam.bridgeapi.model.Voucher;

import java.util.List;

/**
 * @author VGUDLSA
 *
 */
public interface ISubscriptionBridgeService {

	AccessCodes createAccessCodeSubscription(AccessCodes accesscode);
	
	Voucher createVoucherSubscription(Voucher graphVoucher);

	AccessCodes getAccessCodeSubscription(AccessCodes accesscodes);

}
