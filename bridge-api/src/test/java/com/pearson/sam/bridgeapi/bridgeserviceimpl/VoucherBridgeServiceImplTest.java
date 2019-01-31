package com.pearson.sam.bridgeapi.bridgeserviceimpl;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import com.pearson.sam.bridgeapi.bridgeserviceimpl.VoucherBridgeServiceImpl;
import com.pearson.sam.bridgeapi.iservice.IVoucherService;
import com.pearson.sam.bridgeapi.model.Sort;
import com.pearson.sam.bridgeapi.model.Voucher;

import io.leangen.graphql.execution.SortField.Direction;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@RunWith(MockitoJUnitRunner.class)
public class VoucherBridgeServiceImplTest {

	@Mock
	IVoucherService voucherService;
	
	@InjectMocks
	private VoucherBridgeServiceImpl underTest;

	@Test
	public void testcreate() {
		when(voucherService.create(any(Voucher.class))).thenReturn(getVoucher());
		Voucher voucher = underTest.create(getVoucher());
		assertNotNull(voucher);
		
	}

	@Test
	public void testcreateMultiple() {
		when(voucherService.createMultiple(any(Voucher.class))).thenReturn(asList(getVoucher()));
		List<Voucher> voucher = underTest.createMultiple(getVoucher());
		assertNotNull(voucher);
	}

	@Test
	public void testupdate() {
		when(voucherService.update(any(Voucher.class))).thenReturn(getVoucher());
		Voucher voucher = underTest.update(getVoucher());
		assertNotNull(voucher);
	}

	@Test
	public void testfetchOne() {
		when(voucherService.fetchOne(any(Voucher.class))).thenReturn(getVoucher());
		Voucher voucher = underTest.fetchOne(getVoucher());
		assertNotNull(voucher);
	}

	@Test
	public void testgetVoucherFromBatch() {
		when(voucherService.getVoucherFromBatch(anyString())).thenReturn(asList(getVoucher()));
		List<Voucher> voucherlist = underTest.getVoucherFromBatch("TEST");
		assertNotNull(voucherlist);
	}
	
	@Test
	public void testpageIt() {		
		Sort sort = new Sort();
		sort.setField("voucherCode");
		io.leangen.graphql.execution.SortField.Direction direction = Direction.ASC;
		sort.setOrder(direction);
		Pageable pageable = PageRequest.of(0, 10,
				org.springframework.data.domain.Sort.Direction.valueOf(sort.getOrder().toString()), sort.getField());
		Example<Voucher> e = Example.of(new Voucher(),
				ExampleMatcher.matching().withStringMatcher(StringMatcher.DEFAULT));
		when(voucherService.pageIt(any(),any())).thenReturn(getPageofVoucher());
		Page<Voucher> pageOfVoucher = underTest.pageIt(pageable, e);
		assertNotNull(pageOfVoucher);
		
	}
	
	private Page<Voucher> getPageofVoucher() {
		List<Voucher> listvoucher = asList(getVoucher());
		Page<Voucher> pagelistVoucher = new PageImpl<Voucher>(listvoucher);
		return pagelistVoucher;
	}
	
	private Voucher getVoucher() {
		Voucher voucher = new Voucher();
		voucher.setVoucherCode("REA3-E8D3-H9E2");
		voucher.setVoucherId("PP2VOUCHER40");
		voucher.setCreatedBy("PP");
		return voucher;
	}
}
