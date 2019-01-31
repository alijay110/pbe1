package com.pearson.sam.bridgeapi.serviceimpl;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Calendar;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.gson.JsonObject;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGeneralException;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGraphqlException;
import com.pearson.sam.bridgeapi.model.AccessCodes;
import com.pearson.sam.bridgeapi.model.Voucher;
import com.pearson.sam.bridgeapi.repository.AccessCodeRepository;
import com.pearson.sam.bridgeapi.repository.VoucherRepository;
import com.pearson.sam.bridgeapi.samclient.SubscriptionSamClient;
import com.pearson.sam.bridgeapi.validators.ModelValidator;
import com.pearson.sam.bridgeapi.validators.SubscriptionValidator;

@RunWith(MockitoJUnitRunner.class)
public class SubscriptionServiceTest {
	
	@InjectMocks
	private SubscriptionService underTest = new SubscriptionService();
	
	@Mock
	private SubscriptionSamClient subscriptionSamClient;

	@Mock
	private AccessCodeRepository accessCodeRepository;

	@Mock
	private VoucherRepository voucherRepository;

	@Mock
	private SubscriptionValidator validator;
	
	@Mock
	private ModelValidator modelValidator;

	@Test
	public void testCreateAccessCodeSubscription() {
		doNothing().when(modelValidator).validateModel(any(AccessCodes.class),anyString());
		when(accessCodeRepository.findByAccessCode(any(String.class))).thenReturn(getAccessCodes());
		when(subscriptionSamClient.create(any())).thenReturn(getAccessCodes());
		when(accessCodeRepository.save(any(AccessCodes.class))).thenReturn(getAccessCodes());
		when(validator.validateAccessCode(any(AccessCodes.class))).thenReturn(true);
		AccessCodes response = underTest.createAccessCodeSubscription(getAccessCodes());
		Assert.assertNotNull(response);
	}
	
	@Test(expected=BridgeApiGraphqlException.class)
	public void testCreateAccessCodeSubscriptionValidationError() {
		when(subscriptionSamClient.create(any(JsonObject.class))).thenReturn(getAccessCodes());
		when(accessCodeRepository.save(any(AccessCodes.class))).thenThrow(new BridgeApiGeneralException("Exception in Creating access codes"));
		underTest.createAccessCodeSubscription(getAccessCodes());
	}
	
	@Test
	public void testCreateVoucherSubscription() {
		when(voucherRepository.findByVoucherCode(any(String.class))).thenReturn(getVoucher());
		when(accessCodeRepository.findByAccessCode(any(String.class))).thenReturn(getAccessCodes());
		when(subscriptionSamClient.create(any())).thenReturn(getAccessCodes());
		when(voucherRepository.save(any(Voucher.class))).thenReturn(getVoucher());
		when(validator.validateVoucherCode(any(Voucher.class))).thenReturn(true);

		Voucher response = underTest.createVoucherSubscription(getVoucher());
		Assert.assertNotNull(response);
	}
	
	@Test(expected=BridgeApiGraphqlException.class)
	public void testCreateVoucherSubscriptionVoucherEmpty() {
		when(voucherRepository.findByVoucherCode(anyString())).thenReturn(null);
		when(accessCodeRepository.findByAccessCode(anyString())).thenThrow(new BridgeApiGraphqlException("Exception in fetching record"));;
		Voucher response = underTest.createVoucherSubscription(getVoucher());
		Assert.assertNotNull(response);
	}
	
	@Test(expected=BridgeApiGraphqlException.class)
	public void testCreateVoucherSubscriptionVoucherEmpty1() {
		when(voucherRepository.findByVoucherCode(any(String.class))).thenReturn(getVoucher());
		when(accessCodeRepository.findByAccessCode(any(String.class))).thenReturn(getAccessCodes());
		when(validator.validateVoucherCode(any(Voucher.class))).thenReturn(true);
		when(subscriptionSamClient.create(any())).thenReturn(null);
		when(subscriptionSamClient.create(any())).thenThrow(new BridgeApiGraphqlException("Exception in Creating access codes"));
		Voucher response = underTest.createVoucherSubscription(getVoucher());
		Assert.assertNotNull(response);
	}
	
	@Test
	public void testFetchMultiple() {
		when(accessCodeRepository.existsByCode(any(String.class))).thenReturn(true);

		AccessCodes accessCodes = new AccessCodes();
		accessCodes.setAccessCode("ABC");
		when(subscriptionSamClient.getAccessCodeSubscription(accessCodes)).thenReturn(getAccessCodes());
		AccessCodes result = underTest.getAccessCodeSubscription(accessCodes);
		Assert.assertNotNull(result);
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
	
	private Voucher getVoucher() {
		Voucher voucher = new Voucher();
		voucher.setId("id");
		voucher.setVoucherId("voucherId");
		voucher.setVoucherCode("voucherCode");
		voucher.setAccessCode("accessCode");
		voucher.setLastActivatedBy("Param");
		voucher.setLastActivatedBy("Param");

		return voucher;
	}
}
