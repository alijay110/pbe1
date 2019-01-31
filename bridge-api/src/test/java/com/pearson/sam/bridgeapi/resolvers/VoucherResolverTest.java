package com.pearson.sam.bridgeapi.resolvers;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.pearson.sam.bridgeapi.config.AuthContext;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGraphqlException;
import com.pearson.sam.bridgeapi.ibridgeservice.IVoucherBridgeService;
import com.pearson.sam.bridgeapi.model.Sort;
import com.pearson.sam.bridgeapi.model.Voucher;

import io.leangen.graphql.execution.SortField.Direction;

/**
 * Voucher Resolver unit test cases
 * 
 * @author Param
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class VoucherResolverTest {

	@Mock
	private AuthContext authContext;

	@InjectMocks
	private VoucherResolver underTest;

	@Mock
	private IVoucherBridgeService voucherBridgeService;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGenerateMultipleVoucherCodes() {
		Voucher voucher = new Voucher();
		voucher.setQuantity(20);
		when(voucherBridgeService.createMultiple(voucher)).thenReturn(getVoucherList());
		List<Voucher> result = underTest.generateMultipleVoucherCodes(voucher);
		Assert.assertNotNull(result);
	}
	
	@Test(expected=BridgeApiGraphqlException.class)
	public void testGenerateMultipleVoucherCodesWithMoreQuantity() {
		Voucher voucher = new Voucher();
		voucher.setQuantity(26);
		when(voucherBridgeService.createMultiple(voucher)).thenReturn(getVoucherList());
		underTest.generateMultipleVoucherCodes(voucher);
	}

	@Test
	public void testGenerateLargeNumberOfVoucherCodes() {
		Voucher voucher = new Voucher();
		voucher.setQuantity(20);
		when(voucherBridgeService.createMultiple(voucher)).thenReturn(getVoucherList());
		Voucher result = underTest.generateLargeNumberOfVoucherCodes(voucher);
		Assert.assertNotNull(result);
	}
	
	@Test(expected=BridgeApiGraphqlException.class)
	public void testGenerateLargeNumberOfVoucherCodesWithNoCode() {
		Voucher voucher = new Voucher();
		voucher.setQuantity(20);
		List<Voucher> lstVoucher = new ArrayList<Voucher>();
		lstVoucher.add(null);
		when(voucherBridgeService.createMultiple(voucher)).thenReturn(lstVoucher);
		underTest.generateLargeNumberOfVoucherCodes(voucher);
	}

	@Test
	public void testGenerateVouchers() throws IOException, ProcessingException {
		when(voucherBridgeService.create(any(Voucher.class))).thenReturn(new Voucher());
		Voucher result = underTest.generateVouchers(new Voucher());
		Assert.assertNotNull(result);
	}

	@Test
	public void testGetVoucher() throws IOException, ProcessingException {
		when(voucherBridgeService.fetchOne(any(Voucher.class))).thenReturn(new Voucher());
		Voucher result = underTest.getVoucher("voucherId");
		Assert.assertNotNull(result);
	}

	@Test
	public void testUpdateVoucher() throws IOException, ProcessingException {
		when(voucherBridgeService.update(any(Voucher.class))).thenReturn(new Voucher());
		Voucher result = underTest.updateVoucher("voucherCode", new Voucher());
		Assert.assertNotNull(result);
	}

	@Test
	public void testGetPaginatedVoucherCodeTableData() throws IOException, ProcessingException {
		Sort sort = new Sort();
		sort.setField("voucherId");
		io.leangen.graphql.execution.SortField.Direction d = Direction.ASC;
		sort.setOrder(d);
		when((voucherBridgeService.pageIt(any(Pageable.class), any(Example.class)))).thenReturn(getVoucher());
//		Page<Voucher> licenceHistory = underTest.getPaginatedVoucherCodeTableData(0, 15, new Voucher(),
//				StringMatcher.DEFAULT, sort, authContext);
//		Assert.assertNotNull(licenceHistory);
	}

	public Page<Voucher> getVoucher() {

		Page<Voucher> pageVoucher = new PageImpl<Voucher>(getVoucherList());
		return pageVoucher;
	}

	public List<Voucher> getVoucherList() {
		Voucher voucher = new Voucher();
		List<Voucher> lstVoucher = new ArrayList<Voucher>();
		lstVoucher.add(voucher);
		return lstVoucher;
	}
}
