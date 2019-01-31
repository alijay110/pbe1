/**
 * 
 */
package com.pearson.sam.bridgeapi.iservice;

import com.pearson.sam.bridgeapi.model.AccessCodes;
import com.pearson.sam.bridgeapi.model.KeyType;
import com.pearson.sam.bridgeapi.model.Voucher;

import java.util.List;

/**
 * @author VGUDLSA
 *
 */
public interface ISubscriptionService {
	/**
	 * it's update the subscription details into AccessCodes.
	 * @param accesscode
	 * @return AccessCodes
	 */
	AccessCodes createAccessCodeSubscription(AccessCodes accesscode);
	/**
	 * it's get the subscription details of AccessCodes.
	 * @param accesscodes
	 * @return AccsessCode
	 */
	AccessCodes getAccessCodeSubscription(AccessCodes accesscodes);

	Voucher createVoucherSubscription(Voucher graphVoucher);
}
