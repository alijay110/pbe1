package com.pearson.sam.bridgeapi.bridgeserviceimpl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.pearson.sam.bridgeapi.exceptions.BridgeApiGraphqlException;
import com.pearson.sam.bridgeapi.iservice.ISubscriptionService;
import com.pearson.sam.bridgeapi.iservice.IUserService;
import com.pearson.sam.bridgeapi.model.AccessCodes;
import com.pearson.sam.bridgeapi.model.Voucher;
import com.pearson.sam.bridgeapi.resthelper.SAMServiceException;

/**
 * Subscription Bridge Service Unit Test Cases
 * @author VVaijPa
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class SubscriptionBridgeServiceIMplTest {
	
	@InjectMocks
	private SubscriptionBridgeServiceIMpl underTest = new SubscriptionBridgeServiceIMpl();
	
	@Mock
	private ISubscriptionService subscriptionService;
	
	@Mock
	private IUserService userService;
	
	@Test
	public void testCreateAccessCodeSubscription() {
		when(userService.checkAvailability(any(String.class))).thenReturn(false);
		when(subscriptionService.createAccessCodeSubscription(any(AccessCodes.class))).thenReturn(getAccessCodes());
		AccessCodes result = underTest.createAccessCodeSubscription(getAccessCodes());
		Assert.assertNotNull(result);
	}
	
	@Test(expected = BridgeApiGraphqlException.class)
	public void testCreateAccessCodeSubscriptionEmptyCreatedBy() {
		when(userService.checkAvailability(any(String.class))).thenReturn(true);
		underTest.createAccessCodeSubscription(getAccessCodes());
	}
	
	@Test(expected = BridgeApiGraphqlException.class)
	public void testCreateAccessCodeSubscriptionWhenSAMThrowsException() {
		when(userService.checkAvailability(any(String.class))).thenThrow(new SAMServiceException("Invalid CreatedBy"));
		underTest.createAccessCodeSubscription(getAccessCodes());
	}
	
	@Test
	public void getAccessCodeSubscription() {
		List<String> ids = new ArrayList<>();
		ids.add("ACCESSCODE01");
		ids.add("ACCESSCODE02");
		
		AccessCodes accessCodes = new AccessCodes();
		accessCodes.setAccessCode("ABC");

		when(subscriptionService.getAccessCodeSubscription(accessCodes)).thenReturn(getAccessCodes());
		AccessCodes result = underTest.getAccessCodeSubscription(accessCodes);
		Assert.assertNotNull(result);
	}
	
	@Test
	public void testCreateVoucherSubscription() {
		when(subscriptionService.createVoucherSubscription(any(Voucher.class))).thenReturn(new Voucher());
		Voucher result = underTest.createVoucherSubscription(new Voucher());
		Assert.assertNotNull(result);
	}
	
	private AccessCodes getAccessCodes() {
		AccessCodes accessCodes = new AccessCodes();
		accessCodes.setAccessCode("accessCode");
		//accessCodes.setCreatedBy("createdBy");

		return accessCodes;
	}

	private List<AccessCodes> getAccessCodesList() {
		List<AccessCodes> accessCodesList = Arrays.asList(getAccessCodes(), getAccessCodes());
		return accessCodesList;
	}
}
