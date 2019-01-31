package com.pearson.sam.bridgeapi.resolvers;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import com.pearson.sam.bridgeapi.ibridgeservice.ISubscriptionBridgeService;
import com.pearson.sam.bridgeapi.model.AccessCodes;
import com.pearson.sam.bridgeapi.model.KeyType;
import com.pearson.sam.bridgeapi.model.Voucher;

@RunWith(MockitoJUnitRunner.class)
public class SubscriptionResolverTest {

	@Mock
	private ISubscriptionBridgeService subscriptionBridgeService;

	@InjectMocks
	private SubscriptionResolver underTest;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testgenerateMultipleAccessCodes() {
		AccessCodes accessCodes = new AccessCodes();
		accessCodes.setAccessCode("codes");
		when(subscriptionBridgeService.createAccessCodeSubscription(any(AccessCodes.class))).thenReturn(new AccessCodes());
		AccessCodes code = underTest.generateMultipleAccessCodes("accessCode", accessCodes);
		Assert.assertNotNull(code);
	}

	@Test
	public void testgenerateVoucherCodeSubscription() {
		Voucher voucher = new Voucher();
		voucher.setVoucherCode("voucherCode11111");
		when(subscriptionBridgeService.createVoucherSubscription(any(Voucher.class))).thenReturn(voucher);
		voucher = underTest.generateVoucherCodeSubscription("voucherCode", voucher);
		Assert.assertNotNull(voucher);
	}

	@Test
	public void testGetAccessCodeSubscription() {
		AccessCodes accessCodes = new AccessCodes();
		accessCodes.setAccessCode("ABC");
		when(subscriptionBridgeService.getAccessCodeSubscription(accessCodes)).thenReturn(getAccessCodes());
		AccessCodes reult = underTest.getAccessCodeSubscription("accesscode", "productcode");
		Assert.assertNotNull(reult);
	}
	private AccessCodes getAccessCodes() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -7);
		AccessCodes accessCodes = new AccessCodes();
		accessCodes.setAccessCode("accessCodes");
		accessCodes.setCode("7HJ-HCG-GG3X");
		accessCodes.setUserName("mlsreekanth");
		accessCodes.setQuantity(10);
		accessCodes.setType("PP");
		accessCodes.setLastActivatedBy("PP");
		accessCodes.setLastActivatedDate(cal.getTime().toString());
		return accessCodes;
	}
}
